package com.tsengfhy.vservice.service;

import com.tsengfhy.vservice.dto.DemoDto;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IJpaDemoService {

    void save(DemoDto dto);

    Page<DemoDto> find(int pageNo, int pageSize);

    Page<DemoDto> find(Map<String, String> map, int pageNo, int pageSize);
}
