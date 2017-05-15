package com.framework.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static final Properties properties = new Properties();

    public static String getPropertyByProperties(String filename, String field) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(filename);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("PropertiesUtil 加载属性文件失败, filename : {}, field : {}", filename, field);
        }
        return properties.getProperty(field);
    }

}
