package com.tsengfhy.vservice.basic.annotation.message;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageListeners {

    MessageListener[] value();
}
