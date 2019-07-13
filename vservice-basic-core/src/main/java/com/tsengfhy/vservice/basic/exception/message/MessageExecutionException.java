package com.tsengfhy.vservice.basic.exception.message;

import org.springframework.messaging.MessageHandlingException;

public class MessageExecutionException extends MessageHandlingException {

    private static final long serialVersionUID = 0L;

    public MessageExecutionException(String message) {
        super(null, message);
    }

    public MessageExecutionException(String message, Throwable cause) {
        super(null, message, cause);
    }
}
