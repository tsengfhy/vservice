package com.tsengfhy.vservice.basic.properties;

import lombok.Data;

@Data
public class SmsProperties {

    private String type;
    private String accessKey;
    private String accessSecret;
    private String signName;
}
