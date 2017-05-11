package com.concentrate.search.cache.jvm;

import java.util.Map;

import com.concentrate.search.cache.MapCache;
@SuppressWarnings({ "unchecked", "rawtypes" })
public class JVMMapCache<K, V > extends JVMCache<K, V> implements MapCache<K, V >{
	
	private Class<? extends Map> c;
	
	JVMMapCache(Class<? extends Map> c,int limitSize){
		super(c,limitSize);
		this.c=c;
	}
	
	public Map<K, V> hgetall(K key2) {
		return (Map<K, V>) cacheHolder.get(key2);
	}
	
	public V hput(K key1, K key2,
			V value) {
		Map<K, V> valueMap = (Map<K, V>) cacheHolder.get(key1);
		if(valueMap==null){
			valueMap=(Map<K, V>) newInstanceMap(c);
			cacheHolder.put(key1, (V) valueMap);
		}
		V oldValue = valueMap.get(key2);
		valueMap.put(key2, value);
		return oldValue;
	}

	public V hget(K key1, K key2) {
		Map<K, V> valueOld = (Map<K, V>) cacheHolder.get(key1);
		if(valueOld == null){
			return null;
		}
		return valueOld.get(key2);
	}
	
	public long hdel(K key1, K key2) {
		Map<K, V> valueOld = (Map<K, V>) cacheHolder.get(key1);
		long count = 0;
		if(valueOld != null){
			valueOld.remove(key2);
			count=1;
		}
		return count;
	}
	
	public long hsize(K key1) {
		long result = 0;
		Map<K, V> valueOld = (Map<K, V>) cacheHolder.get(key1);
		if(valueOld!=null){
			result = valueOld.size();
		}
		return result;
	}

}
