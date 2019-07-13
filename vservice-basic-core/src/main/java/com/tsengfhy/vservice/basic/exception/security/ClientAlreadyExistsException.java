package com.tsengfhy.vservice.basic.exception.security;

public class ClientAlreadyExistsException extends SecurityPersistenceException {

    private static final long serialVersionUID = 0L;

    public ClientAlreadyExistsException(String message) {
        super(message);
    }

    public ClientAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
