package com.letters7.wuchen.springboot2.cache.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author zoubin02
 */
public class FastJsonRedisSerializer implements RedisSerializer<Object> {
    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if (o == null) {
            return null;
        }
        return JSON.toJSONBytes(o,
                SerializerFeature.WriteClassName,
                //
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteDateUseDateFormat,
                //空字符串输出
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteMapNullValue,
                //JSON格式化输出
                SerializerFeature.PrettyFormat);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        return JSON.parse(bytes);
    }
}
