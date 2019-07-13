package com.tsengfhy.vservice.basic.dto.security;

import com.tsengfhy.vservice.basic.dto.AbstractDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Data
@ApiModel("Group")
public class GroupDto extends AbstractDto {

    @ApiModelProperty(value = "Type", position = 1)
    private String type;

    @ApiModelProperty(value = "Name", position = 2)
    private String name;

    @ApiModelProperty(value = "Description", position = 3)
    private String description;

    @ApiModelProperty(value = "Index", position = 4)
    private Integer series;

    @ApiModelProperty(value = "Parent Id", position = 5)
    private Long pid;

    @NonNull
    @ApiModelProperty(value = "Related users", position = 6)
    private Set<Long> users = new HashSet<>();
}
