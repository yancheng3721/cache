package com.concentrate.search.cache.redis;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.concentrate.search.cache.CollectionCache;
import com.concentrate.search.cache.config.Constants;

public class RedisCollectionCache extends RedisCache implements CollectionCache<Serializable, Serializable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisCollectionCache.class);
	
    RedisCollectionCache(String m,String f,ShardedJedisPool jedisPool) {
        super(m,f,jedisPool);
    }

    public void lput(Serializable key, Serializable value) {
    	if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return ;
        }
    	ShardedJedis jedis = null;
    	try {
    		jedis = redisPool.getResource();
    		jedis.lpush(getBytesFromObject(keyPrefix(key)), getBytesFromObject(value));
    		redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!"+e.getMessage());
			redisPool.returnBrokenResource(jedis);
		}
    }

    public long lsize(Serializable key) {
    	if(Constants.DEFALUT_REDIS_ENABLED != REDIS_ENABLED){
            return 0l;
        }
    	ShardedJedis jedis = null;
    	long rs = 0l;
    	try {
    		jedis = redisPool.getResource();
    		rs = jedis.llen(getBytesFromObject(keyPrefix(key)));
    		redisPool.returnResource(jedis);
		} catch (Exception e) {
			LOGGER.error("redis connection error!"+e.getMessage());
			redisPool.returnBrokenResource(jedis);
		}
    	return rs;
    }


}
