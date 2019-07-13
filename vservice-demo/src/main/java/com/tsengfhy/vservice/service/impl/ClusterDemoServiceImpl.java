package com.tsengfhy.vservice.service.impl;

import com.tsengfhy.vservice.service.IClusterDemoService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "vservice-demo", fallbackFactory = ClusterDemoServiceFallbackFactory.class)
public interface ClusterDemoServiceImpl extends IClusterDemoService {
}
