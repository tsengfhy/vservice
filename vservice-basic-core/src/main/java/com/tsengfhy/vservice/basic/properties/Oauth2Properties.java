package com.tsengfhy.vservice.basic.properties;

import lombok.Data;

@Data
public class Oauth2Properties {

    private AuthorizationProperties authorization = new AuthorizationProperties();

    private ResourceProperties resource = new ResourceProperties();
}
