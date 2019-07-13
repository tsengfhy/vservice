package com.tsengfhy.vservice.basic.core;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.util.regex.Pattern;

public class VServiceRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String BASE_PACKAGE = "com.tsengfhy";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, true);
        scanner.addExcludeFilter(new RegexPatternTypeFilter(Pattern.compile(metadata.getClassName())));
        scanner.scan(BASE_PACKAGE);
    }
}
