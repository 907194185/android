package com.gykj;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


/**
 * 加载本地配置文件的类
 * @author xianyl
 *
 */
public class PropertyConfigurer extends PropertyPlaceholderConfigurer {
	private Map<String, String> propertiesMap;
	 
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
                                     Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        propertiesMap = new HashMap<String, String>();
        String keyStr;
        String value;
        for (Object key : props.keySet()) {
            keyStr = key.toString();
            value = props.getProperty(keyStr);
            propertiesMap.put(keyStr, value);
        }
    }
    
    
    
    /**
     * 获取字符串key对应的值
     * @param name
     * @return
     */
    public String getContextProperty(String key) {
        return propertiesMap.get(key);
    }
}
