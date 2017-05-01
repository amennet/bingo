/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.framework.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigUtils.class);
    
    public static boolean isNotEmpty(String value) {
        return ! isEmpty(value);
    }
	
	public static boolean isEmpty(String value) {
		return value == null || value.length() == 0 
    			|| "false".equalsIgnoreCase(value) 
    			|| "0".equalsIgnoreCase(value) 
    			|| "null".equalsIgnoreCase(value) 
    			|| "N/A".equalsIgnoreCase(value);
	}
	
	public static boolean isDefault(String value) {
		return "true".equalsIgnoreCase(value) 
				|| "default".equalsIgnoreCase(value);
	}

    private static Pattern VARIABLE_PATTERN = Pattern.compile(
            "\\$\\s*\\{?\\s*([\\._0-9a-zA-Z]+)\\s*\\}?");
    
	public static String replaceProperty(String expression, Map<String, String> params) {
        if (expression == null || expression.length() == 0 || expression.indexOf('$') < 0) {
            return expression;
        }
        Matcher matcher = VARIABLE_PATTERN.matcher(expression);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) { // 逐个匹配
            String key = matcher.group(1);
            String value = System.getProperty(key);
            if (value == null && params != null) {
                value = params.get(key);
            }
            if (value == null) {
                value = "";
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
	
    private static volatile Properties PROPERTIES;
    
    public static void setProperties(Properties properties) {
        if (properties != null) {
            PROPERTIES = properties;
        }
    }
    
    public static Properties loadProperties(String fileName) {
        return loadProperties(fileName, false, false);
    }
    
    public static Properties loadProperties(String fileName, boolean allowMultiFile) {
        return loadProperties(fileName, allowMultiFile, false);
    }
    
	/**
	 * Load properties file to {@link Properties} from class path.
	 * 
	 * @param fileName properties file name. for example: <code>dubbo.properties</code>, <code>METE-INF/conf/foo.properties</code>
	 * @param allowMultiFile if <code>false</code>, throw {@link IllegalStateException} when found multi file on the class path.
     * @param optional is optional. if <code>false</code>, log warn when properties config file not found!s
	 * @return loaded {@link Properties} content. <ul>
	 * <li>return empty Properties if no file found.
	 * <li>merge multi properties file if found multi file
	 * </ul>
	 * @throws IllegalStateException not allow multi-file, but multi-file exsit on class path.
	 */
    public static Properties loadProperties(String fileName, boolean allowMultiFile, boolean optional) {
        Properties properties = new Properties();
        if (fileName.startsWith("/")) {
            try {
                FileInputStream input = new FileInputStream(fileName);
                try {
                    properties.load(input);
                } finally {
                    input.close();
                }
            } catch (Throwable e) {
                logger.warn("Failed to load " + fileName + " file from " + fileName + "(ingore this file): " + e.getMessage(), e);
            }
            return properties;
        }
        
        List<java.net.URL> list = new ArrayList<java.net.URL>();
        try {
            Enumeration<java.net.URL> urls = ClassHelper.getClassLoader().getResources(fileName);
            list = new ArrayList<java.net.URL>();
            while (urls.hasMoreElements()) {
                list.add(urls.nextElement());
            }
        } catch (Throwable t) {
            logger.warn("Fail to load " + fileName + " file: " + t.getMessage(), t);
        }
        
        if(list.size() == 0) {
            if (! optional) {
                logger.warn("No " + fileName + " found on the class path.");
            }
            return properties;
        }
        
        if(! allowMultiFile) {
            if (list.size() > 1) {
                String errMsg = String.format("only 1 %s file is expected, but %d dubbo.properties files found on class path: %s",
                        fileName, list.size(), list.toString());
                logger.warn(errMsg);
                // throw new IllegalStateException(errMsg); // see http://code.alibabatech.com/jira/browse/DUBBO-133
            }

            // fall back to use method getResourceAsStream
            try {
                properties.load(ClassHelper.getClassLoader().getResourceAsStream(fileName));
            } catch (Throwable e) {
                logger.warn("Failed to load " + fileName + " file from " + fileName + "(ingore this file): " + e.getMessage(), e);
            }
            return properties;
        }
        
        logger.info("load " + fileName + " properties file from " + list);
        
        for(java.net.URL url : list) {
            try {
                Properties p = new Properties();
                InputStream input = url.openStream();
                if (input != null) {
                    try {
                        p.load(input);
                        properties.putAll(p);
                    } finally {
                        try {
                            input.close();
                        } catch (Throwable t) {}
                    }
                }
            } catch (Throwable e) {
                logger.warn("Fail to load " + fileName + " file from " + url + "(ingore this file): " + e.getMessage(), e);
            }
        }
        
        return properties;
    }

    private static int PID = -1;
    
    public static int getPid() {
        if (PID < 0) {
            try {
                RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();  
                String name = runtime.getName(); // format: "pid@hostname"  
                PID = Integer.parseInt(name.substring(0, name.indexOf('@')));
            } catch (Throwable e) {
                PID = 0;
            }
        }
        return PID;  
    }

	private ConfigUtils() {}
	
}