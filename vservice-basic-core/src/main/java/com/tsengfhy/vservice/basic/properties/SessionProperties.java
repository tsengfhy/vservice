package com.tsengfhy.vservice.basic.properties;

import lombok.Data;

@Data
public class SessionProperties {

    private String cookie = "JSESSIONID";

    private int maximumSessions = 1;
    private boolean maxSessionsPreventsLogin = false;
}
