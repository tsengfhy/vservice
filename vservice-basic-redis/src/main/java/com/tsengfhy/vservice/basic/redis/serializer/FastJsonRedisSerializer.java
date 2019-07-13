package com.tsengfhy.vservice.basic.redis.serializer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tsengfhy.vservice.basic.utils.DataUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.util.Optional;

public class FastJsonRedisSerializer implements RedisSerializer<Object> {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private final Charset charset;

    public FastJsonRedisSerializer() {
        this(DEFAULT_CHARSET);
    }

    public FastJsonRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public byte[] serialize(Object obj) throws SerializationException {
        return Optional.ofNullable(obj).map(o -> DataUtils.toJSON(o, SerializerFeature.WriteClassName).getBytes(charset)).orElseGet(() -> new byte[0]);
    }

    @Nullable
    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return Optional.ofNullable(bytes).filter(b -> b.length > 0).map(b -> DataUtils.toObject(new String(b, charset), Object.class)).orElse(null);
    }

}
