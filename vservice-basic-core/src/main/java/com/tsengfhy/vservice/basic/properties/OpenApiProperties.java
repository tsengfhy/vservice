package com.tsengfhy.vservice.basic.properties;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class OpenApiProperties {

    private String title = "VService API";
    private String description = "";
    private String version = "1.0";
    private String contact = "tsengfhy";
    private String url = "www.tsengfhy.com";
    private String mail = "tsengfhy@gmail.com";

    private Map<String, String> groupMap = new HashMap<>();
}
