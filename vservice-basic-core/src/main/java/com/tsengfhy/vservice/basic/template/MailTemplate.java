package com.tsengfhy.vservice.basic.template;

import com.tsengfhy.vservice.basic.dto.mail.MailMessageDto;
import org.springframework.mail.MailException;
import org.springframework.validation.annotation.Validated;

import javax.mail.search.SearchTerm;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface MailTemplate {

    void send(MailMessageDto... dtos) throws MailException;

    MailMessageDto findByUid(@NotNull String username, @NotNull String uid) throws MailException;

    List<MailMessageDto> find(@NotNull String username) throws MailException;

    List<MailMessageDto> find(@NotNull String username, SearchTerm searchTerm, String... exceptUids) throws MailException;

    void delete(@NotNull String username, String... uids) throws MailException;
}
