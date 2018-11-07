package com.anhaoyang.demo.ssm.cache;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppCacheManager extends AbstractCacheManager {
	@Autowired
	private ApplicationContext applicationContext;//启动类set入，调用下面set方法

    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }
	
    //在这里把缓存整合到一块
	@Override
	protected Collection<? extends Cache> loadCaches() {
		Collection<Cache> caches=new ArrayList<Cache>();
		caches.add(getCacheFromApplicationContext(AppCachePool.Redis.toString()));
		caches.add(getCacheFromApplicationContext(AppCachePool.EhCache.toString()));
		return caches;
	}
	
	private Cache getCacheFromApplicationContext(String beanName) {
		return (Cache) applicationContext.getBean(beanName);
	}
}
