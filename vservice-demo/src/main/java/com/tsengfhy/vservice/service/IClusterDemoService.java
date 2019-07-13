package com.tsengfhy.vservice.service;

import com.tsengfhy.vservice.basic.web.core.Rest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/public/cloud")
public interface IClusterDemoService {

    @GetMapping(value = "/count/remote")
    Rest count(@RequestParam(value = "value") String value);
}
