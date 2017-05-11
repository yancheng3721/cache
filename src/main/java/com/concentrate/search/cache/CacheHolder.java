package com.concentrate.search.cache;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;


public class CacheHolder<K,V> {

    private Map<String,Cache<K,V>> caches = new TreeMap<String,Cache<K,V>>();
    
    public static String cacheKey(String moduleName,String functionName){
        return moduleName+"_"+functionName+"_";
    }
    
    protected Cache<K,V> getCache(String moduleName,String functionName){
        return caches.get(cacheKey(moduleName,functionName));
    }
    
    protected void addCache(String moduleName,String functionName ,Cache<K,V> cache){
        if(caches.containsKey(cacheKey(moduleName,functionName))){
        	throw new RuntimeException("cache already exists!"+cacheKey(moduleName,functionName));
        }
    	caches.put(cacheKey(moduleName,functionName), cache);
    }
    
    
    public String displayCacheInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append("缓存模块\t缓存个数\t\n");
        int totalCaches = 0;
        if(caches!=null && caches.size()>0){
            for(Entry<String, Cache<K, V>> e:caches.entrySet()){
                String key = e.getKey();
                Cache<K, V> c = e.getValue();
                sb.append(key+"\t"+c.totalSize()+"\t\n");
                totalCaches+=c.totalSize();
            }
        }
        sb.append("------------------------------------------------------------------");
        sb.append("-------"+caches.size()+"个模块，总计"+totalCaches+"条缓存--------");
        System.out.println(sb);
        return sb.toString();
    }
}
