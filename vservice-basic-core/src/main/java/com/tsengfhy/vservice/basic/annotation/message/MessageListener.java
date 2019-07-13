package com.tsengfhy.vservice.basic.annotation.message;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(MessageListeners.class)
public @interface MessageListener {

    @AliasFor("topic")
    String value() default  "";

    @AliasFor("value")
    String topic() default "";

    String tag() default "";
}
