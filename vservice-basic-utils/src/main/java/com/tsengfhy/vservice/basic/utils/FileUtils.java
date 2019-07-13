package com.tsengfhy.vservice.basic.utils;

import com.tsengfhy.vservice.basic.dto.file.FileDto;
import com.tsengfhy.vservice.basic.exception.file.FileException;
import com.tsengfhy.vservice.basic.exception.file.FileIOException;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@UtilityClass
public final class FileUtils {

    private final static Map<String, com.tsengfhy.vservice.basic.constant.File> FILE_TYPE_MAP = new ConcurrentHashMap<>();

    static {
        String[] images = new String[]{"bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};
        FILE_TYPE_MAP.putAll(Arrays.stream(images).collect(Collectors.toMap(item -> item, item -> com.tsengfhy.vservice.basic.constant.File.IMAGE)));
        String[] documents = new String[]{"txt", "doc", "docx", "xls", "htm", "html", "jsp", "rtf", "wpd", "pdf", "ppt"};
        FILE_TYPE_MAP.putAll(Arrays.stream(documents).collect(Collectors.toMap(item -> item, item -> com.tsengfhy.vservice.basic.constant.File.DOCUMENT)));
        String[] videos = new String[]{"mp4", "avi", "mov", "wmv", "asf", "navi", "3gp", "mkv", "f4v", "rmvb", "webm"};
        FILE_TYPE_MAP.putAll(Arrays.stream(videos).collect(Collectors.toMap(item -> item, item -> com.tsengfhy.vservice.basic.constant.File.VIDEO)));
        String[] musics = new String[]{"mp3", "wma", "wav", "mod", "ra", "cd", "md", "asf", "aac", "vqf", "ape", "mid", "ogg", "m4a", "vqf"};
        FILE_TYPE_MAP.putAll(Arrays.stream(musics).collect(Collectors.toMap(item -> item, item -> com.tsengfhy.vservice.basic.constant.File.MUSIC)));
    }

    @Nullable
    private static com.tsengfhy.vservice.basic.constant.File getType(String filename) {

        return Optional.ofNullable(FilenameUtils.getExtension(filename))
                .map(String::toLowerCase)
                .map(FILE_TYPE_MAP::get)
                .orElse(com.tsengfhy.vservice.basic.constant.File.OTHER);
    }

    @Nullable
    public static com.tsengfhy.vservice.basic.constant.File getType(File file) {

        return Optional.ofNullable(file)
                .filter(File::exists)
                .map(File::getName)
                .map(FileUtils::getType)
                .orElse(null);
    }

    @Nullable
    public static com.tsengfhy.vservice.basic.constant.File getType(MultipartFile file) {

        return Optional.ofNullable(file)
                .filter(f -> !f.isEmpty())
                .map(MultipartFile::getOriginalFilename)
                .map(FileUtils::getType)
                .orElse(null);
    }

    public static void fillFile(FileDto dto, File file) throws FileException {

        Optional.ofNullable(file)
                .filter(File::exists)
                .ifPresent(f -> {
                    dto.setName(f.getName());
                    dto.setType(getType(f));
                    dto.setSize(f.length());
                    try {
                        dto.setInputStream(org.apache.commons.io.FileUtils.openInputStream(f));
                    } catch (IOException e) {
                        throw new FileIOException(MessageSourceUtils.getMessage("File.badIO", "File can't read"), e);
                    }
                });
    }

    public static void fillFile(FileDto dto, MultipartFile file) throws FileException {

        Optional.ofNullable(file)
                .filter(f -> !f.isEmpty())
                .ifPresent(f -> {
                    dto.setName(f.getOriginalFilename());
                    dto.setType(getType(f));
                    dto.setSize(f.getSize());
                    try (InputStream is = f.getInputStream()) {
                        dto.setInputStream(is);
                    } catch (IOException e) {
                        throw new FileIOException(MessageSourceUtils.getMessage("File.badIO", "File can't read"));
                    }
                });
    }

    public static void fillChunk(FileDto dto, MultipartFile file, String name, long size, int index, boolean over) throws FileException {

        Optional.ofNullable(file)
                .filter(f -> !f.isEmpty())
                .ifPresent(f -> {
                    dto.setName(name);
                    dto.setType(getType(name));
                    dto.setSize(size);
                    try (InputStream is = f.getInputStream()) {
                        dto.setInputStream(is);
                    } catch (IOException e) {
                        throw new FileIOException(MessageSourceUtils.getMessage("File.badIO", "File can't read"));
                    }

                    FileDto.Chunk chunk = new FileDto.Chunk();
                    chunk.setIndex(index);
                    chunk.setSize(f.getSize());
                    chunk.setOver(over);
                    dto.setChunk(chunk);
                });
    }
}
