package com.tsengfhy.vservice.basic.utils;


import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public final class KafkaUtils {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    @Setter
    private static KafkaProperties kafkaProperties;

    public static Object preSerialize(Object value, Serializer serializer) {

        return Optional.ofNullable(kafkaProperties)
                .map(properties -> serializer == Serializer.KEY ? properties.getProducer().getKeySerializer() : properties.getProducer().getValueSerializer())
                .map(serializerClass -> Arrays.stream(serializerClass.getDeclaredMethods())
                        .filter(method -> "serialize".equals(method.getName()))
                        .map(method -> method.getParameterTypes()[1])
                        .filter(parameterClass -> !parameterClass.equals(Object.class))
                        .findFirst()
                        .orElse(null)
                )
                .map(parameterClass -> {
                    if (value == null || parameterClass.isInstance(value)) {
                        return value;
                    } else if (String.class.equals(parameterClass)) {
                        return DataUtils.toJSON(value, SerializerFeature.WriteClassName);
                    } else if (byte[].class.equals(parameterClass)) {
                        return DataUtils.toJSON(value, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
                    } else {
                        return value;
                    }
                })
                .orElse(value);
    }

    public static Object postDeserialize(Object value, Serializer serializer) {

        return Optional.ofNullable(kafkaProperties)
                .map(properties -> serializer == Serializer.KEY ? properties.getConsumer().getKeyDeserializer() : properties.getConsumer().getValueDeserializer())
                .map(serializerClass -> Arrays.stream(serializerClass.getDeclaredMethods())
                        .filter(method -> "deserialize".equals(method.getName()))
                        .map(Method::getReturnType)
                        .filter(returnTypeClass -> !returnTypeClass.equals(Object.class))
                        .findFirst()
                        .orElse(null)
                )
                .map(returnTypeClass -> {
                    if (value == null) {
                        return value;
                    } else if (String.class.equals(returnTypeClass)) {
                        return DataUtils.toObject((String) value, Object.class);
                    } else if (byte[].class.equals(returnTypeClass)) {
                        return DataUtils.toObject(new String((byte[]) value, DEFAULT_CHARSET), Object.class);
                    } else {
                        return value;
                    }
                })
                .orElse(value);
    }

    public enum Serializer {
        KEY,
        VALUE
    }
}
