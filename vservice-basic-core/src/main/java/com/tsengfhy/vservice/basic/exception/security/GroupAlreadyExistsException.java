package com.tsengfhy.vservice.basic.exception.security;

public class GroupAlreadyExistsException extends SecurityPersistenceException {

    private static final long serialVersionUID = 0L;

    public GroupAlreadyExistsException(String message) {
        super(message);
    }

    public GroupAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
