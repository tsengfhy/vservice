package com.tsengfhy.vservice.controller;

import com.tsengfhy.vservice.basic.web.core.Rest;
import com.tsengfhy.vservice.service.IClusterDemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/cluster")
@Api(tags = {"Cluster Demo"}, description = "A demo for Cluster RPC")
public class ClusterDemoCtrl {

    @Autowired(required = false)
    private IClusterDemoService clusterDemoService;

    @PostMapping(value = "/count/{type}")
    @ApiOperation(value = "Remote count", notes = "Call a remote service to count and show result")
    public Rest<Integer> count(
            @ApiParam(value = "Count type") @PathVariable String type
    ) {
        return clusterDemoService.count(type);
    }
}
