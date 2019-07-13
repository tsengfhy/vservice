package com.tsengfhy.vservice.dto;

import com.tsengfhy.vservice.basic.dto.AbstractDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Demo")
public class DemoDto extends AbstractDto {

    @ApiModelProperty(value = "Title", position = 1, example = "My first message")
    private String title;

    @ApiModelProperty(value = "Content", position = 2, example = "I have a few info to tell u")
    private String content;
}
