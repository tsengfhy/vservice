package com.tsengfhy.vservice.basic.template;


import com.tsengfhy.vservice.basic.dto.file.FileDto;
import com.tsengfhy.vservice.basic.exception.file.FileException;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface FileTemplate {

    int locate(@NotNull Long id);

    void save(FileDto dto) throws FileException;

    void update(FileDto dto) throws FileException;

    FileDto findById(@NotNull Long id) throws FileException;

    FileDto findById(@NotNull Long id, boolean isThumb) throws FileException;

    Page<FileDto> find(FileDto dto, int pageNo, int pageSize, String sort);

    void delete(@NotNull Long id) throws FileException;
}
