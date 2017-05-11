package com.concentrate.search.cache.jvm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.concentrate.search.cache.CollectionCache;


@SuppressWarnings({ "unchecked", "rawtypes" })
public class JVMCollectionCache<K,V> extends JVMCache<K,V> implements CollectionCache<K,V>{

	private Class<? extends Collection> collectionClass;
	
	JVMCollectionCache(Class<? extends Map> mapClass,Class<? extends Collection> collectionClass,int limitSize){
		super(mapClass, limitSize);
		this.collectionClass = collectionClass;
	}

	public void lput(K key, V value) {
		Collection oldValues = (Collection) cacheHolder.get(key);
		if(oldValues == null){
			oldValues =  (Collection) newInstanceCollection(collectionClass);
			cacheHolder.put(key, (V) oldValues);
		}
		oldValues.add(value);
	}
	
	protected Collection<?> newInstanceCollection( Class<? extends Collection> collectionClass){
        Collection<?> result = null;
        try {
            result = collectionClass.newInstance();
        } catch (Exception e) {
            result = new ArrayList();
        }
        return result;
    }

	public long lsize(K key) {
		long result = 0;
		Collection c = (Collection) cacheHolder.get(key);
		if(c!=null){
			result = c.size();
		}
		return result;
	}
	
}
