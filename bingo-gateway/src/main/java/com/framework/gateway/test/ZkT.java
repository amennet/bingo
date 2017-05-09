package com.framework.gateway.test;

import com.framework.broker.BrokerConfig;
import com.framework.broker.BrokerController;
import com.framework.common.register.URL;
import com.framework.register.support.ZookeeperRegisterHelper;
import com.framework.register.support.ZookeeperRegistryFactory;
import com.framework.register.zookeeper.zkclient.ZkclientZookeeperTransporter;

/**
 * Created by ZhangGe on 2017/5/1.
 */
public class ZkT {

    public static void main(String[] args) throws InterruptedException {
        URL registryUrl = URL.registryUrl("192.168.123.181:2181", "192.168.123.181.2183");

        ZookeeperRegistryFactory zookeeperRegistryFactory = new ZookeeperRegistryFactory(new ZkclientZookeeperTransporter());

        ZookeeperRegisterHelper zookeeperRegisterHelper = new ZookeeperRegisterHelper(registryUrl, zookeeperRegistryFactory);

        BrokerConfig brokerConfig = new BrokerConfig();
        brokerConfig.setZookeeperRegisterHelper(zookeeperRegisterHelper);

        URL regGateway1 = URL.serverUrl("127.0.0.1", 1235, "test");
        URL regGateway2 = URL.serverUrl("127.0.0.1", 2236, "test");
        URL regGateway3 = URL.serverUrl("127.0.0.1", 2237, "test");
        URL regGateway4 = URL.serverUrl("127.0.0.1", 2238, "test1");

        URL subGateway1 = URL.clientUrl("test");


        zookeeperRegisterHelper.register(regGateway1);
        zookeeperRegisterHelper.register(regGateway2);
        zookeeperRegisterHelper.register(regGateway3);
        zookeeperRegisterHelper.register(regGateway4);

        Thread.sleep(100000);
    }
}
