package com.tsengfhy.vservice.basic.web.i18n;

import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import lombok.Setter;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Supplier;

@Setter
public class HeaderLocaleResolver extends AbstractLocaleContextResolver {

    public static final String LOCALE_SESSION_ATTRIBUTE_NAME = HeaderLocaleResolver.class.getName() + ".LOCALE";
    public static final String TIME_ZONE_SESSION_ATTRIBUTE_NAME = HeaderLocaleResolver.class.getName() + ".TIME_ZONE";
    public static final String LOCALE_HEADER_ATTRIBUTE_NAME = "Accept-Language";
    public static final String TIME_ZONE_HEADER_ATTRIBUTE_NAME = "Accept-TimeZone";
    private String localeSessionAttributeName = LOCALE_SESSION_ATTRIBUTE_NAME;
    private String timeZoneSessionAttributeName = TIME_ZONE_SESSION_ATTRIBUTE_NAME;
    private String localeHeaderAttributeName = LOCALE_HEADER_ATTRIBUTE_NAME;
    private String timeZoneHeaderAttributeName = TIME_ZONE_HEADER_ATTRIBUTE_NAME;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        Locale locale = (Locale) WebUtils.getSessionAttribute(request, this.localeSessionAttributeName);

        List<Supplier<Locale>> list = new ArrayList<>();
        list.add(() -> locale);
        list.add(this::getDefaultLocale);

        return Optional.ofNullable(request.getHeader(this.localeHeaderAttributeName))
                .map(localeHeader -> request.getLocale())
                .filter(headerLocale -> {
                    if (headerLocale != locale) {
                        WebUtils.setSessionAttribute(request, this.localeSessionAttributeName, headerLocale);
                    }
                    return true;
                }).orElseGet(() -> list.stream().map(Supplier::get).filter(Objects::nonNull).findFirst().orElse(null));
    }

    @Override
    public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
        throw new UnsupportedOperationException(MessageSourceUtils.getMessage("Common.methodNotSupport", new Object[]{HeaderLocaleResolver.class.getName() + ".setLocale()"}, "Method " + HeaderLocaleResolver.class.getName() + ".setLocale() not support"));
    }

    @Nullable
    protected TimeZone resolveTimeZone(HttpServletRequest request) {

        TimeZone timeZone = (TimeZone) WebUtils.getSessionAttribute(request, this.timeZoneSessionAttributeName);

        List<Supplier<TimeZone>> list = new ArrayList<>();
        list.add(() -> timeZone);
        list.add(this::getDefaultTimeZone);

        return Optional.ofNullable(request.getHeader(this.timeZoneHeaderAttributeName))
                .map(StringUtils::parseTimeZoneString)
                .filter(headerTimeZone -> {
                    if (headerTimeZone != timeZone) {
                        WebUtils.setSessionAttribute(request, this.timeZoneSessionAttributeName, headerTimeZone);
                    }
                    return true;
                }).orElseGet(() -> list.stream().map(Supplier::get).filter(Objects::nonNull).findFirst().orElse(null));
    }

    @Override
    public LocaleContext resolveLocaleContext(HttpServletRequest request) {
        return new TimeZoneAwareLocaleContext() {

            @Override
            public Locale getLocale() {
                return HeaderLocaleResolver.this.resolveLocale(request);
            }

            @Nullable
            @Override
            public TimeZone getTimeZone() {
                return HeaderLocaleResolver.this.resolveTimeZone(request);
            }
        };
    }

    @Override
    public void setLocaleContext(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable LocaleContext localeContext) {
        throw new UnsupportedOperationException(MessageSourceUtils.getMessage("Common.methodNotSupport", new Object[]{HeaderLocaleResolver.class.getName() + ".setLocaleContext()"}, "Method " + HeaderLocaleResolver.class.getName() + ".setLocaleContext() not support"));
    }
}
