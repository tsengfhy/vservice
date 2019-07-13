package com.tsengfhy.vservice.basic.properties;

import lombok.Data;

@Data
public class SecurityProperties {

    private AdminProperties admin = new AdminProperties();

    private VerifyCodeProperties verifyCode = new VerifyCodeProperties();

    private AuthenticationProperties authentication = new AuthenticationProperties();

    private RememberMeProperties rememberMe = new RememberMeProperties();

    private SessionProperties session = new SessionProperties();

    private Oauth2Properties oauth2 = new Oauth2Properties();
}
