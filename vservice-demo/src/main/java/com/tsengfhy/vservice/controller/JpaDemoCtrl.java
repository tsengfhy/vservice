package com.tsengfhy.vservice.controller;

import com.tsengfhy.vservice.basic.utils.RestUtils;
import com.tsengfhy.vservice.basic.web.core.Rest;
import com.tsengfhy.vservice.dto.DemoDto;
import com.tsengfhy.vservice.form.DemoVO;
import com.tsengfhy.vservice.service.IJpaDemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/public/jpa")
@Api(tags = {"JPA Demo"}, description = "A demo for JPA")
public class JpaDemoCtrl {

    @Autowired
    private IJpaDemoService jpaDemoService;

    @PostMapping(value = "")
    @ApiOperation(value = "Save info", notes = "Save info into database and elasticsearch")
    public Rest<DemoDto> add(
            @RequestBody @Valid DemoVO form
    ) {

        DemoDto dto = new DemoDto();
        BeanUtils.copyProperties(form, dto);

        jpaDemoService.save(dto);

        return RestUtils.save(true, "", dto);
    }

    @GetMapping(value = "/db")
    @ApiOperation(value = "Get info", notes = "Get info from database")
    public Rest<Page<DemoDto>> db() {

        return RestUtils.select(jpaDemoService.find(0, 10));
    }

    @GetMapping(value = "/search")
    @ApiOperation(value = "Get info", notes = "Get info from elasticsearch")
    public Rest<Page<DemoDto>> search() {

        Map<String, String> map = new HashMap<>();
        map.put("content", "few");

        return RestUtils.select(jpaDemoService.find(map, 0, 10));
    }
}
