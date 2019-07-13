package com.tsengfhy.vservice.service.impl;

import com.tsengfhy.vservice.domain.SysDemo;
import com.tsengfhy.vservice.dto.DemoDto;
import com.tsengfhy.vservice.repository.SysDemoRepository;
import com.tsengfhy.vservice.repository.SysDemoSearchRepository;
import com.tsengfhy.vservice.service.IJpaDemoService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JpaDemoServiceImpl implements IJpaDemoService {

    @Autowired(required = false)
    private SysDemoSearchRepository sysDemoSearchRepository;

    @Autowired(required = false)
    private SysDemoRepository sysDemoRepository;

    @Override
    public void save(DemoDto dto) {

        SysDemo sysDemo = new SysDemo();
        BeanUtils.copyProperties(dto, sysDemo);

        sysDemoRepository.save(sysDemo);
        sysDemoSearchRepository.save(sysDemo);

        BeanUtils.copyProperties(sysDemo, dto);
    }

    @Override
    public Page<DemoDto> find(int pageNo, int pageSize) {

        Page<SysDemo> page = sysDemoRepository.findAll(PageRequest.of(pageNo, pageSize));
        List<DemoDto> list = page.getContent()
                .stream()
                .map(demo -> {
                    DemoDto dto = new DemoDto();
                    BeanUtils.copyProperties(demo, dto);

                    return dto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

    @Override
    public Page<DemoDto> find(Map<String, String> map, int pageNo, int pageSize) {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        map.forEach((key, value) -> {
            queryBuilder.should(QueryBuilders.fuzzyQuery(key, value));
        });

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(pageNo, pageSize))
                .build();

        Page<SysDemo> page = sysDemoSearchRepository.search(searchQuery);

        List<DemoDto> list = new ArrayList<>();
        page.getContent().forEach(demo -> {
            DemoDto dto = new DemoDto();
            BeanUtils.copyProperties(demo, dto);

            list.add(dto);
        });

        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }
}
