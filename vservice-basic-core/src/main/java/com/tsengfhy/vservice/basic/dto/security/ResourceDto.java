package com.tsengfhy.vservice.basic.dto.security;

import com.tsengfhy.vservice.basic.dto.AbstractDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Resource")
public class ResourceDto extends AbstractDto {

    @ApiModelProperty(value = "Type", position = 1)
    private String type;

    @ApiModelProperty(value = "Name", position = 2)
    private String name;

    @ApiModelProperty(value = "Description", position = 3)
    private String description;

    @ApiModelProperty(value = "Url", position = 4, example = "/index.html")
    private String url;

    @ApiModelProperty(value = "Method", position = 5, allowableValues = "GET,POST,PUT,DELETE")
    private String method;

    @ApiModelProperty(value = "Index", position = 6)
    private Integer series;

    @ApiModelProperty(value = "Parent Id", position = 7)
    private Long pid;
}
