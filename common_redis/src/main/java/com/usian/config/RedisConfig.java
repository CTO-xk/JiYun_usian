package com.usian.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 配置RedisTemplate
 */
@Configuration
public class RedisConfig {

    /**
     * 创建RedisTemplate:用于执行Redis操作的方法
     */
    @Bean
    public RedisTemplate<String, Object> setRedisTemplate(RedisConnectionFactory factory) {
        //创建 RedisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        //创建 Redis 中的 key 的序列化器
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //创建 Redis 中的 value 的序列化器
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new
                Jackson2JsonRedisSerializer(Object.class);
        
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 得到的json串带有对象的全类名，指定序列化输入的类型必须是非final修饰的
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

	    //设置key:value和hashKey:hashValue的序列化的方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        //初始实例化redisTemplate
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}