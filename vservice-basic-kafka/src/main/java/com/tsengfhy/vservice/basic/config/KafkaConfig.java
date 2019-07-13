package com.tsengfhy.vservice.basic.config;

import com.tsengfhy.vservice.basic.kafka.listener.MessageListenerAnnotationBeanPostProcessor;
import com.tsengfhy.vservice.basic.template.MessageTemplate;
import com.tsengfhy.vservice.basic.template.impl.KafkaMessageTemplate;
import com.tsengfhy.vservice.basic.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@Configuration
@ConditionalOnClass({KafkaTemplate.class})
@AutoConfigureAfter({KafkaAutoConfiguration.class})
public class KafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @PostConstruct
    public void init() {

        log.info("VService module [Kafka] is loaded");

        KafkaUtils.setKafkaProperties(kafkaProperties);
    }

    @Bean
    public MessageTemplate messageTemplate(KafkaTemplate kafkaTemplate) throws Exception {

        KafkaMessageTemplate messageTemplate = new KafkaMessageTemplate();
        messageTemplate.setKafkaTemplate(kafkaTemplate);
        messageTemplate.setDefaultDestination(kafkaProperties.getTemplate().getDefaultTopic());

        return messageTemplate;
    }

    @Bean
    public MessageListenerAnnotationBeanPostProcessor messageListenerAnnotationBeanPostProcessor() throws Exception {

        MessageListenerAnnotationBeanPostProcessor messageListenerAnnotationBeanPostProcessor = new MessageListenerAnnotationBeanPostProcessor();

        messageListenerAnnotationBeanPostProcessor.setDefaultTopic(kafkaProperties.getTemplate().getDefaultTopic());
        boolean isAckDiscarded = Optional.ofNullable(kafkaProperties.getConsumer().getEnableAutoCommit()).orElse(true)
                || (kafkaProperties.getListener().getAckMode() != ContainerProperties.AckMode.MANUAL && kafkaProperties.getListener().getAckMode() != ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        messageListenerAnnotationBeanPostProcessor.setAckDiscarded(isAckDiscarded);

        return messageListenerAnnotationBeanPostProcessor;
    }
}
