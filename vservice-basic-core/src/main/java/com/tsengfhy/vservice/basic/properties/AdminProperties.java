package com.tsengfhy.vservice.basic.properties;

import lombok.Data;

@Data
public class AdminProperties {

    private boolean enabled = false;

    private String username = "admin";
    private String password = "admin";

    private String role = "ADMIN";
}
