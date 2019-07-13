package com.tsengfhy.vservice.controller;

import com.tsengfhy.vservice.basic.annotation.message.MessageListener;
import com.tsengfhy.vservice.basic.template.MessageTemplate;
import com.tsengfhy.vservice.basic.utils.RestUtils;
import com.tsengfhy.vservice.basic.web.core.Rest;
import com.tsengfhy.vservice.dto.DemoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/public/message")
@Api(tags = {"MessageQueue Demo"}, description = "A demo of common api for messageQueue")
public class MessageDemoCtrl {

    @Autowired(required = false)
    private MessageTemplate messageTemplate;

    @PostMapping(value = "/{message}")
    @ApiOperation(value = "Send message", notes = "Send a message to server and then u can see result in console")
    public Rest<String> send(
            @ApiParam(value = "Message Content") @PathVariable String message
    ) {

        DemoDto dto = new DemoDto();
        dto.setTitle("test");
        dto.setContent(message);

        messageTemplate.convertAndSend("test", dto);

        return RestUtils.operate(HttpStatus.OK.value(), "Successful operate", message);
    }

    @MessageListener(topic = "test", tag = "tag1")
    @SendTo("test1")
    public DemoDto listen(DemoDto dto) {
        log.info("------------------ tag1:message = " + dto);
        return dto;
    }

    @MessageListener(topic = "test", tag = "tag2")
    public void listen2(DemoDto dto) {
        log.info("------------------ tag2:message = " + dto);
    }

    @MessageListener(topic = "test1")
    public void listen3(DemoDto dto) {
        log.info("------------------ tag3:message = " + dto);
    }
}
