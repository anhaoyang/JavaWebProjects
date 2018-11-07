package com.anhaoyang.demo.ssm.cache.ehcache;

import net.sf.ehcache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EhcacheCacheConfig {
	
	@Bean("ehCacheCacheManager")
    public CacheManager ehCacheCacheManager(EhCacheCacheManagerFactory ehCacheCacheManagerFactory){
		return ehCacheCacheManagerFactory.getCacheManager();
    }
	
}
