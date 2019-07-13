package com.tsengfhy.vservice.basic.dto.security;

import com.tsengfhy.vservice.basic.dto.AbstractDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Data
@ApiModel("User")
public class UserDto extends AbstractDto {

    @ApiModelProperty(value = "Username", position = 1)
    private String username;

    @ApiModelProperty(value = "Mail", position = 2, example = "tsengfhy@gmail.com")
    private String mail;

    @ApiModelProperty(value = "Phone", position = 3, example = "186********")
    private String phone;

    @ApiModelProperty(value = "Token", position = 4)
    private String token;

    @ApiModelProperty(hidden = true)
    private transient String password;

    @ApiModelProperty(value = "Lock flag", position = 6)
    private String locked;

    @ApiModelProperty(value = "Enable flag", position = 7)
    private String enabled;

    @ApiModelProperty(value = "Related group Id", position = 8)
    private Long groupId;

    @NonNull
    @ApiModelProperty(value = "Related roles", position = 9)
    private Set<Long> roles = new HashSet<>();
}
