package com.framework.broker;

import com.framework.broker.cluster.HaStrategy;
import com.framework.broker.cluster.LoadBalance;
import com.framework.broker.processor.DefaultRemotingProcessor;
import com.framework.common.register.URL;
import com.framework.register.RegistryFactory;
import com.framework.register.support.ZookeeperRegisterHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangGe on 2017/4/29.
 */
public class BrokerConfig {

    private ZookeeperRegisterHelper zookeeperRegisterHelper;

    private Map<URL, DefaultRemotingProcessor> registerUrls = new HashMap<>();

    private List<URL> subsribeUrls;

    private LoadBalance loadBalance;

    private HaStrategy haStrategy;

    private Map<Integer/* reqeustCode */, String/* brokerName */> requestBrokerName;

    public ZookeeperRegisterHelper getZookeeperRegisterHelper() {
        return zookeeperRegisterHelper;
    }

    public void setZookeeperRegisterHelper(ZookeeperRegisterHelper zookeeperRegisterHelper) {
        this.zookeeperRegisterHelper = zookeeperRegisterHelper;
    }

    public Map<URL, DefaultRemotingProcessor> getRegisterUrls() {
        return registerUrls;
    }

    public void setRegisterUrls(Map<URL, DefaultRemotingProcessor> registerUrls) {
        this.registerUrls = registerUrls;
    }

    public List<URL> getSubsribeUrls() {
        return subsribeUrls;
    }

    public void setSubsribeUrls(List<URL> subsribeUrls) {
        this.subsribeUrls = subsribeUrls;
    }

    public LoadBalance getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }

    public HaStrategy getHaStrategy() {
        return haStrategy;
    }

    public void setHaStrategy(HaStrategy haStrategy) {
        this.haStrategy = haStrategy;
    }

    public Map<Integer, String> getRequestBrokerName() {
        return requestBrokerName;
    }

    public void setRequestBrokerName(Map<Integer, String> requestBrokerName) {
        this.requestBrokerName = requestBrokerName;
    }


}
