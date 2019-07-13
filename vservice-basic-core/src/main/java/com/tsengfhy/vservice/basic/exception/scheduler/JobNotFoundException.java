package com.tsengfhy.vservice.basic.exception.scheduler;

public class JobNotFoundException extends SchedulerException {

    private static final long serialVersionUID = 0L;

    public JobNotFoundException(String message) {
        super(message);
    }

    public JobNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
