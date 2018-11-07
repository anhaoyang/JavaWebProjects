package com.anhaoyang.demo.ssm.cache.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.anhaoyang.demo.ssm.cache.AppAbstractCache;
import com.anhaoyang.demo.ssm.cache.AppCachePool;
@Component("Redis")
@ConfigurationProperties(prefix="spring.redis")
public class RedisCache implements AppAbstractCache,InitializingBean {
	Logger loger=LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
    public static final String name=AppCachePool.Redis.name();
    /**
     * 是否在启动时删除redis中所有的内容
     */
    public boolean preClear=true;
    /**
     * 键值 key在缓存中的存活时间（秒），默认：86500 (24小时)
     */
    public long livetimeDefault=86500;
    
    public boolean isPreClear() {
		return preClear;
	}

	public void setPreClear(boolean preClear) {
		this.preClear = preClear;
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    public String getName() {
        return RedisCache.name;
    }

    @Override
    public Object getNativeCache() {
        return this.redisTemplate;
    }
    
    //把之前的缓存都清除掉
	@Override
	public void afterPropertiesSet() throws Exception {
		if(preClear) {
			this.clear();
			loger.warn("清除redis缓存成功");
		}
	}

	@Override
	public void clear() {
		redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
	}

	@Override
	public ValueWrapper getValueWrapper(Map<String, Object> keys) {
		final String keyf = (String) keys.get("key");
        Object object = null;
        object = redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] key = keyf.getBytes();
                byte[] value = connection.get(key);
                if (value == null) {
                    return null;
                }
                return toObject(value);
			}
        });
        return (object != null ? new SimpleValueWrapper(object) : null);
	}

	@Override
	public <T> T getT(Map<String, Object> keys, Class<T> type) {
		final String keyf = (String) keys.get("key");
        T object = null;
        object = redisTemplate.execute(new RedisCallback<T>() {
			@Override
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] key = keyf.getBytes();
                byte[] value = connection.get(key);
                if (value == null) {
                    return null;
                }
                return toT(value,type);
			}
        });
        return (object != null ? object : null);
	}

	@Override
	public <T> T getT(Map<String, Object> keys, Callable<T> valueLoader) {
		try {
			return valueLoader.call();
		} catch (Exception e) {
			throw new ValueRetrievalException(keys.get("key"), valueLoader, e);
		}
	}

	@Override
	public void putObject(Map<String, Object> keys, Object value) {
		final String keyf = (String) keys.get("key");
        final Object valuef = value;
        long liveTimePre = livetimeDefault;
        if(keys.containsKey("liveTime")) {
        	liveTimePre=Long.parseLong(keys.get("liveTime").toString());
        }
        final long liveTime=liveTimePre;
        
        redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] keyb = keyf.getBytes();
                byte[] valueb = toByteArray(valuef);
                connection.set(keyb, valueb);
                if (liveTime > 0) {
                    connection.expire(keyb, liveTime);
                }
                return 1L;
            }
        });
	}

	@Override
	public ValueWrapper putObjectIfAbsent(Map<String, Object> keys, Object value) {
		final String keyf = (String) keys.get("key");
        final Object valuef = value;
        long liveTimePre = livetimeDefault;
        if(keys.containsKey("liveTime")) {
        	liveTimePre=Long.parseLong(keys.get("liveTime").toString());
        }
        final long liveTime=liveTimePre;
        
        Object object = null;
        object = redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keyb = keyf.getBytes();
                byte[] mValue = connection.get(keyb);
                if (mValue == null) {
                	byte[] valueb = toByteArray(valuef);
                    connection.set(keyb, valueb);
                    if (liveTime > 0) {
                        connection.expire(keyb, liveTime);
                    }
                }
                return toObject(mValue);
			}
        });
        return (object != null ? new SimpleValueWrapper(object) : null);
	}

	@Override
	public void evict(Map<String, Object> keys) {
		final String keyf = (String) keys.get("key");
        redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                return connection.del(keyf.getBytes());
            }
        });
	}


    private byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }


    private Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }
    
	@SuppressWarnings("unchecked")
	private <T> T toT(byte[] bytes , Class<T> type) {
        T obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object object=ois.readObject();
            obj = (T) object;
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (ClassCastException ex) {
			loger.error("把对象转化为"+type.getName()+"失败：msg="+ex.getMessage());
		}
        return obj;
    }
	
}
