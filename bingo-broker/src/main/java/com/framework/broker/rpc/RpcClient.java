package com.framework.broker.rpc;

import com.framework.common.exception.BingoException;
import com.weibo.api.motan.config.ProtocolConfig;
import com.weibo.api.motan.config.RefererConfig;
import com.weibo.api.motan.config.RegistryConfig;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ZhangGe on 2017/5/5.
 */
public final class RpcClient {

    private static String zookeeperAddr;
    private static ConcurrentHashMap<Class, Object> referMap = new ConcurrentHashMap<>();

    public static void addRefer(Class serviceClass, String registerAddr) {
        if (!serviceClass.isInterface()) {
            throw new BingoException("serviceClass 必须为interface");
        }
        Object object = MotanApiClient.newReferer(serviceClass, registerAddr);
        referMap.put(serviceClass, object);
    }

    public static <T> T getRefer(Class<T> serviceClass) {
        if (serviceClass == null) {
            throw new BingoException("serviceClass 参数不能为空");
        }
        if (!serviceClass.isInterface()) {
            throw new BingoException("serviceClass 必须为interface");
        }
        return (T) referMap.get(serviceClass);
    }
}

final class MotanApiClient {

    public static <T> T newReferer(Class<T> serviceClass, String registerAddr) {
        RefererConfig<T> refererConfig = new RefererConfig<T>();

        // 设置接口及实现类
        refererConfig.setInterface(serviceClass);

        // 配置ZooKeeper注册中心
        RegistryConfig zookeeperRegistry = new RegistryConfig();
        zookeeperRegistry.setRegProtocol("zookeeper");
        zookeeperRegistry.setAddress(registerAddr);
        refererConfig.setRegistry(zookeeperRegistry);

        // 配置RPC协议
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setId("motan");
        protocol.setName("motan");
        refererConfig.setProtocol(protocol);

        // 使用服务
        T ref = refererConfig.getRef();

        return ref;
    }

}

