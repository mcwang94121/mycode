package com.example.demo.config;

import com.example.demo.entity.DemoDO;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomCacheKeyGenerator
 *
 * @author mc
 * @date 2023年06月09日
 */
@Component
public class CustomCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {

        if (params.length > 0 && params[0] instanceof List) {
            List<Object> keys = new ArrayList<>();
            List<?> list = (List<?>) params[0];
            for (Object item : list) {
                if (item instanceof DemoDO) {
                    // 获取列表中的某个字段值作为缓存key的一部分
                    Long keyPart = ((DemoDO) item).getId();
                    keys.add(keyPart);
                }
            }
            return keys.toArray();
        }
        // 默认的缓存key生成逻辑
        return "1";
    }
}
