package com.tsengfhy.vservice.basic.kafka.listener;

import com.tsengfhy.vservice.basic.exception.message.MessageExecutionException;
import com.tsengfhy.vservice.basic.utils.KafkaUtils;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class MessageListenerAdapter implements MessageListener<Object, Object> {

    private final Object bean;
    private final Method method;

    public MessageListenerAdapter(Object bean, Method method) {
        Assert.notNull(bean, "Listener bean must not be null!");
        Assert.notNull(method, "Listener method must not be null!");
        Assert.isInstanceOf(method.getDeclaringClass(), bean, "Listener bean and method must suit!");

        this.bean = bean;
        this.method = BridgeMethodResolver.findBridgedMethod(method);
    }

    @Override
    public Object onMessage(ConsumerRecord<Object, Object> consumerRecord) throws MessagingException {

        Object data = Optional.ofNullable(consumerRecord.value()).map(value -> KafkaUtils.postDeserialize(value, KafkaUtils.Serializer.VALUE)).orElse(null);

        try {
            return MethodUtils.invokeMethod(this.bean, true, this.method.getName(), data);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new MessageExecutionException(MessageSourceUtils.getMessage("Message.executeFailure", new Object[]{this.bean.getClass().getName() + "." + this.method.getName() + "()"}, "Method " + this.bean.getClass().getName() + "." + this.method.getName() + "() invoked failed"), e);
        }
    }

    @Override
    public Object onMessage(ConsumerRecord<Object, Object> consumerRecord, Acknowledgment acknowledgment) throws MessagingException {
        Object returnValue = this.onMessage(consumerRecord);
        acknowledgment.acknowledge();
        return returnValue;
    }
}
