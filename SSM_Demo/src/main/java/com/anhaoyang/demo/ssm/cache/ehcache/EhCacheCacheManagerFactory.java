package com.anhaoyang.demo.ssm.cache.ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.anhaoyang.demo.ssm.cache.AppCachePool;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.Configuration;

@Component("ehCacheCacheManagerFactory")
@ConfigurationProperties(prefix="spring.cache.ehcache")
public class EhCacheCacheManagerFactory {
	private Logger loger=LoggerFactory.getLogger(getClass());
	
	/**
	 * 配置文件位置
	 */
	private String config;
	/**
	 * 
	 * @param config
	 */
	public void setConfig(String config) {
		this.config = config;
	}
	
	public CacheManager getCacheManager() {
		CacheManager cacheManager=null;
		if(config!=null) {
			cacheManager = createCacheManagerFrom_Ehcache_xml();
			loger.info("由配置文件"+config+"创建了Ehcache的缓存 cacheNames={"+arrayToString(cacheManager.getCacheNames())+"}");
			return cacheManager;
		}
		cacheManager = createCacheManagerFrom_Factory();
		loger.info("由应用配置文件创建了Ehcache的缓存 cacheNames={"+arrayToString(cacheManager.getCacheNames())+"}");
		return cacheManager;
	}
	
	/**
	 * 由 ehcache.xml 创建一个ehcache管理器
	 * <br/><br/>
	 * 注意：由此方法创建则 应用配置文件中的 spring.cache.ehcache.config 是必要的配置
	 * @return net.sf.ehcache.CacheManager
	 */
	public CacheManager createCacheManagerFrom_Ehcache_xml(){
		EhCacheManagerFactoryBean bean = new EhCacheManagerFactoryBean();
		bean.setConfigLocation(new ClassPathResource(config));
		bean.setShared(true);
		bean.afterPropertiesSet();
		return bean.getObject();
	}
	
	/**
	 * 缓存级别配置：指定在硬盘上存储对象的路径：<br/>
	 * <br/>可配置为：<br/>
	 * user.home  		->  C:\Users\用户名  <br/>
	 * user.dir	  		->  E:\Eclipses\workspace\SSM_Demo    (当前项目的磁盘位置)  <br/>
	 * java.io.tmpdir	->  C:\Users\用户名\AppData\Local\Temp\  <br/>
	 * E:\temp           （自定义的位置）
	 * <br/><br/>
	 * 磁盘存储:将缓存中暂时不使用的对象,转移到硬盘,类似于Windows系统的虚拟内存。
	 */
	private String diskStorePath="java.io.tmpdir";

	/**
	 * 缓存级别配置：指定当前缓存能够使用的硬盘的最大字节数，其值可以是数字加单位，单位可以是K、M或者G，不区分大小写，如：30G。当在CacheManager级别指定了该属性后，Cache级别也可以用百分比来表示，如：60%，表示最多使用CacheManager级别指定硬盘容量的60%。该属性也可以在运行期指定。当指定了该属性后会隐式的使当前Cache的overflowToDisk为true。
	 */
	private String maxBytesLocalDisk="20G";
	
	/**
	 * 缓存级别配置：指定当前缓存能够使用的堆内存的最大字节数，其值的设置规则跟maxBytesLocalDisk是一样的。 
	 */
	private String maxBytesLocalHeap="30M";
	
	/**
	 * Cache持久性策略：<br/>
	 * LOCALTEMPSWAP  ->  磁盘持久化的标准开放源代码（非容错） (默认) <br/>
	 * LOCALRESTARTABLE  ->  企业容错持久化  <br/>
	 * NONE  ->  本地可重新启动  <br/>
	 * DISTRIBUTED  ->  TraceCoTa集群持久化（需要TraceCoTa集群缓存）  <br/>
	 */
	private String persistenceStrategy="LOCALTEMPSWAP"; 
	
	/**
	 * Cache级别配置：指定[缓存]中允许存放元素的最大数量。这个属性也可以在运行期动态修改。但是这个属性只对Terracotta分布式缓存有用。
	 */
	private long maxEntriesInCache=0;
	
	/**
	 * Cache级别配置：指定允许在[硬盘]上存放元素的最大数量，0表示不限制。这个属性我们也可以在运行期通过CacheConfiguration来更改。 
	 */
	private long maxEntriesLocalDisk=0;
	
	/**
	 * Cache级别配置：指定允许在[内存]中存放元素的最大数量，0表示不限制。这个属性也可以在运行期动态修改。 
	 */
	private long maxEntriesLocalHeap=0;
	
	/**
	 * Cache级别配置：当指定该属性为true时，我们在从Cache中读数据时取到的是Cache中对应元素的一个copy副本，而不是对应的一个引用。默认为false。
	 */
	private boolean copyOnRead=false;
	
	/**
	 * Cache级别配置：当指定该属性为true时，我们在往Cache中写入数据时用的是原对象的一个copy副本，而不是对应的一个引用。默认为false。
	 */
	private boolean copyOnWrite=false;
	
	/**
	 * Cache级别配置：当清理Cache所有键值时，是否清理当前Cache在磁盘上的缓存
	 */
	private boolean clearOnFlush=true;
	
	/**
	 * Cache级别配置：单位是秒，表示一个元素所允许闲置的最大时间，也就是说一个元素在不被请求的情况下允许在缓存中待的最大时间。默认是0，表示不限制。 
	 */
	private long timeToIdleSeconds=0;
	
	/**
	 * Cache级别配置：单位是秒，表示无论一个元素闲置与否，其允许在Cache中存在的最大时间。默认是0，表示不限制。 
	 */
	private long timeToLiveSeconds=0;
	
	/**
	 * Cache级别配置：表示是否永恒，默认为false。如果设为true，将忽略timeToIdleSeconds和timeToLiveSeconds，Cache内的元素永远都不会过期，也就不会因为元素的过期而被清除了。 
	 */
	private boolean eternal=false;
	
	/**
	 * Cache级别配置：单位是秒，表示多久检查元素是否过期的线程多久运行一次，默认是120秒。
	 */
	private long diskExpiryThreadIntervalSeconds=120;
	
	/**
	 * Cache级别配置：当内存里面的元素数量或大小达到指定的限制后将采用的驱除策略:<br/>
	 * LRU  （默认,最近最少使用）<br/>LFU  （最不常使用）<br/>FIFO  （先进先出）
	 */
	private String memoryStoreEvictionPolicy="LRU";
	
	/**
	 * 由 应用配置文件 创建一个ehcache管理器
	 * <br/>可配置为：<br/>
	 * user.home  		->  C:\Users\用户名  <br/>
	 * user.dir	  		->  E:\Eclipses\workspace\SSM_Demo    (当前项目的磁盘位置)  <br/>
	 * java.io.tmpdir	->  C:\Users\用户名\AppData\Local\Temp\
	 * 应用配置文件：application.properties || application.yml
	 * @return net.sf.ehcache.CacheManager
	 */
	private CacheManager createCacheManagerFrom_Factory() {
		Configuration configuration=new Configuration();
		configuration.diskStore(new DiskStoreConfiguration().path(diskStorePath));
		configuration.setMaxBytesLocalDisk(maxBytesLocalDisk);
		configuration.setMaxBytesLocalHeap(maxBytesLocalHeap);
		//配置cache
		CacheConfiguration cacheConfiguration=new CacheConfiguration();
		cacheConfiguration.setName(AppCachePool.EhCache.toString());
		cacheConfiguration.setMaxEntriesInCache(maxEntriesInCache);
		cacheConfiguration.setMaxEntriesLocalDisk(maxEntriesLocalDisk);
		cacheConfiguration.setMaxEntriesLocalHeap(maxEntriesLocalHeap);
		cacheConfiguration.setCopyOnRead(copyOnRead);
		cacheConfiguration.setCopyOnWrite(copyOnWrite);
		cacheConfiguration.setClearOnFlush(clearOnFlush);
		cacheConfiguration.setTimeToIdleSeconds(timeToIdleSeconds);
		cacheConfiguration.setTimeToLiveSeconds(timeToLiveSeconds);
		cacheConfiguration.setEternal(eternal);
		cacheConfiguration.setDiskExpiryThreadIntervalSeconds(diskExpiryThreadIntervalSeconds);
		cacheConfiguration.memoryStoreEvictionPolicy(memoryStoreEvictionPolicy);
		//cache持久化策略
		PersistenceConfiguration persistenceConfiguration=new PersistenceConfiguration();
		persistenceConfiguration.setStrategy(persistenceStrategy);
		cacheConfiguration.persistence(persistenceConfiguration);
		
		configuration.addCache(cacheConfiguration);
		return CacheManager.create(configuration);
	}

	public void setDiskStorePath(String diskStorePath) {
		this.diskStorePath = diskStorePath;
	}

	public void setMaxBytesLocalDisk(String maxBytesLocalDisk) {
		this.maxBytesLocalDisk = maxBytesLocalDisk;
	}

	public void setMaxBytesLocalHeap(String maxBytesLocalHeap) {
		this.maxBytesLocalHeap = maxBytesLocalHeap;
	}

	public void setPersistenceStrategy(String persistenceStrategy) {
		this.persistenceStrategy = persistenceStrategy;
	}

	public void setMaxEntriesInCache(long maxEntriesInCache) {
		this.maxEntriesInCache = maxEntriesInCache;
	}

	public void setMaxEntriesLocalDisk(long maxEntriesLocalDisk) {
		this.maxEntriesLocalDisk = maxEntriesLocalDisk;
	}

	public void setMaxEntriesLocalHeap(long maxEntriesLocalHeap) {
		this.maxEntriesLocalHeap = maxEntriesLocalHeap;
	}

	public void setCopyOnRead(boolean copyOnRead) {
		this.copyOnRead = copyOnRead;
	}

	public void setCopyOnWrite(boolean copyOnWrite) {
		this.copyOnWrite = copyOnWrite;
	}

	public void setClearOnFlush(boolean clearOnFlush) {
		this.clearOnFlush = clearOnFlush;
	}

	public void setTimeToIdleSeconds(long timeToIdleSeconds) {
		this.timeToIdleSeconds = timeToIdleSeconds;
	}

	public void setTimeToLiveSeconds(long timeToLiveSeconds) {
		this.timeToLiveSeconds = timeToLiveSeconds;
	}

	public void setEternal(boolean eternal) {
		this.eternal = eternal;
	}

	public void setDiskExpiryThreadIntervalSeconds(long diskExpiryThreadIntervalSeconds) {
		this.diskExpiryThreadIntervalSeconds = diskExpiryThreadIntervalSeconds;
	}

	public void setMemoryStoreEvictionPolicy(String memoryStoreEvictionPolicy) {
		this.memoryStoreEvictionPolicy = memoryStoreEvictionPolicy;
	}
	
	private String arrayToString(String[] strings) {
		String str="";
		for(String string:strings) {
			str=str+","+string;
		}
		return str.substring(1);
	}
}
