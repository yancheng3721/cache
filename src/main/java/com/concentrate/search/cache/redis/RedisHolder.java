package com.concentrate.search.cache.redis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import com.concentrate.search.cache.CacheHolder;
import com.concentrate.search.cache.config.Constants;
import com.concentrate.search.cache.redis.xml.RedisBean;
import com.concentrate.search.cache.redis.xml.XMLConfigParser;

public abstract class RedisHolder<K,V> extends CacheHolder<K,V> {
    
    
    //REDIS组定义
    private Map<String, ShardedJedisPool> shardPoolMap = new HashMap<String, ShardedJedisPool>(); // 连接池的资源map
    //功能点，使用哪一组REDIS
    private Map<String,String> groupConfig = new HashMap<String,String>();
    
    public void initial() {
        XMLConfigParser xmlPaser = new XMLConfigParser();
        Map<String, List<RedisBean>> map = xmlPaser.getRedisBean(Constants.REDIS_CONFIG_FILEPATH);
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        
        Map<String, ShardedJedisPool> tempShardedPoolMap = new HashMap<String, ShardedJedisPool>();

        while (iter.hasNext()) {
            String mapKey = iter.next();
            List<RedisBean> redisList = map.get(mapKey);
            List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
            
            for (int size = 0; size < redisList.size(); size++) {
                RedisBean bean = redisList.get(size);
                if (bean.isIsable()) {
                    JedisShardInfo jedisShardInfo = new JedisShardInfo(bean.getRedisIp(), bean.getPort(), bean.getTimeout());
            		list.add(jedisShardInfo);

                }
            }
            
            ShardedJedisPool shardedPool;
            JedisPoolConfig config = new JedisPoolConfig();
//    		config.setMaxTotal(redisList.get(0).getMaxActive());
    		config.setMaxIdle(redisList.get(0).getMaxIdle());
    		config.setMaxWaitMillis(redisList.get(0).getMaxWait());
    		config.setTestOnBorrow(redisList.get(0).isTestOnBorrow());
            shardedPool = new ShardedJedisPool(config, list);
            tempShardedPoolMap.put(mapKey, shardedPool);

        }
        
        shardPoolMap.clear();
        shardPoolMap.putAll(tempShardedPoolMap);
    }
    
    private String getRedisGroupByModuleAndFunction(String module,String function){
        String redisGroup = this.groupConfig.get(module+"_"+function);
        if(redisGroup == null){
            redisGroup = Constants.DEFAULT_REDIS_GROUP_NAME;
        }
        return redisGroup;
    }
    
    /**
     * 
     * 功能描述: <br>
     * 取得REDIS CLIENT
     *
     * @param module
     * @param function
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public ShardedJedisPool getShardRedisPool(String module,String function){
        return shardPoolMap.get(getRedisGroupByModuleAndFunction(module,function));
    }
    
    
}
