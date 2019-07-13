package com.tsengfhy.vservice.basic.template.impl;

import com.tsengfhy.vservice.basic.exception.message.MessageSendException;
import com.tsengfhy.vservice.basic.template.MessageTemplate;
import com.tsengfhy.vservice.basic.utils.KafkaUtils;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.core.AbstractMessageSendingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Setter
public class KafkaMessageTemplate extends AbstractMessageSendingTemplate<String> implements MessageTemplate, InitializingBean {

    private KafkaTemplate kafkaTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.kafkaTemplate, "KafkaTemplate must not be null!");
    }

    @Override
    protected void doSend(String destination, Message<?> message) {

        MessageHeaderAccessor messageHeaderAccessor = MessageHeaderAccessor.getMutableAccessor(message);
        messageHeaderAccessor.setHeader(KafkaHeaders.TOPIC, destination);
        Object key = messageHeaderAccessor.getHeader(KafkaHeaders.MESSAGE_KEY);
        messageHeaderAccessor.setHeader(KafkaHeaders.MESSAGE_KEY, KafkaUtils.preSerialize(key, KafkaUtils.Serializer.KEY));
        Object value = message.getPayload();

        Optional.ofNullable(kafkaTemplate.send(MessageBuilder.createMessage(KafkaUtils.preSerialize(value, KafkaUtils.Serializer.VALUE), messageHeaderAccessor.getMessageHeaders())))
                .map(future -> {
                    try {
                        return (SendResult<String, String>) future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new MessagingException(e.getMessage(), e);
                    }
                })
                .filter(result -> result.getRecordMetadata().hasOffset() && result.getRecordMetadata().offset() > 0)
                .orElseThrow(() -> new MessageSendException(MessageSourceUtils.getMessage("Message.sendFailure", new Object[]{destination}, "Message send failed to topic: " + destination)));
    }
}
