package com.concentrate.search.cache.redis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.concentrate.search.cache.MapCache;
import com.concentrate.search.cache.config.Constants;

public class RedisMapCache extends RedisCache implements MapCache<Serializable, Serializable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisCache.class);

    RedisMapCache(String m,String f,ShardedJedisPool redisPool) {
        super(m,f,redisPool);
    }
    
    public Map<Serializable, Serializable> hgetall(Serializable key) {
    	if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return null;
        }
    	Map<byte[], byte[]> rs = null;
    	ShardedJedis jedis = null;
    	try {
    		jedis = redisPool.getResource();
    		rs = jedis.hgetAll(getBytesFromObject(keyPrefix(key)));
			redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!", e);
			redisPool.returnBrokenResource(jedis);
		}
        return convert(rs);
    }

    private Map<Serializable, Serializable> convert(Map<byte[], byte[]> rs) {
    	Map<Serializable,Serializable> result = new HashMap<Serializable,Serializable>();
    	if(rs!=null){
    		for(Map.Entry<byte[],byte[]> e:rs.entrySet()){
    			byte[] key = e.getKey();
    			byte[] value = e.getValue();
    			result.put(new String(key), (Serializable) getObjectFromBytes(value));
    		}
    	}
		return result;
	}

	public Serializable hput(Serializable key1, Serializable key2, Serializable value) {
    	if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return null;
        }
    	Serializable rs = null;
    	ShardedJedis jedis = null;
    	try {
    		jedis = redisPool.getResource();
    		rs = jedis.hset(getBytesFromObject(keyPrefix(key1)), getBytesFromObject(key2), getBytesFromObject(value));
			redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!", e);
			redisPool.returnBrokenResource(jedis);
		}
    	
        return rs;
    }

    public Serializable hget(Serializable key1, Serializable key2) {
    	if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return null;
        }
    	Serializable rs = null;
    	ShardedJedis jedis = null;
    	try {
    		jedis = redisPool.getResource();
    		rs = (Serializable) getObjectFromBytes(jedis.hget(getBytesFromObject(keyPrefix(key1)), getBytesFromObject(key2)));
			redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!", e);
			redisPool.returnBrokenResource(jedis);
		}
        return rs;
    }

    public long hdel(Serializable key1, Serializable key2) {
    	if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return 0;
        }
    	long rs = 0;
    	ShardedJedis jedis = null;
    	try {
    		jedis = redisPool.getResource();
    		rs = jedis.hdel(getBytesFromObject(keyPrefix(key1)), getBytesFromObject(key2));
			redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!", e);
			redisPool.returnBrokenResource(jedis);
		}
        return rs;
    }

    public long hsize(Serializable key1) {
        
        return 0;
    }

    

}
