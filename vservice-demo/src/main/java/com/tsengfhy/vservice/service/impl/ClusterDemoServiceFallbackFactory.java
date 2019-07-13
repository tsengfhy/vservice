package com.tsengfhy.vservice.service.impl;

import com.tsengfhy.vservice.basic.utils.RestUtils;
import com.tsengfhy.vservice.basic.web.core.Rest;
import feign.hystrix.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ClusterDemoServiceFallbackFactory implements FallbackFactory<ClusterDemoServiceImpl> {

    @Override
    public ClusterDemoServiceImpl create(Throwable e) {
        return new ClusterDemoServiceImpl() {
            @Override
            public Rest count(String value) {

                return RestUtils.operate(HttpStatus.INTERNAL_SERVER_ERROR.value(), "fallback");
            }
        };
    }
}
