package com.framework.gateway;

import com.framework.broker.BrokerConfig;
import com.framework.broker.BrokerController;
import com.framework.broker.cluster.hastrategy.FailfastHaStrategy;
import com.framework.broker.cluster.loadbalance.RoundRobinLoadBalance;
import com.framework.common.api.ICommand;
import com.framework.common.register.URL;
import com.framework.gateway.network.GatewayController;
import com.framework.gateway.network.client.GatewayConfig;
import com.framework.gateway.processor.ClientProcessor;
import com.framework.gateway.processor.DefaultGatewayProcessor;
import com.framework.register.support.ZookeeperRegisterHelper;
import com.framework.register.support.ZookeeperRegistryFactory;
import com.framework.register.zookeeper.zkclient.ZkclientZookeeperTransporter;
import com.framework.remoting.netty.NettyRequestProcessor;

import java.util.*;

/**
 * Created by ZhangGe on 2017/5/3.
 */
public class GatewayStartupHelper {

    private String registryUrl;

    private Map<URL, NettyRequestProcessor> serverUrls;

    private Set<URL> clientUrls;

    private Map<Integer, String> requestBrokerName;

    private BrokerConfig brokerConfig;

    private BrokerController brokerController;

    private GatewayConfig gatewayConfig;

    private GatewayController gatewayController;

    private boolean init;

    public GatewayStartupHelper(String registryUrl, ICommand... commands) {
        this.registryUrl = registryUrl;
        this.serverUrls = new HashMap<>();
        this.clientUrls = new HashSet<>();
        this.init = false;
        this.requestBrokerName = new HashMap<>();
        this.brokerConfig = new BrokerConfig();
        this.gatewayConfig = new GatewayConfig();
        if (commands != null && commands.length > 0) {
            for (ICommand command : commands) {
                Map<Integer, String> cmds = command.getCommands();
                if (cmds != null && cmds.size() > 0) {
                    requestBrokerName.putAll(cmds);
                }
            }
        }
    }

    private GatewayStartupHelper init() {
        brokerConfig.setHaStrategy(new FailfastHaStrategy());
        brokerConfig.setLoadBalance(new RoundRobinLoadBalance());

        URL regUrl = URL.registryUrl(registryUrl.split(","));

        ZookeeperRegistryFactory zookeeperRegistryFactory = new ZookeeperRegistryFactory(new ZkclientZookeeperTransporter());
        ZookeeperRegisterHelper zookeeperRegisterHelper = new ZookeeperRegisterHelper(regUrl, zookeeperRegistryFactory);
        brokerConfig.setZookeeperRegisterHelper(zookeeperRegisterHelper);

        gatewayConfig.setZookeeperRegisterHelper(zookeeperRegisterHelper);
        gatewayConfig.setGatewayProcessor(new ClientProcessor());

        // TODO 完成listener
        gatewayConfig.setClientIdChangeListeners(null);

        return this;
    }

    public GatewayStartupHelper addServerUrl(String host, int port, String brokerName) {
        serverUrls.put(URL.serverUrl(host, port, brokerName), new DefaultGatewayProcessor());
        return this;
    }

    public GatewayStartupHelper addClientUrl(String brokerName) {
        clientUrls.add(URL.clientUrl(brokerName));
        return this;
    }

    public GatewayStartupHelper createControllers() {
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

        gatewayController = new GatewayController(gatewayConfig);

        return this;
    }

    public GatewayStartupHelper start() {
        if (!init) {
            createControllers();
        }
        brokerController.start();
        gatewayController.start();

        after();

        return this;
    }

    private void after() {
        for (Map.Entry<URL, NettyRequestProcessor> serverUrl : serverUrls.entrySet()) {
            ((DefaultGatewayProcessor) serverUrl.getValue()).registerBrokerController(brokerController);
            ((DefaultGatewayProcessor) serverUrl.getValue()).registerGatewayController(gatewayController);
        }
        ((ClientProcessor) gatewayConfig.getGatewayProcessor()).registerBrokerController(brokerController);
        ((ClientProcessor) gatewayConfig.getGatewayProcessor()).registerGatewayController(gatewayController);
    }

    public void addGatewayRegister(String host, int port, String gatewayName) {
        gatewayConfig.setHost(host);
        gatewayConfig.setPort(port);
        gatewayConfig.setRegisterUrl(URL.serverUrl(host, port, gatewayName));
    }

    public void addGatewaySubscribe(String gatewayName) {
        gatewayConfig.setSubsribeUrl(URL.clientUrl(gatewayName));
    }

    public GatewayController getGatewayController() {
        return this.gatewayController;
    }

    public BrokerController getBrokerController() {
        return this.brokerController;
    }
}
