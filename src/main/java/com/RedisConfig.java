package com;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goods.model.GoodsVO;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		// 设置key序列化器为StringRedisSerializer
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);

		// 设置value序列化器为Jackson2JsonRedisSerializer
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

	    template.setValueSerializer(jackson2JsonRedisSerializer);
	    template.afterPropertiesSet();
		return template;
	}
	
	@Bean
	public RedisTemplate<String, byte[]> byteArrayRedisTemplate(RedisConnectionFactory connectionFactory) {
	    RedisTemplate<String, byte[]> template = new RedisTemplate<>();
	    template.setConnectionFactory(connectionFactory);
	    template.setKeySerializer(new StringRedisSerializer());
	    template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // JSON 处理 byte[]
	    return template;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate1(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		// 设置key序列化器为StringRedisSerializer
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);

		// 设置value序列化器为Jackson2JsonRedisSerializer
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

	    template.setValueSerializer(jackson2JsonRedisSerializer);
	    template.afterPropertiesSet();
		return template;
	}
}