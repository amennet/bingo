package com.framework.gateway;

import com.framework.broker.BrokerConfig;
import com.framework.broker.BrokerController;
import com.framework.broker.cluster.hastrategy.FailfastHaStrategy;
import com.framework.broker.cluster.loadbalance.RoundRobinLoadBalance;
import com.framework.broker.processor.DefaultRemotingProcessor;
import com.framework.common.register.URL;
import com.framework.register.support.ZookeeperRegisterHelper;
import com.framework.register.support.ZookeeperRegistryFactory;
import com.framework.register.zookeeper.zkclient.ZkclientZookeeperTransporter;
import com.framework.remoting.CommandCustomHeader;
import com.framework.remoting.RemotingClient;
import com.framework.remoting.exception.RemotingCommandException;
import com.framework.remoting.protocol.RemotingCommand;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangGe on 2017/5/1.
 */
public class GatewayStartupBackup {

    public static void main(String[] args) {
        BrokerConfig brokerConfig = new BrokerConfig();


        brokerConfig.setHaStrategy(new FailfastHaStrategy());
        brokerConfig.setLoadBalance(new RoundRobinLoadBalance());

        URL registryUrl = URL.registryUrl("192.168.123.181:2181", "192.168.123.181.2183");

        ZookeeperRegistryFactory zookeeperRegistryFactory = new ZookeeperRegistryFactory(new ZkclientZookeeperTransporter());

        ZookeeperRegisterHelper zookeeperRegisterHelper = new ZookeeperRegisterHelper(registryUrl, zookeeperRegistryFactory);
        brokerConfig.setZookeeperRegisterHelper(zookeeperRegisterHelper);

        URL regGateway1 = URL.serverUrl("127.0.0.1", 1234, "gateway");
        URL clientUrl = URL.clientUrl("gateway");


        Map<URL, DefaultRemotingProcessor> registerMap = new HashMap<>();
        registerMap.put(regGateway1, new GatewayRemotingProcessor());

        List<URL> subscribeUrls = new ArrayList<>();
        subscribeUrls.add(clientUrl);

        brokerConfig.setRegisterUrls(registerMap);
        brokerConfig.setSubsribeUrls(subscribeUrls);

        Map<Integer, String> requestBrokerName = new HashMap<>();

        requestBrokerName.put(1, "gateway");
        requestBrokerName.put(2, "gateway");

        brokerConfig.setRequestBrokerName(requestBrokerName);

        BrokerController brokerController = new BrokerController(brokerConfig);

        brokerController.start();


        ///////////////////////////////test network///////////////////////////////////


        RemotingCommand requestCommand = RemotingCommand.createRequestCommand(1, new CommandCustomHeader() {
            @Override
            public void checkFields() throws RemotingCommandException {

            }
        });

        requestCommand.setBody("123456789".getBytes());

        for (int i = 0; i < 10000; i++) {

            System.out.println("2---------------------------------");

            // requestCommand.markResponseType();  // 不能用，会超时返回  ,用默认的

            RemotingCommand remotingCommand1 = brokerController.clusterRequest(requestCommand);

            System.out.println(remotingCommand1);

            System.out.println("3---------------------------------");

            requestCommand.markOnewayRPC();

            RemotingCommand remotingCommand2 = brokerController.clusterRequest(requestCommand);

            System.out.println(remotingCommand2);

            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

            System.out.println("===============================" + i +"============================");
        }

    }

}
