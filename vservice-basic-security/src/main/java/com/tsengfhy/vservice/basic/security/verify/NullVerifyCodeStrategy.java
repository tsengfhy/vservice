package com.tsengfhy.vservice.basic.security.verify;

import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.springframework.security.authentication.CredentialsExpiredException;

public class NullVerifyCodeStrategy implements VerifyCodeStrategy {

    @Override
    public String generate(String username, String type) {
        throw new UnsupportedOperationException(MessageSourceUtils.getMessage("Common.methodNotSupport", new Object[]{NullVerifyCodeStrategy.class.getName() + ".generate()"}, "Method " + NullVerifyCodeStrategy.class.getName() + ".generate() not support"));
    }

    @Override
    public void clear(String username, String type) {
        throw new UnsupportedOperationException(MessageSourceUtils.getMessage("Common.methodNotSupport", new Object[]{NullVerifyCodeStrategy.class.getName() + ".clear()"}, "Method " + NullVerifyCodeStrategy.class.getName() + ".clear() not support"));
    }

    @Override
    public boolean matches(String username, String type, String verifyCode) throws CredentialsExpiredException {
        return true;
    }
}
