package com.tsengfhy.vservice.basic.service.impl;

import com.tsengfhy.vservice.basic.repository.SysResourceRepository;
import com.tsengfhy.vservice.basic.security.access.FilterInvocationSecurityMetadataSourceImpl;
import com.tsengfhy.vservice.basic.service.ISecurityInitService;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Setter
public class SecurityInitServiceImpl implements ISecurityInitService, InitializingBean {

    private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;

    private SysResourceRepository sysResourceRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.loadRequestMap();
    }

    @Override
    public synchronized void loadRequestMap() {

        Optional.ofNullable(filterInvocationSecurityMetadataSource)
                .filter(FilterInvocationSecurityMetadataSourceImpl.class::isInstance)
                .ifPresent(filterInvocationSecurityMetadataSource -> {
                    Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = sysResourceRepository.findResourceMapping()
                            .stream()
                            .collect(Collectors.groupingByConcurrent(
                                    item -> new AntPathRequestMatcher(
                                            String.valueOf(item[0]),
                                            Optional.ofNullable(item[1]).map(method -> String.valueOf(method).toUpperCase()).orElse(null)
                                    ),
                                    Collectors.collectingAndThen(
                                            Collectors.mapping(
                                                    item -> String.valueOf(Optional.ofNullable(item[2]).orElseGet(() -> new Random().nextInt(99999999))),
                                                    Collectors.joining(",")
                                            ),
                                            SecurityConfig::createListFromCommaDelimitedString
                                    )
                            ));
                    ((FilterInvocationSecurityMetadataSourceImpl) filterInvocationSecurityMetadataSource).setRequestMap(requestMap);
                });
    }
}
