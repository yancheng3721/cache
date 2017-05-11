package com.concentrate.search.cache;

import java.util.Map;



public interface MapCache <K, V > extends Cache<K, V> {
	
	V hput(K key1, K key2, V value) ;

	V hget(K key1, K key2) ;
	
	long hdel(K key1,K key2);

	long hsize(K key1);
	
	Map<K, V> hgetall(K key2);
}
