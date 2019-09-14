package com.tsengfhy.vservice.basic.security.verify;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleVerifyCodeStrategy extends DefaultVerifyCodeStrategy {

    @Override
    public String generate(String type, String username) {
        String verifyCode = super.generate(type, username);
        log.info("Type: '{}', Username: '{}', VerifyCode: '{}'", type, username, verifyCode);

        return verifyCode;
    }
}
