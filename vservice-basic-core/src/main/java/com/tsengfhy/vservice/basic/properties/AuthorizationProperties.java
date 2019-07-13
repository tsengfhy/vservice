package com.tsengfhy.vservice.basic.properties;

import lombok.Data;

@Data
public class AuthorizationProperties {

    private boolean enabled = false;

    private String clientIdParameter = "client_id";
    private String clientSecretParameter = "client_secret";

    private int accessTokenValiditySeconds = 3600;
    private int refreshTokenValiditySeconds = 7200;
}
