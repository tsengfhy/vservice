package com.tsengfhy.vservice.cluster.server.gateway;

import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;

public class CascadeServiceRouteMapper extends PatternServiceRouteMapper {

    public CascadeServiceRouteMapper() {
        super("(?<name>^.+)/(?<version>v\\d.*$)", "${version}/${name}");
    }

    @Override
    public String apply(String serviceId) {
        String route = serviceId.replaceAll("-|_", "/");
        return super.apply(route);
    }
}
