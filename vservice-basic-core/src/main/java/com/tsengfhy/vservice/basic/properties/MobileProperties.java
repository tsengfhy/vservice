package com.tsengfhy.vservice.basic.properties;

import lombok.Data;

@Data
public class MobileProperties {

    private boolean enabled = false;

    private String mobileParameter = "mobile";
    private String codeParameter = "code";
}
