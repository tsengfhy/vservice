package com.tsengfhy.vservice.basic.exception.security;

public class NoSuchResourceException extends SecurityPersistenceException {

    private static final long serialVersionUID = 0L;

    public NoSuchResourceException(String message) {
        super(message);
    }

    public NoSuchResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
