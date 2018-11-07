package com.anhaoyang.demo.ssm.cache;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
@Configuration
public class AppCacheConfig {
	
	@Primary
    @Bean("cacheManager")
	public CacheManager cacheManager() {
		AppCacheManager cacheManager = new AppCacheManager();
		return cacheManager;
	}
}