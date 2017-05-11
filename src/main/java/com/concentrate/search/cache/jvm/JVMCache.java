package com.concentrate.search.cache.jvm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.concentrate.search.cache.Cache;

@SuppressWarnings({ "unchecked", "rawtypes" })
class JVMCache<K, V> implements Cache<K, V> {

	protected Map<K,V> cacheHolder ;
	protected int limitSize=-1;
	
	protected JVMCache(Class<?> mapClass,int limitSize){
		if(limitSize>0){
			this.limitSize = limitSize;
		}
		cacheHolder =  (Map<K, V>) newInstanceMap(mapClass);
	}
	
	protected Map<?, ?> newInstanceMap(Class<?> mapClass){
		Map<?, ?> result = null;
		try {
			result = (Map<?, ?>) mapClass.newInstance();
		} catch (Exception e) {
			result = new HashMap();
		}
		return result;
	}
	
	
	public void clear() {
		cacheHolder.clear();
	}

	public boolean contains(K key) {
		return cacheHolder.containsKey(key);
	}

	public void remove(K key) {
		cacheHolder.remove(key);
	}

	public void warm() {
		
	}

	public long totalSize() {
		return cacheHolder.size();
	}

	public long limitSize() {
		
		return limitSize;
	}
	
	public V get(K key) {
		return cacheHolder.get(key);
	}

	public V put(K key, V value) {
		return cacheHolder.put(key, value);
	}
	
	public String infoString() {
	    return "JVM CACHE: ";
	}

	@Override
	public String toString() {
		return "JVMCache [cacheHolder=" + cacheHolder + ", limitSize="
				+ limitSize + "]";
	}

	public Map<K, V> mget(Collection<K> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	public void mput(Map<K, V> map) {
		// TODO Auto-generated method stub
	}
	
	
}
