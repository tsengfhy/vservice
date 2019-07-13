package com.tsengfhy.vservice.basic.exception.scheduler;

public class NoSuchJobException extends SchedulerException {

    private static final long serialVersionUID = 0L;

    public NoSuchJobException(String message) {
        super(message);
    }

    public NoSuchJobException(String message, Throwable cause) {
        super(message, cause);
    }
}
