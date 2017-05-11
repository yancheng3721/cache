package com.concentrate.search.cache.redis.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XML转化工具
 * 
 * @author 12091669
 *
 */
public class XMLConfigParser {
    private final Logger logger = LoggerFactory.getLogger(XMLConfigParser.class);

    public Map<String, List<RedisBean>> getRedisBean(String filePath) {
        logger.info("开始加载redis的配置文件数据================");
        File inputXml = new File(filePath);
        SAXReader saxReader = new SAXReader();
        RedisBean redisBean = null;
        List<RedisBean> redisList = null;
        Map<String, List<RedisBean>> redisMap = new HashMap<String, List<RedisBean>>();
        try {
            Document document = saxReader.read(inputXml);
            Element root = document.getRootElement();
            Iterator<?> iter = root.elementIterator("redisGroup");
            while (iter.hasNext()) {
                Element recordEle = (Element) iter.next();
                String mapName = recordEle.attributeValue("name");
                Iterator<?> childIter = recordEle.elementIterator("redis");
                redisList = new ArrayList<RedisBean>();
                while (childIter.hasNext()) {
                    Element childEle = (Element) childIter.next();
                    redisBean = new RedisBean();
                    redisBean.setRedisIp(childEle.elementTextTrim("ip"));
                    redisBean.setMaxActive(parserInt(childEle.elementText("maxActive")));
                    redisBean.setMaxIdle(parserInt(childEle.elementText("maxIdle")));
                    redisBean.setMinIdle(parserInt(childEle.elementText("minIdle")));
                    redisBean.setMaxWait(parserInt(childEle.elementText("maxWait")));
                    redisBean.setTestOnBorrow(paserBoolean(childEle.elementText("testOnBorrow")));
                    redisBean.setPort(parserInt(childEle.elementText("port")));
                    redisBean.setTimeout(parserInt(childEle.elementText("timeout")));
                    redisBean.setIsable(paserBoolean(childEle.elementText("isable")));
                    redisList.add(redisBean);
                }
                redisMap.put(mapName, redisList);
            }
        } catch (DocumentException e) {
            logger.error("redis配置文件加载过程出现异常", e);
        }
        return redisMap;
    }

    private int parserInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean paserBoolean(String str) {
        return "true".equals(str);
    }
}
