package com.tsengfhy.vservice.basic.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "vservice")
public class Properties {

    private String artifact = "VService";

    private WebProperties web = new WebProperties();

    private SecurityProperties security = new SecurityProperties();

    private SmsProperties sms = new SmsProperties();

    private ClusterProperties cluster = new ClusterProperties();
}
