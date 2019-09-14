package com.tsengfhy.vservice.basic.security.access;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = Collections.emptyMap();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void setRequestMap(Map<RequestMatcher, Collection<ConfigAttribute>> requestMap) {
        Assert.notNull(requestMap, "RequestMap must not be null!");
        lock.writeLock().lock();
        this.requestMap = requestMap;
        lock.writeLock().unlock();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        try {
            lock.readLock().lock();
            Set<ConfigAttribute> attributes = this.requestMap.keySet()
                    .stream()
                    .filter(matcher -> matcher.matches(((FilterInvocation) object).getRequest()))
                    .map(this.requestMap::get)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            return Collections.unmodifiableSet(attributes);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {

        try {
            lock.readLock().lock();
            return Collections.unmodifiableSet(this.requestMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet()));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
