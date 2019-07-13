package com.tsengfhy.vservice.basic.exception.sms;

public class SmsPreparationException extends SmsException {

    private static final long serialVersionUID = 0L;

    public SmsPreparationException(String message) {
        super(message);
    }

    public SmsPreparationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmsPreparationException(Throwable cause) {
        super("Could not prepare sms", cause);
    }
}
