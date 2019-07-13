package com.tsengfhy.vservice.controller;

import com.tsengfhy.vservice.basic.dto.mail.MailMessageDto;
import com.tsengfhy.vservice.basic.template.MailTemplate;
import com.tsengfhy.vservice.basic.utils.RestUtils;
import com.tsengfhy.vservice.basic.web.core.Rest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/mail")
@Api(tags = {"Mail Demo"}, description = "A demo of common api for mail")
public class MailDemoCtrl {

    @Autowired(required = false)
    private MailTemplate mailTemplate;

    @GetMapping(value = "/receive/{username}")
    @ApiOperation(value = "Receive mail", notes = "Receive mail")
    public Rest<List<MailMessageDto>> receive(
            @ApiParam(value = "Username") @PathVariable String username
    ) {
        return RestUtils.select(mailTemplate.find(username));
    }

    @PostMapping(value = "/send/{username}")
    @ApiOperation(value = "Send mail", notes = "Send mail")
    public Rest<MailMessageDto> send(
            @ApiParam(value = "Username") @PathVariable String username
    ) {

        MailMessageDto dto = new MailMessageDto();
        dto.setFrom(username);
        dto.setText(username);
        dto.setSubject("Test Mail");
        dto.setText("This is a test mail!");

        return RestUtils.save(true, "", dto);
    }
}
