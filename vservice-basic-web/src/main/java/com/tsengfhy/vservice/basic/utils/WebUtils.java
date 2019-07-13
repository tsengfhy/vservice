package com.tsengfhy.vservice.basic.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@UtilityClass
public final class WebUtils {

    public static Optional<HttpServletRequest> getRequest() {

        return Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()))
                .map(ServletRequestAttributes::getRequest);
    }

    public static Optional<HttpServletResponse> getResponse() {

        return Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()))
                .map(ServletRequestAttributes::getResponse);
    }

    public static boolean isRest() {

        return getRequest()
                .map(request -> {
                    try {
                        return new HeaderContentNegotiationStrategy().resolveMediaTypes(new ServletWebRequest(request));
                    } catch (HttpMediaTypeNotAcceptableException e) {
                        return null;
                    }
                })
                .map(mediaTypes -> mediaTypes.stream().anyMatch(mediaType -> mediaType.getQualityValue() == 1 && MediaType.APPLICATION_JSON_UTF8.isCompatibleWith(mediaType)))
                .orElse(false);
    }

    public static boolean isAjax() {

        return getRequest()
                .map(request -> request.getHeader("X-Requested-With"))
                .map("XMLHttpRequest"::equals)
                .orElse(false);
    }

    public static boolean isCors() {

        return getRequest()
                .map(request -> {
                    String origin = request.getHeader("Origin");
                    String current = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                    return StringUtils.isNotBlank(origin) && !origin.contains(current);
                })
                .orElse(false);
    }

    public static boolean isPreFlight() {

        return isCors()
                && getRequest().map(request -> HttpMethod.OPTIONS.matches(request.getMethod())).orElse(false)
                && getRequest().map(request -> request.getHeader("Access-Control-Request-Method") != null).orElse(false);
    }
}
