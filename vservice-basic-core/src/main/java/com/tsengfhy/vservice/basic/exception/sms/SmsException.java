package com.tsengfhy.vservice.basic.exception.sms;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

public class SmsException extends NestedRuntimeException {

    private static final long serialVersionUID = 0L;

    public SmsException(String message) {
        super(message);
    }

    public SmsException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
