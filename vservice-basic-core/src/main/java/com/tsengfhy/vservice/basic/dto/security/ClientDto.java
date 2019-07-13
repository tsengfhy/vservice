package com.tsengfhy.vservice.basic.dto.security;

import com.tsengfhy.vservice.basic.dto.AbstractDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Data
@ApiModel("Client")
public class ClientDto extends AbstractDto {

    @ApiModelProperty(value = "Secret", position = 1)
    private String secret;

    @ApiModelProperty(value = "Name", position = 2)
    private String name;

    @ApiModelProperty(value = "Description", position = 3)
    private String description;

    @ApiModelProperty(value = "Grant types", position = 4)
    private String grantTypes;

    @ApiModelProperty(value = "Scopes", position = 5)
    private String scopes;

    @ApiModelProperty(value = "Approve scopes", position = 6)
    private String approveScopes;

    @ApiModelProperty(value = "Resources", position = 7)
    private String resources;

    @ApiModelProperty(value = "Redirect uris", position = 8)
    private String redirectUris;

    @ApiModelProperty(value = "Lock flag", position = 9)
    private String locked;

    @ApiModelProperty(value = "Enable flag", position = 10)
    private String enabled;

    @NonNull
    @ApiModelProperty(value = "Related roles", position = 11)
    private Set<Long> roles = new HashSet<>();
}
