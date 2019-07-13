package com.tsengfhy.vservice.basic.config;

import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.web.i18n.HeaderLocaleResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Configuration
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
@ConditionalOnWebApplication
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
public class WebConfig implements WebMvcConfigurer {

    @PostConstruct
    public void init() {
        log.info("VService module [Web] is loaded");
    }

    @Autowired
    private Properties properties;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(3600)
        ;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addRedirectViewController("/", properties.getWeb().getIndex());
    }

    @Override
    public Validator getValidator() {

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);

        return validator;
    }

    @Bean
    public LocaleResolver localeResolver(WebMvcProperties webMvcProperties) throws Exception {

        HeaderLocaleResolver localeResolver = new HeaderLocaleResolver();
        localeResolver.setDefaultLocale(Optional.ofNullable(webMvcProperties.getLocale()).orElseGet(() -> new Locale(SystemUtils.USER_LANGUAGE, SystemUtils.USER_COUNTRY)));
        localeResolver.setDefaultTimeZone(StringUtils.parseTimeZoneString(SystemUtils.USER_TIMEZONE));

        return localeResolver;
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() throws Exception {

        return (ErrorPageRegistry registry) ->
                registry.addErrorPages(
                        new ErrorPage(HttpStatus.BAD_REQUEST, properties.getWeb().getBadRequest()),
                        new ErrorPage(HttpStatus.UNAUTHORIZED, properties.getWeb().getUnauthorized()),
                        new ErrorPage(HttpStatus.FORBIDDEN, properties.getWeb().getForbidden()),
                        new ErrorPage(HttpStatus.NOT_FOUND, properties.getWeb().getNotFound()),
                        new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, properties.getWeb().getMethodNotAllowed()),
                        new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, properties.getWeb().getInternalServerError()),
                        new ErrorPage(HttpStatus.SERVICE_UNAVAILABLE, properties.getWeb().getServiceUnavailable()),
                        new ErrorPage(HttpStatus.GATEWAY_TIMEOUT, properties.getWeb().getGatewayTimeout())
                );
    }

    @Bean
    @ConditionalOnClass({JSONAware.class})
    public HttpMessageConverter fastJsonHttpMessageConverter() throws Exception {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.PrettyFormat
        );
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);

        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);

        return fastJsonHttpMessageConverter;
    }

    @Configuration
    @ConditionalOnClass({Docket.class})
    @EnableSwagger2
    static class SwaggerConfig implements ApplicationContextAware {

        @Autowired
        private Properties properties;

        @Setter
        private ApplicationContext applicationContext;

        private static final String DEFAULT_BASE_PACKAGE = "com.tsengfhy";

        @PostConstruct
        public void init() {
            BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();

            String factoryBeanName = applicationContext.getBeanNamesForType(this.getClass())[0];
            String beanNameSuffix = Docket.class.getSimpleName();

            properties.getWeb().getOpenApi().getGroupMap().forEach((groupName, basePackage) -> {
                BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Docket.class)
                        .setFactoryMethodOnBean("docket", factoryBeanName)
                        .addConstructorArgValue(groupName)
                        .addConstructorArgValue(basePackage)
                        .getBeanDefinition();
                beanDefinitionRegistry.registerBeanDefinition(groupName + beanNameSuffix, beanDefinition);
            });

            if (applicationContext.getBeanNamesForType(Docket.class).length == 0) {
                BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Docket.class)
                        .setFactoryMethodOnBean("docket", factoryBeanName)
                        .addConstructorArgValue(null)
                        .addConstructorArgValue(null)
                        .getBeanDefinition();
                beanDefinitionRegistry.registerBeanDefinition("docket", beanDefinition);
            }
        }

        public Docket docket(@Nullable String groupName, @Nullable String basePackage) {

            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .groupName(groupName)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(Optional.ofNullable(basePackage).orElse(DEFAULT_BASE_PACKAGE)))
                    .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                    .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                    .build();
        }

        private ApiInfo apiInfo() {

            return new ApiInfoBuilder()
                    .title(properties.getWeb().getOpenApi().getTitle())
                    .description(properties.getWeb().getOpenApi().getDescription())
                    .version(properties.getWeb().getOpenApi().getVersion())
                    .contact(new Contact(
                            properties.getWeb().getOpenApi().getContact(),
                            properties.getWeb().getOpenApi().getUrl(),
                            properties.getWeb().getOpenApi().getMail())
                    )
                    .build();
        }
    }
}
