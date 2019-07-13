package com.tsengfhy.vservice.controller;

import com.tsengfhy.vservice.basic.dto.file.FileDto;
import com.tsengfhy.vservice.basic.template.FileTemplate;
import com.tsengfhy.vservice.basic.utils.FileUtils;
import com.tsengfhy.vservice.basic.utils.RestUtils;
import com.tsengfhy.vservice.basic.web.core.Rest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/public/file")
@Api(tags = {"File Demo"}, description = "A demo of common api for file system")
public class FileDemoCtrl {

    private Long fileId;

    @Autowired(required = false)
    private FileTemplate fileTemplate;

    @PostMapping(value = "")
    @ApiOperation(value = "Save file", notes = "Save file into file system and show the result info")
    public Rest<FileDto> add() {
        File file = new File("D:\\test.jpg");

        FileDto dto = new FileDto();
        dto.setForeignType("aaaa");
        dto.setForeignId(1111L);
        FileUtils.fillFile(dto, file);

        fileTemplate.save(dto);
        fileId = dto.getId();
        return RestUtils.save(true, "", dto);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "Get files", notes = "Get file info")
    public Rest<Page<FileDto>> list() {

        FileDto dto = new FileDto();
        dto.setForeignType("aaaa");
        dto.setForeignId(1111L);

        return RestUtils.select(fileTemplate.find(dto, 1, 10, ""));
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get file", notes = "Get file by Id")
    public Object get(
            @ApiParam(value = "File Id") @PathVariable Long id
    ) throws IOException {

        FileDto dto = fileTemplate.findById(Optional.ofNullable(id).orElse(fileId));

        byte[] byteArray = IOUtils.toByteArray(dto.getInputStream());
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return "<img src=\"data:image/png;base64," + base64Encoder.encodeBuffer(byteArray).trim() + "\" />";
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete file", notes = "Delete file by Id")
    public Rest delete(
            @ApiParam(value = "File Id") @PathVariable Long id
    ) {

        fileTemplate.delete(Optional.ofNullable(id).orElse(fileId));
        return RestUtils.delete(true, "");
    }

    @PutMapping(value = "/append")
    @ApiOperation(value = "Append File", notes = "Save an append file and show it")
    public Rest<String> append() throws IOException {

        FileDto dto = new FileDto();
        dto.setForeignType("aaaa");
        dto.setForeignId(1111L);
        dto.setType(com.tsengfhy.vservice.basic.constant.File.OTHER);
        dto.setName("test.txt");
        dto.setSize(8L);
        dto.setInputStream(new ByteArrayInputStream("adsf".getBytes()));
        FileDto.Chunk chunk = new FileDto.Chunk();
        chunk.setIndex(0);
        chunk.setSize(4L);
        chunk.setOver(false);
        dto.setChunk(chunk);
        fileTemplate.save(dto);

        fileTemplate.locate(dto.getId());

        FileDto dto2 = new FileDto();
        dto2.setId(dto.getId());
        dto2.setInputStream(new ByteArrayInputStream("eeee".getBytes()));
        FileDto.Chunk chunk2 = new FileDto.Chunk();
        chunk2.setIndex(1);
        chunk2.setSize(4L);
        chunk2.setOver(true);
        dto2.setChunk(chunk2);
        fileTemplate.save(dto2);

        FileDto dto3 = fileTemplate.findById(dto.getId());
        fileTemplate.delete(dto.getId());

        byte[] byteArray = IOUtils.toByteArray(dto3.getInputStream());
        return RestUtils.select(new String(byteArray));
    }
}
