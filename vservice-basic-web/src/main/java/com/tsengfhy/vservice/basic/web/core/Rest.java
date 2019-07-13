package com.tsengfhy.vservice.basic.web.core;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@ApiModel("Response")
public class Rest<T> implements Serializable {

    private static final long serialVersionUID = 0L;

    @ApiModelProperty(value = "Status code", position = 1, example = "200")
    private Integer status;

    @ApiModelProperty(value = "Message", position = 2, example = "Successful operate")
    private String message;

    @ApiModelProperty(value = "Request path", position = 3, example = "/")
    private String path;

    @ApiModelProperty(value = "Timestamp", position = 4, example = "2008-01-01 00:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @ApiModelProperty(value = "Token", position = 5)
    private String token;

    @ApiModelProperty(value = "Errors", position = 6, allowEmptyValue = true)
    private Map<String, String> errors;

    @ApiModelProperty(value = "Data", position = 7, allowEmptyValue = true)
    private T data;
}
