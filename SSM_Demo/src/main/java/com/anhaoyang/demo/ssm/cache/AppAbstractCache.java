package com.anhaoyang.demo.ssm.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;


/**
 * 在此抽象接口中对缓存的操作进行预处理
 */
public abstract interface AppAbstractCache extends Cache {
	Logger loger=LoggerFactory.getLogger(AppAbstractCache.class);
	
	/**
	 * 执行 Cache 的 ValueWrapper get(Object key)
	 */
	public abstract ValueWrapper getValueWrapper(Map<String,Object> keys);
	@Override
	public default ValueWrapper get(Object key) {
		Map<String,Object> keys=parseKey(key);
		
		loger.info("在获取缓存之前执行");
		
		return this.getValueWrapper(keys);
	}
	
	/**
	 * 执行 Cache 的 <T> T get(Object key, Class<T> type)
	 */
	public abstract <T> T getT(Map<String,Object> keys, Class<T> type);
	@Override
	public default <T> T get(Object key, Class<T> type) {
		Map<String,Object> keys=parseKey(key);
		return getT(keys,type);
	}
	
	/**
	 * 执行 Cache 的  <T> T get(Object key, Callable<T> valueLoader)
	 */
	public abstract <T> T getT(Map<String,Object> keys, Callable<T> valueLoader);
	@Override
	public default <T> T get(Object key, Callable<T> valueLoader) {
		Map<String,Object> keys=parseKey(key);
		return getT(keys,valueLoader);
	}

	/**
	 * 执行 Cache 的 void put(Object key, Object value)
	 */
	public abstract void putObject(Map<String,Object> keys, Object value);
	@Override
	public default void put(Object key, Object value) {
		Map<String,Object> keys=parseKey(key);
		putObject(keys,value);
	}

	/**
	 * 执行 Cache 的 ValueWrapper putIfAbsent(Object key, Object value)
	 */
	public abstract ValueWrapper putObjectIfAbsent(Map<String,Object> keys, Object value);
	@Override
	public default ValueWrapper putIfAbsent(Object key, Object value) {
		Map<String,Object> keys=parseKey(key);
		return putObjectIfAbsent(keys,value);
	}

	/**
	 * 执行 Cache 的  void evict(Object key)
	 */
	public abstract void evict(Map<String,Object> keys);
	@Override
	public default void evict(Object key) {
		Map<String,Object> keys=parseKey(key);
		evict(keys);
	}

	/**
	 * 把key中的附带参数与key分离开来
	 * @param key
	 * @return
	 */
	public default Map<String,Object> parseKey(Object key){
    	//提取key的附带参数
        String keyStr=key.toString();
        Map<String,Object> attrs=new HashMap<String,Object>();
        attrs.put("key", key);
		if(keyStr.contains("?")) {
			key=keyStr.substring(0,keyStr.indexOf("?"));
			attrs.put("key", key);
			String[] attrStrs=keyStr.substring(keyStr.indexOf("?")+1).split("&");
			for(String attr:attrStrs) {
				if(attr.contains("=")) {
					String attrKey=attr.split("=")[0];
					Object attrObject=attr.split("=")[1];
					attrs.put(attrKey, attrObject);
				}
			}
		}
		return attrs;
    }
}
