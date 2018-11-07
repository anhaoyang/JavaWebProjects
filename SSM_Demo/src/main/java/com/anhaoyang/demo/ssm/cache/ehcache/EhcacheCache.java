package com.anhaoyang.demo.ssm.cache.ehcache;

import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.SimpleValueWrapper;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import org.springframework.stereotype.Component;

import com.anhaoyang.demo.ssm.cache.AppAbstractCache;
import com.anhaoyang.demo.ssm.cache.AppCachePool;

@Component("Ehcache")
public class EhcacheCache implements AppAbstractCache,InitializingBean{
	@Autowired
	private CacheManager ehCacheCacheManager;
	private static Cache cache;
	private static CacheConfiguration cacheConfiguration;
	/**
	 * 把cache实例化一下
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		cache=ehCacheCacheManager.getCache(AppCachePool.EhCache.toString());
		cacheConfiguration=cache.getCacheConfiguration();
	}

	@Override
	public String getName() {
		return cache.getName();
	}

	@Override
	public Object getNativeCache() {
		return cache;
	}

	@Override
	public void clear() {
		cache.flush();
	}

	@Override
	public ValueWrapper getValueWrapper(Map<String, Object> keys) {
		Element element=cache.get(keys.get("key"));
		if(element==null) {
			return null;
		}
		SimpleValueWrapper simpleValueWrapper=new SimpleValueWrapper(element.getObjectValue());
		return simpleValueWrapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getT(Map<String, Object> keys, Class<T> type) {
		Element element=cache.get(keys.get("key"));
		return (T)element.getObjectValue();
	}

	@Override
	public <T> T getT(Map<String, Object> keys, Callable<T> valueLoader) {
		return null;
	}

	@Override
	public void putObject(Map<String, Object> keys, Object value) {
		int timeToIdleSeconds=new Long(cacheConfiguration.getTimeToIdleSeconds()).intValue();
		int timeToLiveSeconds=new Long(cacheConfiguration.getTimeToLiveSeconds()).intValue();
		if(keys.containsKey("liveTime")) {
			try {
				int timeToIdleSeconds_temp=Integer.parseInt(keys.get("liveTime").toString());
				timeToIdleSeconds=timeToIdleSeconds_temp;
			} catch (Exception e) {}
		}
		System.out.println(timeToIdleSeconds);
		Element element=new Element(keys.get("key"), value, timeToIdleSeconds, timeToLiveSeconds);
		cache.put(element);
	}

	@Override
	public ValueWrapper putObjectIfAbsent(Map<String, Object> keys, Object value) {
		Element element=cache.get(keys.get("key"));
		if(element==null) {
			putObject(keys,value);
		}
		SimpleValueWrapper simpleValueWrapper=new SimpleValueWrapper(value);
		return simpleValueWrapper;
	}

	@Override
	public void evict(Map<String, Object> keys) {
		cache.remove(keys.get("key"));
	}

}
