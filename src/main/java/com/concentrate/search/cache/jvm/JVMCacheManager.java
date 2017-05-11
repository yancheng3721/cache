package com.concentrate.search.cache.jvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import com.concentrate.search.cache.Cache;
import com.concentrate.search.cache.CacheHolder;
import com.concentrate.search.cache.CacheManager;
import com.concentrate.search.cache.CollectionCache;
import com.concentrate.search.cache.MapCache;

/**
 * 
 * 〈一句话功能简述〉<br> 
 * JVM缓存工厂方法
 *
 * @author 12091669
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class JVMCacheManager extends CacheHolder implements CacheManager{
    
	private static CacheManager instance = new JVMCacheManager();
	
	private JVMCacheManager(){
		
	}
	
	public static CacheManager getInstance(){
		return instance;
	}
	
	public <K,V> CollectionCache<K, V > newCollectionCache(String moduleName,String functionName,Class<?> mapClass,Class<?> collectionClass  ,int limitSize){
		
		CollectionCache<K,V>  cache = (JVMCollectionCache<K,V>) getCache(moduleName,functionName);
		if(cache==null){
			cache = (CollectionCache<K,V>) new JVMCollectionCache(mapClass,collectionClass,limitSize);
			addCache(moduleName,functionName,cache);
		}
		return cache;
	}
	
	public <K, V > MapCache<K, V > newMapCache(String moduleName,String functionName,Class<?> mapClass,int limitSize){
		JVMMapCache<K, V >  cache = (JVMMapCache<K, V>) getCache(moduleName,functionName);
		if(cache==null){
			cache = new JVMMapCache(mapClass,limitSize);
			addCache(moduleName,functionName,cache);
		}
		return cache;
	}
	
	public <K,V> Cache<K,V> newCache(String moduleName,
			String functionName, Class<?> mapClass, int limitSize) {
		JVMCache<K,V>  cache = (JVMCache<K,V>) getCache(moduleName,functionName);
		if(cache==null){
			cache = new JVMCache<K,V>(mapClass,limitSize);
			addCache(moduleName,functionName,cache);
		}
		return cache;
	}
	
	
	public static void main(String[] args) {
		CacheManager cm = getInstance();
		Cache<String,String> c1 = cm.newCache("联想词", "中文联想词缓存", HashMap.class, 0);
		c1.put("手机", "手机32323");
		c1.put("手机", "手机3123123");
		CollectionCache<String,String> c2 = cm.newCollectionCache("联想词", "中文联想词缓存列表",   HashMap.class,  ArrayList.class, 0);
		c2.lput("手机", "手机32323");
		c2.lput("手机", "手机32323");
		c2.lput("手机", "手机32323");
		c2.lput("手机", "手机32323");
		c2.lput("手机", "手机3123123");
		MapCache<String,String> c3 = cm.newMapCache("联想词", "中文联想词缓存对象",   TreeMap.class, 0);
		c3.hput("手机", "尺寸", "1000");
		c3.hput("手机", "价格", "1000");
		c3.hput("手机", "型号", "iphone4s");
		c3.hput("电视机", "尺寸", "2000");
		c3.hput("电视机", "价格", "2000");
		c3.hput("电视机", "型号", "CHANGHONG4s");
		
		System.out.println(c1);
		System.out.println("===============");
		System.out.println(c2);
		System.out.println("===============");
		System.out.println(c3);
		System.out.println("===============");
		CacheHolder ch = (CacheHolder) cm;
		ch.displayCacheInfo();
		c1.remove("手机");
		c1.remove("冰箱");
		c2.remove("手机");
		c2.remove("冰箱");
		c3.remove("手机");
		c3.remove("电视机");
		ch.displayCacheInfo();
	}


}
