package com.cong.wego.utils;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * Redis 实用程序
 *
 * @author liuhuaicong
 * @date 2023/10/28
 */
@Slf4j
@Component
public class RedisUtils {

    public RedisTemplate redisTemplate;

    private static StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void init() {
        stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
    }

    private static final String LUA_INCR_EXPIRE =
            "local key,ttl=KEYS[1],ARGV[1] \n" +
                    " \n" +
                    "if redis.call('EXISTS',key)==0 then   \n" +
                    "  redis.call('SETEX',key,ttl,1) \n" +
                    "  return 1 \n" +
                    "else \n" +
                    "  return tonumber(redis.call('INCR',key)) \n" +
                    "end ";

    public static Long inc(String key, int time, TimeUnit unit) {
        RedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_INCR_EXPIRE, Long.class);
        return stringRedisTemplate.execute(redisScript, Collections.singletonList(key), String.valueOf(unit.toSeconds(time)));
    }

    public static Boolean zIsMember(String key, Object value) {
        return Objects.nonNull(stringRedisTemplate.opsForZSet().score(key, value.toString()));
    }
    public static void zRemove(String key, Object value) {
        zRemove(key, value.toString());
    }

    public static void zRemove(String key, String value) {
        stringRedisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     *
     * @param key   钥匙
     * @param value 价值
     * @param score 得分
     */
    public static void zAdd(String key, String value, double score) {
        stringRedisTemplate.opsForZSet().add(key, value, score);
    }

    public static void zAdd(String key, Object value, double score) {
        zAdd(key, value.toString(), score);
    }
}
