package com.tsengfhy.vservice.basic.dto.mail;

import com.tsengfhy.vservice.basic.dto.AbstractDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("MailAccount")
public class MailAccountDto extends AbstractDto {

    @ApiModelProperty(value = "Username", position = 1)
    private String username;

    @ApiModelProperty(hidden = true)
    private transient String password;
}
