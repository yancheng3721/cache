package com.concentrate.search.cache;



/**
 * 缓存管理工厂方法类<br>
 * 该工厂返回常用的3类缓存：集合缓存 MAP缓存 对象缓存<br>
 * 获取缓存需指定 使用缓存的模块名/功能名/JVM缓存实现类/JVM缓存限制条数<br>
 * JVMCacheManager.getInstance();
 * RedisCacheManager.getInstance();
 * 
 * 
 * 
 * @author 12091669
 *
 */
public interface CacheManager   {
    
	public <K,V> CollectionCache<K, V> newCollectionCache(String moduleName,String functionName,Class<?> mapClass,Class<?> collectionClass ,int limitSize);

	public <K, V > MapCache<K,V> newMapCache(String moduleName,String functionName,Class<?> mapClass,int limitSize);

	public <K,V> Cache<K,V> newCache(String moduleName,String functionName,Class<?> mapClass,int limitSize);

}
