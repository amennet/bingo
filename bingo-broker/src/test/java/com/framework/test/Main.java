/*
package com.framework.test;

import com.framework.broker.cluster.hastrategy.FailfastHaStrategy;
import com.framework.broker.cluster.loadbalance.RoundRobinLoadBalance;
import com.framework.broker.notify.ClientRegister;
import com.framework.common.register.URL;
import com.framework.register.support.ZookeeperRegistryFactory;
import com.framework.register.zookeeper.curator.CuratorZookeeperTransporter;
import com.framework.remoting.RemotingClient;
import com.framework.remoting.RemotingServer;
import com.framework.remoting.exception.RemotingConnectException;
import com.framework.remoting.exception.RemotingSendRequestException;
import com.framework.remoting.exception.RemotingTimeoutException;
import com.framework.remoting.exception.RemotingTooMuchRequestException;
import com.framework.remoting.protocol.RemotingCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

*/
/**
 * Created by ZhangGe on 2017/4/28.
 *//*

public class Main {

    public static void main(String[] args) throws InterruptedException, RemotingSendRequestException, RemotingTimeoutException, RemotingTooMuchRequestException, RemotingConnectException {

        URL registerUrl = URL.valueOf("192.168.123.181:2181");
        URL serverUrl = URL.valueOf("gateway", "127.0.0.1", 1234, URL.SERVER);
        URL clientUrl = URL.valueOf("gateway", "127.0.0.1", 1234, URL.CLIENT);

        ZookeeperRegistryFactory zookeeperRegistryFactory = new ZookeeperRegistryFactory();
        zookeeperRegistryFactory.setZookeeperTransporter(new CuratorZookeeperTransporter());

        RouteInfoManager routeInfoManager = new RouteInfoManager();

        GatewayRemotingProcessor gatewayRemotingProcessor = new GatewayRemotingProcessor(serverUrl, routeInfoManager);
        List<RemotingProcessor> remotingProcessors = new ArrayList<>();
        remotingProcessors.add(gatewayRemotingProcessor);

        ClientRegister clientRegister = new ClientRegister(serverUrl, routeInfoManager);
        List<ClientRegister> clientRegisters = new ArrayList<>();
        clientRegisters.add(clientRegister);

        RoundRobinLoadBalance roundRobinLoadBalance = new RoundRobinLoadBalance();
        FailfastHaStrategy failfastHaStrategy = new FailfastHaStrategy();

        RemotingConfig remotingConfig = new RemotingConfig();
        remotingConfig.setServerPort(1234);
        remotingConfig.setRegisterUrl(registerUrl);
        remotingConfig.setClientRegisters(clientRegisters);
        remotingConfig.setHaStrategy(failfastHaStrategy);
        remotingConfig.setLoadBalance(roundRobinLoadBalance);
        remotingConfig.setRegistryFactory(zookeeperRegistryFactory);
        remotingConfig.setRouteInfoManager(routeInfoManager);
        remotingConfig.setRemotingProcessors(remotingProcessors);

        InitSupport initSupport = new InitSupport(remotingConfig);
        initSupport.start();

        System.out.println("start complete");

        Set<String> abc = routeInfoManager.getServersByName("zookeeper");

        RemotingClient remotingClient = initSupport.getRemotingClient();
        RemotingServer remotingServer = initSupport.getRemotingServer();

        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setCount(1);
        requestHeader.setMessageTitle("Welcome");
        RemotingCommand request = RemotingCommand.createRequestCommand(0, requestHeader);
        RemotingCommand response = remotingClient.invokeSync("localhost:1234", request, 1000 * 3);
        System.out.println("----------------------------------------------------------------");
        System.out.println(response);


    }
}
*/
