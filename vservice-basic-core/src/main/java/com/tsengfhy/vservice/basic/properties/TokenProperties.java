package com.tsengfhy.vservice.basic.properties;

import lombok.Data;

@Data
public class TokenProperties {

    private boolean enabled = false;

    private String parameter = "token";
}
