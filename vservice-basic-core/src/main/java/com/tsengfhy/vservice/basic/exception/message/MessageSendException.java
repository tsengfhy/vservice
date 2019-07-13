package com.tsengfhy.vservice.basic.exception.message;

import org.springframework.messaging.MessagingException;

public class MessageSendException extends MessagingException {

    private static final long serialVersionUID = 0L;

    public MessageSendException(String message) {
        super(message);
    }

    public MessageSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
