package com.letters7.wuchen.springboot2.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 这里默认的缓存主键为：类名+方法名+参数hashcode
 * 所以BaseModel需要正确的实现hashcode方法
 * @author wuchen
 */
@Component
public class SimpleKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(target.getClass().getName());
        sb.append(method.getName());
        for (Object obj : params) {
            if (obj == null) {
                obj = "null";
            }
            sb.append(obj.hashCode());
        }
        return sb.toString();
    }
}
