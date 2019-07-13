package com.tsengfhy.vservice.basic.properties;

import lombok.Data;

@Data
public class RememberMeProperties {

    private boolean enabled = false;

    private String cookie = "TOKEN";
    private String parameter = "remember-me";

    private int tokenValiditySeconds = 7 * 24 * 60 * 60;
}
