package com.tsengfhy.vservice.basic.dto.file;

import com.tsengfhy.vservice.basic.dto.AbstractDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.InputStream;

@Data
@ApiModel("File")
public class FileDto extends AbstractDto {

    @ApiModelProperty(value = "Type", position = 1, example = "IMAGE")
    private com.tsengfhy.vservice.basic.constant.File type;

    @ApiModelProperty(value = "Name", position = 2, example = "test.jpg")
    private String name;

    @ApiModelProperty(value = "Size", position = 3)
    private Long size;

    @ApiModelProperty(value = "Uri", position = 4, example = "group1/M00/00/00/rBEvqlxDF8uASiknAAArwrzAcLE367.jpg")
    private String uri;

    @ApiModelProperty(value = "Thumb uri", position = 5, allowEmptyValue = true, example = "group1/M00/00/00/rBEvqlxDF8uASiknAAArwrzAcLE367_150x150.jpg")
    private String thumbUri;

    @ApiModelProperty(value = "Description", position = 6, allowEmptyValue = true, example = "This is just a test file")
    private String description;

    @ApiModelProperty(value = "Foreign type", position = 7)
    private String foreignType;

    @ApiModelProperty(value = "Foreign Id", position = 8)
    private Long foreignId;

    @ApiModelProperty(value = "Index", position = 9)
    private Integer series;

    @ApiModelProperty(value = "Complete flag", position = 10, allowEmptyValue = true)
    private String completed;

    @ApiModelProperty(hidden = true)
    private transient InputStream inputStream;

    @ApiModelProperty(value = "Chunk", position = 11, allowEmptyValue = true)
    private Chunk chunk;

    @Data
    @ApiModel("Chunk")
    public static class Chunk {

        @ApiModelProperty(value = "Index", position = 1)
        private Integer index;

        @ApiModelProperty(value = "Size", position = 2)
        private Long size;

        @ApiModelProperty(value = "Over flag", position = 3)
        private boolean over;
    }
}
