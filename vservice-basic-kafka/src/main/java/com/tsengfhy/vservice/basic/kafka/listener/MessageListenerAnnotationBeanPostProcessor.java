package com.tsengfhy.vservice.basic.kafka.listener;

import com.tsengfhy.vservice.basic.annotation.message.MessageListener;
import com.tsengfhy.vservice.basic.annotation.message.MessageListeners;
import com.tsengfhy.vservice.basic.exception.message.MessagePreparationException;
import com.tsengfhy.vservice.basic.utils.KafkaUtils;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MessageListenerAnnotationBeanPostProcessor extends KafkaListenerAnnotationBeanPostProcessor implements InitializingBean {

    @Setter
    private String defaultTopic;

    private Method defaultProxyMethod;

    @Setter
    private boolean isAckDiscarded;

    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap(64));

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.hasText(this.defaultTopic, "DefaultTopic must be set!");

        if (this.isAckDiscarded) {
            this.defaultProxyMethod = MessageListenerAdapter.class.getMethod("onMessage", ConsumerRecord.class);
        } else {
            this.defaultProxyMethod = MessageListenerAdapter.class.getMethod("onMessage", ConsumerRecord.class, Acknowledgment.class);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = AopUtils.getTargetClass(bean);

        if (!this.nonAnnotatedClasses.contains(clazz)) {
            Map<Method, Set<MessageListener>> annotatedMethods = MethodIntrospector.selectMethods(clazz, (Method method) -> Optional.of(MessageListenerAnnotationBeanPostProcessor.this.findListenerAnnotations(method)).filter(messageListeners -> !messageListeners.isEmpty()).orElse(null));

            if (annotatedMethods.isEmpty()) {
                this.nonAnnotatedClasses.add(clazz);

                log.trace("No @MessageListener annotations found on bean type: {}", clazz);
            } else {
                annotatedMethods.keySet().forEach(method -> {

                    Object proxyBean;
                    Method proxyMethod;

                    Optional<SendTo> optional = Optional.ofNullable(method.getAnnotation(SendTo.class));
                    if (optional.isPresent()) {
                        proxyBean = new MessageListenerAdapter(bean, method) {

                            @Override
                            @SendTo
                            public Object onMessage(ConsumerRecord<Object, Object> consumerRecord) throws MessagingException {
                                return KafkaUtils.preSerialize(super.onMessage(consumerRecord), KafkaUtils.Serializer.VALUE);
                            }

                            @Override
                            @SendTo
                            public Object onMessage(ConsumerRecord<Object, Object> consumerRecord, Acknowledgment acknowledgment) throws MessagingException {
                                return KafkaUtils.preSerialize(super.onMessage(consumerRecord, acknowledgment), KafkaUtils.Serializer.VALUE);
                            }
                        };

                        try {
                            if (this.isAckDiscarded) {
                                proxyMethod = proxyBean.getClass().getMethod("onMessage", ConsumerRecord.class);
                            } else {
                                proxyMethod = proxyBean.getClass().getMethod("onMessage", ConsumerRecord.class, Acknowledgment.class);
                            }

                            InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxyMethod.getAnnotation(SendTo.class));
                            Map<String, Object> memberValues = (Map<String, Object>) FieldUtils.readDeclaredField(invocationHandler, "memberValues", true);
                            memberValues.put("value", optional.map(SendTo::value).orElse(null));
                        } catch (Exception e) {
                            throw new MessagePreparationException(MessageSourceUtils.getMessage("Message.initializeFailure", "Message listener initialize failed"), e);
                        }

                    } else {
                        proxyBean = new MessageListenerAdapter(bean, method);
                        proxyMethod = this.defaultProxyMethod;
                    }

                    annotatedMethods.get(method).forEach(messageListener -> this.processKafkaListener(this.transform(messageListener), proxyMethod, proxyBean, beanName));
                });

                log.debug("{} @MessageListener methods processed on bean: '{}'", annotatedMethods.size(), beanName);
            }
        }

        return bean;
    }

    private Set<MessageListener> findListenerAnnotations(Method method) {

        Set<MessageListener> listeners = new HashSet<>();
        Optional.ofNullable(AnnotationUtils.findAnnotation(method, MessageListener.class)).ifPresent(listeners::add);
        Optional.ofNullable(AnnotationUtils.findAnnotation(method, MessageListeners.class)).map(MessageListeners::value).map(Arrays::asList).ifPresent(listeners::addAll);

        return listeners;
    }

    private KafkaListener transform(MessageListener messageListener) {

        String id = generateId();

        return new KafkaListener() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return KafkaListener.class;
            }

            @Override
            public String id() {
                return id;
            }

            @Override
            public String containerFactory() {
                return "";
            }

            @Override
            public String[] topics() {
                return new String[]{(StringUtils.isNotBlank(messageListener.topic()) ? messageListener.topic() : MessageListenerAnnotationBeanPostProcessor.this.defaultTopic)};
            }

            @Override
            public String topicPattern() {
                return "";
            }

            @Override
            public TopicPartition[] topicPartitions() {
                return new TopicPartition[]{};
            }

            @Override
            public String containerGroup() {
                return "";
            }

            @Override
            public String errorHandler() {
                return "";
            }

            @Override
            public String groupId() {
                return messageListener.tag();
            }

            @Override
            public boolean idIsGroup() {
                return false;
            }

            @Override
            public String clientIdPrefix() {
                return "";
            }

            @Override
            public String beanRef() {
                return "__listener";
            }

            @Override
            public String concurrency() {
                return "";
            }

            @Override
            public String autoStartup() {
                return "";
            }

            @Override
            public String[] properties() {
                return new String[]{};
            }
        };
    }

    private String generateId() {
        return "org.springframework.kafka.listener.MessageListenerContainer#" + this.counter.getAndIncrement();
    }
}
