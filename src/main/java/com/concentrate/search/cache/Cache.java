package com.concentrate.search.cache;

import java.util.Collection;
import java.util.Map;


public interface Cache<K,V> {

   void clear();
   
   boolean contains(K key);
	
   void remove(K key);
   
   void warm();
   
   long totalSize();
   
   long limitSize();
   
   V get(K key);
   
   V put(K key,V value);
   
   String infoString();
   
   Map<K, V> mget(Collection<K> keys);
   
   void mput(Map<K, V> map);
}
