package com.concentrate.search.cache;



public interface CollectionCache<K, V > extends Cache<K, V> {
	
	void lput(K key,V value);
	
	long lsize(K key);
	
}
