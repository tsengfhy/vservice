package com.tsengfhy.vservice.basic.exception.security;

public class NoSuchClientException extends SecurityPersistenceException {

    private static final long serialVersionUID = 0L;

    public NoSuchClientException(String message) {
        super(message);
    }

    public NoSuchClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
