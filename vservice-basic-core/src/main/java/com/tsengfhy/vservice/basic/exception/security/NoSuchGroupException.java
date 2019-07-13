package com.tsengfhy.vservice.basic.exception.security;

public class NoSuchGroupException extends SecurityPersistenceException {

    private static final long serialVersionUID = 0L;

    public NoSuchGroupException(String message) {
        super(message);
    }

    public NoSuchGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
