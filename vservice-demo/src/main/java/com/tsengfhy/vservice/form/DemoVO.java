package com.tsengfhy.vservice.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("DemoVO")
public class DemoVO {

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "title", position = 1, required = true, example = "My first message")
    private String title;

    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "content", position = 2, required = true, example = "I have a few info to tell u")
    private String content;
}
