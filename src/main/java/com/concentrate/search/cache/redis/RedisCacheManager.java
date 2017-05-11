package com.concentrate.search.cache.redis;

import java.io.Serializable;

import com.concentrate.search.cache.Cache;
import com.concentrate.search.cache.CacheManager;
import com.concentrate.search.cache.CollectionCache;
import com.concentrate.search.cache.MapCache;

/**
 * 
 * 〈一句话功能简述〉<br> 
 * REDIS缓存管理类<br>
 * mapClass/collectionClass是指REDIS缓存如果需要在jvm也一份的话，该JVM缓存以什么类型的Map进行JVM缓存<br>
 * REDIS的缓存基于SNF的REDIS缓存框架实现，存储的key value均为二进制
 *
 * @author 12091669
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@SuppressWarnings("unchecked")
public class RedisCacheManager extends RedisHolder<Serializable, Serializable> implements CacheManager {
    
    
    private RedisCacheManager() {
        initial();
    }

    private static CacheManager instance = new RedisCacheManager();
    
    public static CacheManager getInstance (){
        return instance;
    }
    
    public <K, V> CollectionCache<K, V> newCollectionCache(String moduleName, String functionName, Class<?> mapClass,
            Class<?> collectionClass, int limitSize) {
        CollectionCache<Serializable, Serializable>  cache =  (CollectionCache<Serializable, Serializable>) getCache(moduleName,functionName);
        if(cache==null){
            cache = (CollectionCache<Serializable, Serializable>) new RedisCollectionCache(moduleName,functionName,getShardRedisPool(moduleName,functionName));
            addCache(moduleName,functionName,cache);
        }
        return (CollectionCache<K, V>) cache;
    }

    public <K, V> MapCache<K, V> newMapCache(String moduleName, String functionName, Class<?> mapClass, int limitSize) {
        MapCache<Serializable, Serializable>  cache =  (MapCache<Serializable, Serializable>) getCache(moduleName,functionName);
        if(cache==null){
            cache = (MapCache<Serializable, Serializable>) new RedisMapCache(moduleName,functionName,getShardRedisPool(moduleName,functionName));
            addCache(moduleName,functionName,cache);
        }
        return (MapCache<K, V>) cache;
    }


    public <K, V> Cache<K, V> newCache(String moduleName, String functionName, Class<?> mapClass, int limitSize) {
        Cache<Serializable, Serializable>  cache =  (Cache<Serializable, Serializable>) getCache(moduleName,functionName);
        if(cache==null){
            cache = (Cache<Serializable, Serializable>) new RedisCache(moduleName,functionName,getShardRedisPool(moduleName,functionName));
            addCache(moduleName,functionName,cache);
        }
        return (Cache<K, V>) cache;
    }

}
