package com.tsengfhy.vservice.basic.security.verify;

import org.springframework.security.authentication.CredentialsExpiredException;

public interface VerifyCodeStrategy {

    String generate(String type, String username);

    void clear(String type, String username);

    boolean matches(String type, String username, String verifyCode) throws CredentialsExpiredException;
}
