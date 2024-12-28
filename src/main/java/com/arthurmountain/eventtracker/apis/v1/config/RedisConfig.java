package com.arthurmountain.eventtracker.apis.v1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
  @Value("${redis.host}")
  private String redisHost;

  @Value("${redis.port}")
  private int redisPort;

  @Bean
  public StringRedisTemplate stringRedisTemplate() {
    return new StringRedisTemplate(
        new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost,
            redisPort)));
  }
}