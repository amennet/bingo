package com.framework.gateway;

import com.framework.broker.BrokerConfig;
import com.framework.broker.BrokerController;
import com.framework.broker.BrokerStartupHelper;
import com.framework.broker.cluster.hastrategy.FailfastHaStrategy;
import com.framework.broker.cluster.loadbalance.RoundRobinLoadBalance;
import com.framework.broker.processor.DefaultRemotingProcessor;
import com.framework.common.command.GatewayCommand;
import com.framework.common.register.URL;
import com.framework.register.support.ZookeeperRegisterHelper;
import com.framework.register.support.ZookeeperRegistryFactory;
import com.framework.register.zookeeper.zkclient.ZkclientZookeeperTransporter;
import com.framework.remoting.CommandCustomHeader;
import com.framework.remoting.exception.RemotingCommandException;
import com.framework.remoting.protocol.RemotingCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangGe on 2017/5/1.
 */
public class GatewayStartup {

    public static void main(String[] args) throws InterruptedException {
        BrokerStartupHelper brokerStartupHelper = new BrokerStartupHelper("192.168.123.181:2181,192.168.123.181:2183",
                GatewayCommand.GETUSER /* 暂时使用枚举，需要传入实例 */);

        brokerStartupHelper.addServerUrl("127.0.0.1", 1234, "gateway", new GatewayRemotingProcessor());
        brokerStartupHelper.addServerUrl("127.0.0.1", 2234, "gateway", new GatewayRemotingProcessor1());
        brokerStartupHelper.addServerUrl("127.0.0.1", 3234, "gateway", new GatewayRemotingProcessor2());
        brokerStartupHelper.addServerUrl("127.0.0.1", 4234, "gateway", new GatewayRemotingProcessor3());

        brokerStartupHelper.addClientUrl("game");
        brokerStartupHelper.addClientUrl("hall");
        brokerStartupHelper.addClientUrl("gateway");

        brokerStartupHelper.start();

        RemotingCommand requestCommand = RemotingCommand.createRequestCommand(1, new CommandCustomHeader() {
            @Override
            public void checkFields() throws RemotingCommandException {

            }
        });

        requestCommand.setBody("123456789".getBytes());

        Thread.sleep(10000);

        for (int i = 0; i < 10000000; i++) {


            System.out.println("2---------------------------------");

            // requestCommand.markResponseType();  // 不能用，会超时返回  ,用默认的

            RemotingCommand remotingCommand1 = brokerStartupHelper.getBrokerController().clusterRequest(requestCommand);

            System.out.println(remotingCommand1);

            System.out.println("3---------------------------------");

            requestCommand.markOnewayRPC();

            RemotingCommand remotingCommand2 = brokerStartupHelper.getBrokerController().clusterRequest(requestCommand);

            System.out.println(remotingCommand2);

            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

            System.out.println("===============================" + i +"============================");
        }
    }
}
