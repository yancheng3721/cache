package com.concentrate.search.cache.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

import com.concentrate.search.cache.Cache;
import com.concentrate.search.cache.CacheHolder;
import com.concentrate.search.cache.config.Constants;

public class RedisCache implements Cache<Serializable, Serializable>{
    
    public static final int REDIS_ENABLED = 1;
    public static final int REDIS_DISABLED = 0;
    

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisCache.class);
	
    protected String moduleName ;
    protected String functionName;
     
    RedisCache( String moduleName,String functionName, ShardedJedisPool redisPool) {
        this.moduleName = moduleName;
        this.functionName = functionName;
        this.redisPool = redisPool;
    }
    
    protected ShardedJedisPool redisPool = null;
    
    public void clear() {
    	if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return ;
        }
    	long rs = 0l;
    	ShardedJedis jedis = null;
    	try {
    		jedis = redisPool.getResource();
    		rs = jedis.del(getBytesFromObject(keyPrefix("*")));
    		redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!"+e.getMessage());
			redisPool.returnBrokenResource(jedis);
		}
    }

    public boolean contains(Serializable key) {
    	if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return false;
        }
    	boolean rs = false;
    	ShardedJedis jedis = null;
    	try {
    		jedis = redisPool.getResource();
    		rs = jedis.exists(getBytesFromObject(keyPrefix(key)));
    		redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!"+e.getMessage());
			redisPool.returnBrokenResource(jedis);
		}
        return rs;
    }

    public void remove(Serializable key) {
    	if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return ;
        }
    	long rs = 0l;
    	ShardedJedis jedis = null;
    	try {
    		jedis = redisPool.getResource();
    		rs = jedis.del(getBytesFromObject(keyPrefix(key)));
    		redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!"+e.getMessage());
			redisPool.returnBrokenResource(jedis);
		}
    }

    public void warm() {
        
        
    }

    public long totalSize() {
        //unsupported
        return 0;
    }

    public long limitSize() {
        return 0;
    }

    public Serializable get(Serializable key) {
        if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return null;
        }
    	Serializable rs = null;
    	ShardedJedis jedis = null;
    	try {
    		jedis = redisPool.getResource();
			rs = (Serializable) getObjectFromBytes(jedis.get(getBytesFromObject(keyPrefix(key))));
			redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!"+e.getMessage());
			redisPool.returnBrokenResource(jedis);
		}
        return rs;
    }
    
    public Serializable put(Serializable key, Serializable value) {
        if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return null;
        }
    	Serializable rs = null;
    	ShardedJedis jedis = null;
    	try {
    		jedis = redisPool.getResource();
    		rs = jedis.setex(getBytesFromObject(keyPrefix(key)), 300, getBytesFromObject(value));
			redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!"+e.getMessage());
			redisPool.returnBrokenResource(jedis);
		}
        return rs;
    }

    public String infoString() {
        return "REDIS CACHE：";
    }
    
    protected Serializable keyPrefix(Serializable key){
        return CacheHolder.cacheKey(moduleName, functionName)+key;
    }

    public static byte[] getBytesFromObject(Serializable obj) {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(bo);
			oos.writeObject(obj);
		} catch (IOException e1) {
			LOGGER.error("IO error!"+e1.getMessage());
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				LOGGER.error("IO error!"+e.getMessage());
			}
		}
        return bo.toByteArray();
     }

    public static Object getObjectFromBytes(byte[] objBytes) {
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream ois = null;
        Object rs = null;
        try {
        	ois = new ObjectInputStream(bi);
			rs = ois.readObject();
		} catch (ClassNotFoundException e) {
			LOGGER.error("ClassNotFoundException error!"+e.getMessage());
		} catch (IOException e) {
			LOGGER.error("IO error!"+e.getMessage());
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				LOGGER.error("IO error!"+e.getMessage());
			}
		}
        return rs;
    }

	public Map<Serializable, Serializable> mget(Collection<Serializable> keys) {
		if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return null;
        }
    	ShardedJedis jedis = null;
    	Map<Serializable, Serializable> result = new HashMap<Serializable, Serializable>(keys.size());
    	try {
    		jedis = redisPool.getResource();
    		ShardedJedisPipeline pipeline = jedis.pipelined();
            Map<Serializable, Response<byte[]>> responses = new HashMap<Serializable, Response<byte[]>>(keys.size());  
            for (Serializable key : keys) {
                responses.put(key, pipeline.get(getBytesFromObject(keyPrefix(key))));  
            }  
            pipeline.sync();  
            for (Serializable key : responses.keySet()) {
                result.put(key, (Serializable) getObjectFromBytes(responses.get(key).get()));
            } 
			redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!"+e.getMessage());
			redisPool.returnBrokenResource(jedis);
		}
        return result;
	}

	public void mput(Map<Serializable, Serializable> map) {
		if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return;
        }
    	ShardedJedis jedis = null;
    	try {
    		jedis = redisPool.getResource();
    		ShardedJedisPipeline pipeline = jedis.pipelined();
            for (Entry<Serializable, Serializable> entry : map.entrySet()) {
            	pipeline.setex(getBytesFromObject(keyPrefix(entry.getKey())), 300, getBytesFromObject(entry.getValue()));
            }
            
            pipeline.syncAndReturnAll();
			redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!"+e.getMessage());
			redisPool.returnBrokenResource(jedis);
		}
	}
}
