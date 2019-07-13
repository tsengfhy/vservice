package com.tsengfhy.vservice.basic.exception.scheduler;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

public class SchedulerException extends NestedRuntimeException {

    private static final long serialVersionUID = 0L;

    public SchedulerException(String message) {
        super(message);
    }

    public SchedulerException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
