package com.tsengfhy.vservice.basic.exception.mail;

import org.springframework.mail.MailException;

public class NoSuchMailException extends MailException {

    private static final long serialVersionUID = 0L;

    public NoSuchMailException(String message) {
        super(message);
    }

    public NoSuchMailException(String message, Throwable cause) {
        super(message, cause);
    }
}
