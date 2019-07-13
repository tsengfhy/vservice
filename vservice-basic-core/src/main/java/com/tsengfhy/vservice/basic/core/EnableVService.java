package com.tsengfhy.vservice.basic.core;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({VServiceRegistrar.class})
public @interface EnableVService {
}
