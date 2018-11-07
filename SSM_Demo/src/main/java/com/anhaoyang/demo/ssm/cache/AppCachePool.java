package com.anhaoyang.demo.ssm.cache;


public enum AppCachePool {
	Redis(0,"Redis"),
	EhCache(1,"Ehcache");
	
	int index;
	String cacheName;
	
	AppCachePool(int index,String cacheName){
		this.index=index;
		this.cacheName=cacheName;
	}
	
	public String toString() {
		return cacheName;
	}
}
