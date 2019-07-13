package com.tsengfhy.vservice.basic.exception.message;

import org.springframework.messaging.MessagingException;

public class MessagePreparationException extends MessagingException {

    private static final long serialVersionUID = 0L;

    public MessagePreparationException(String message) {
        super(message);
    }

    public MessagePreparationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessagePreparationException(Throwable cause) {
        super("Could not prepare message", cause);
    }
}
