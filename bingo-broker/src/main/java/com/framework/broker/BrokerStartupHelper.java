package com.framework.broker;

import com.framework.broker.cluster.hastrategy.FailfastHaStrategy;
import com.framework.broker.cluster.loadbalance.RoundRobinLoadBalance;
import com.framework.broker.processor.DefaultRemotingProcessor;
import com.framework.common.api.ICommand;
import com.framework.common.exception.BingoException;
import com.framework.common.register.URL;
import com.framework.register.support.ZookeeperRegisterHelper;
import com.framework.register.support.ZookeeperRegistryFactory;
import com.framework.register.zookeeper.zkclient.ZkclientZookeeperTransporter;

import java.util.*;

/**
 * Created by ZhangGe on 2017/5/1.
 */
public final class BrokerStartupHelper {

    private String registryUrl;

    private Map<URL, DefaultRemotingProcessor> serverUrls;

    private Set<URL> clientUrls;

    private Map<Integer, String> requestBrokerName;

    private BrokerConfig brokerConfig;

    private BrokerController brokerController;

    private boolean init;

    public BrokerStartupHelper(String registryUrl, ICommand... commands) {
        this.registryUrl = registryUrl;
        this.serverUrls = new HashMap<>();
        this.clientUrls = new HashSet<>();
        this.init = false;
        this.requestBrokerName = new HashMap<>();
        if (commands != null && commands.length > 0) {
            for (ICommand command : commands) {
                Map<Integer, String> cmds = command.getCommands();
                if (cmds != null && cmds.size() >0) {
                    requestBrokerName.putAll(cmds);
                }
            }
        }
    }

    private BrokerStartupHelper init() {
        brokerConfig = new BrokerConfig();

        brokerConfig.setHaStrategy(new FailfastHaStrategy());
        brokerConfig.setLoadBalance(new RoundRobinLoadBalance());

        URL regUrl = URL.registryUrl(registryUrl.split(","));

        ZookeeperRegistryFactory zookeeperRegistryFactory = new ZookeeperRegistryFactory(new ZkclientZookeeperTransporter());
        ZookeeperRegisterHelper zookeeperRegisterHelper = new ZookeeperRegisterHelper(regUrl, zookeeperRegistryFactory);
        brokerConfig.setZookeeperRegisterHelper(zookeeperRegisterHelper);

        return this;
    }

    public BrokerStartupHelper addServerUrl(String host, int port, String brokerName, DefaultRemotingProcessor processor) {
        serverUrls.put(URL.serverUrl(host, port, brokerName), processor);
        return this;
    }

    public BrokerStartupHelper addClientUrl(String brokerName) {
        clientUrls.add(URL.clientUrl(brokerName));
        return this;
    }


    public BrokerStartupHelper createBrokerController() {
        if (!init) {
            init();
            init = true;
        }

        if (serverUrls != null && serverUrls.size() > 0) {
            brokerConfig.setRegisterUrls(serverUrls);
        }

        if (clientUrls != null && clientUrls.size() > 0) {
            brokerConfig.setSubsribeUrls(new ArrayList<>(clientUrls));
        }

        if (requestBrokerName != null && requestBrokerName.size() > 0) {
            brokerConfig.setRequestBrokerName(requestBrokerName);
        }

        brokerController = new BrokerController(brokerConfig);

        return this;
    }

    public void start() {
        if (!init) {
            createBrokerController();
        }
        brokerController.start();
    }

    public BrokerController getBrokerController() {
        return brokerController;
    }
}
