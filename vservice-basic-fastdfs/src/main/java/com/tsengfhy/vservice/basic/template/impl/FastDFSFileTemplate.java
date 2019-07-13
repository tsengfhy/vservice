package com.tsengfhy.vservice.basic.template.impl;

import com.github.tobato.fastdfs.domain.conn.ConnectionManager;
import com.github.tobato.fastdfs.domain.fdfs.StorageNode;
import com.github.tobato.fastdfs.domain.fdfs.StorageNodeInfo;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.domain.proto.storage.*;
import com.github.tobato.fastdfs.service.TrackerClient;
import com.tsengfhy.vservice.basic.constant.File;
import com.tsengfhy.vservice.basic.domain.SysFile;
import com.tsengfhy.vservice.basic.dto.file.FileDto;
import com.tsengfhy.vservice.basic.exception.file.FileException;
import com.tsengfhy.vservice.basic.exception.file.FileIOException;
import com.tsengfhy.vservice.basic.exception.file.NoSuchFileException;
import com.tsengfhy.vservice.basic.repository.SysFileRepository;
import com.tsengfhy.vservice.basic.template.FileTemplate;
import com.tsengfhy.vservice.basic.utils.DataUtils;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
public class FastDFSFileTemplate implements FileTemplate, InitializingBean {

    private TrackerClient trackerClient;

    private ConnectionManager connectionManager;

    private ThumbImageConfig config;

    private SysFileRepository sysFileRepository;

    private Cache cache;

    public void setCacheManager(CacheManager cacheManager) {
        cache = cacheManager.getCache(com.tsengfhy.vservice.basic.constant.Cache.UPLOAD.name());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.trackerClient, "TrackerClient must not be null!");
        Assert.notNull(this.connectionManager, "ConnectionManager must not be null!");
        Assert.notNull(this.config, "ThumbImageConfig must not be null!");
        Assert.notNull(this.sysFileRepository, "SysFileRepository must not be null!");
        Assert.notNull(this.cache, "Cache must not be null!");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Upload implements Serializable {

        private static final long serialVersionUID = 0L;

        private String uri;
        private int index;
        private long size;

        Upload step(int index, long size) {
            this.index = index;
            this.size += size;
            return this;
        }
    }

    @Override
    public int locate(Long id) {

        return Optional.ofNullable(cache.get(id, Upload.class))
                .map(Upload::getIndex)
                .orElseThrow(() -> new NoSuchFileException(MessageSourceUtils.getMessage("File.noFile", new Object[]{id}, "No file with key: " + id)));
    }

    @Override
    @Transactional
    public void save(FileDto dto) throws FileException {

        final boolean isChunk = Optional.ofNullable(dto.getChunk()).isPresent();
        if (isChunk && dto.getChunk().getIndex() > 0) {
            //Continued transmission
            Upload upload = Optional.ofNullable(cache.get(dto.getId(), Upload.class)).orElseThrow(() -> new NoSuchFileException(MessageSourceUtils.getMessage("File.noFile", new Object[]{dto.getId()}, "No file with key: " + dto.getId())));
            if (dto.getChunk().getIndex() - upload.getIndex() != 1) {
                throw new FileException(MessageSourceUtils.getMessage("File.wrongChunk", new Object[]{upload.getIndex() + 1}, "Wrong chunk, expect index: " + (upload.getIndex() + 1)));
            }

            StorePath storePath = StorePath.parseFromUrl(upload.getUri());
            StorageNodeInfo client = this.trackerClient.getUpdateStorage(storePath.getGroup(), storePath.getPath());

            StorageModifyCommand command = new StorageModifyCommand(storePath.getPath(), dto.getInputStream(), dto.getChunk().getSize(), upload.getSize());
            this.connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);

            if (!dto.getChunk().isOver()) {
                cache.put(dto.getId(), upload.step(dto.getChunk().getIndex(), dto.getChunk().getSize()));
            } else {
                cache.evict(dto.getId());

                SysFile sysFile = sysFileRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchFileException(MessageSourceUtils.getMessage("File.noFile", new Object[]{dto.getId()}, "No file with key: " + dto.getId())));

                sysFile.setCompleted("1");
                sysFileRepository.save(sysFile);
            }
        } else {
            try {
                byte[] bytes = IOUtils.toByteArray(dto.getInputStream());
                String extension = FilenameUtils.getExtension(dto.getName()).toLowerCase();
                StorageNode client = this.trackerClient.getStoreStorage();

                StorageUploadFileCommand command = new StorageUploadFileCommand(client.getStoreIndex(), new ByteArrayInputStream(bytes), extension, isChunk ? dto.getChunk().getSize() : dto.getSize(), isChunk);
                StorePath storePath = this.connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
                dto.setUri(storePath.getFullPath());

                if (!isChunk && File.IMAGE.equals(dto.getType())) {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    Thumbnails.of(new ByteArrayInputStream(bytes)).size(config.getWidth(), config.getHeight()).toOutputStream(os);
                    InputStream thumbIs = new ByteArrayInputStream(os.toByteArray());

                    StorageUploadSlaveFileCommand slaveCommand = new StorageUploadSlaveFileCommand(thumbIs, (long) thumbIs.available(), storePath.getPath(), config.getPrefixName(), extension);
                    StorePath slavePath = this.connectionManager.executeFdfsCmd(client.getInetSocketAddress(), slaveCommand);
                    dto.setThumbUri(slavePath.getFullPath());
                }
            } catch (IOException e) {
                throw new FileIOException(MessageSourceUtils.getMessage("File.badIO", "File can't read"));
            }

            SysFile sysFile = new SysFile();
            BeanUtils.copyProperties(dto, sysFile);
            sysFile.setType(dto.getType().name().toLowerCase());

            if (sysFile.getSeries() == null) {
                sysFile.setSeries(sysFileRepository.generateSeries(dto.getForeignId(), dto.getForeignType()));
            }

            if (!isChunk) {
                sysFile.setCompleted("1");
            }

            sysFileRepository.save(sysFile);
            BeanUtils.copyProperties(sysFile, dto);

            if (isChunk) {
                cache.put(dto.getId(), new Upload(dto.getUri(), dto.getChunk().getIndex(), dto.getChunk().getSize()));
            }
        }
    }

    @Override
    @Transactional
    public void update(FileDto dto) throws FileException {

        SysFile sysFile = sysFileRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchFileException(MessageSourceUtils.getMessage("File.noFile", new Object[]{dto.getId()}, "No file with key: " + dto.getId())));

        BeanUtils.copyProperties(dto, sysFile, "type", "size", "uri", "thumbUri");

        if (sysFile.getSeries() == null) {
            sysFile.setSeries(sysFileRepository.generateSeries(dto.getForeignId(), dto.getForeignType()));
        }

        sysFileRepository.save(sysFile);
        BeanUtils.copyProperties(sysFile, dto);
    }

    @Override
    public FileDto findById(Long id) throws FileException {
        return findById(id, false);
    }

    @Override
    public FileDto findById(Long id, boolean isThumb) throws FileException {

        SysFile sysFile = sysFileRepository.findById(id).orElseThrow(() -> new NoSuchFileException(MessageSourceUtils.getMessage("File.noFile", new Object[]{id}, "No file with key: " + id)));

        FileDto dto = new FileDto();
        BeanUtils.copyProperties(sysFile, dto);
        dto.setType(EnumUtils.getEnumIgnoreCase(File.class, sysFile.getType()));

        StorePath storePath = StorePath.parseFromUrl((isThumb && StringUtils.isNotBlank(sysFile.getThumbUri())) ? sysFile.getThumbUri() : sysFile.getUri());
        StorageNodeInfo client = this.trackerClient.getFetchStorage(storePath.getGroup(), storePath.getPath());
        StorageDownloadCommand command = new StorageDownloadCommand(storePath.getGroup(), storePath.getPath(), 0L, 0L, new DownloadByteArray());
        dto.setInputStream(new ByteArrayInputStream((byte[]) this.connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command)));

        return dto;
    }

    @Override
    public Page<FileDto> find(FileDto dto, int pageNo, int pageSize, String sort) {

        SysFile sysFile = new SysFile();
        BeanUtils.copyProperties(dto, sysFile);
        sysFile.setType(Optional.ofNullable(dto.getType()).map(File::name).map(StringUtils::lowerCase).orElse(null));

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("uri", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("thumbUri", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains());

        Page<SysFile> page = sysFileRepository.findAll(Example.of(sysFile, matcher), PageRequest.of(pageNo - 1, pageSize, DataUtils.toSort(sort)));
        List<FileDto> list = page.getContent()
                .stream()
                .map(file -> {
                    FileDto fileDto = new FileDto();
                    BeanUtils.copyProperties(file, fileDto);
                    fileDto.setType(EnumUtils.getEnumIgnoreCase(File.class, file.getType()));

                    return fileDto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

    @Override
    @Transactional
    public void delete(Long id) throws FileException {

        SysFile sysFile = sysFileRepository.findById(id).orElseThrow(() -> new NoSuchFileException(MessageSourceUtils.getMessage("File.noFile", new Object[]{id}, "No file with key: " + id)));

        StorePath storePath = StorePath.parseFromUrl(sysFile.getUri());
        StorageNodeInfo client = this.trackerClient.getUpdateStorage(storePath.getGroup(), storePath.getPath());
        StorageDeleteFileCommand command = new StorageDeleteFileCommand(storePath.getGroup(), storePath.getPath());
        this.connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);

        if (StringUtils.isNotBlank(sysFile.getThumbUri())) {
            StorePath slavePath = StorePath.parseFromUrl(sysFile.getThumbUri());
            StorageDeleteFileCommand slaveCommand = new StorageDeleteFileCommand(slavePath.getGroup(), slavePath.getPath());
            this.connectionManager.executeFdfsCmd(client.getInetSocketAddress(), slaveCommand);
        }

        sysFileRepository.delete(sysFile);
    }
}
