//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.framework.register.support;

import com.framework.common.register.URL;
import com.framework.register.NotifyListener;
import com.framework.register.Registry;
import com.framework.register.RegistryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperRegisterHelper {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegisterHelper.class);

    private RegistryFactory registryFactory;

    private URL registryUrl;

    public ZookeeperRegisterHelper(URL registryUrl, RegistryFactory registryFactory) {
        this.registryUrl = registryUrl;
        this.registryFactory = registryFactory;
    }

    public void register(URL url) {
        Registry registry = registryFactory.getRegistry(registryUrl);
        registry.register(url);
        logger.info("zookeeper server 注册成功");
    }

    public void subscribe(URL url, NotifyListener notifyListener) {
        Registry registry = registryFactory.getRegistry(registryUrl);
        registry.subscribe(url, notifyListener);
        logger.info("zookeeper broker 获取完成");
    }

    public void unRegister(URL url) {
        Registry registry = registryFactory.getRegistry(registryUrl);
        if (registry != null) {
            registry.unregister(url);
            logger.info("zookeeper unregister success...");
        }
    }

    public void unSubscribe(URL url, NotifyListener notifyListener) {
        Registry registry = registryFactory.getRegistry(registryUrl);
        registry.unsubscribe(url, notifyListener);
        logger.info("zookeeper unSubscribe success...");
    }
}
