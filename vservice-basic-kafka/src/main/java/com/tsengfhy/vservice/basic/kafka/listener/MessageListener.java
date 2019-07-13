package com.tsengfhy.vservice.basic.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.MessagingException;

public interface MessageListener<K, V> {

    Object onMessage(ConsumerRecord<K, V> consumerRecord) throws MessagingException;

    Object onMessage(ConsumerRecord<K, V> consumerRecord, Acknowledgment acknowledgment) throws MessagingException;
}
