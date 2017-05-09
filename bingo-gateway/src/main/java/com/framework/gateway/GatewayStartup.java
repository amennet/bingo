package com.framework.gateway;

import com.framework.broker.rpc.RpcClient;
import com.framework.common.command.GameCommand;
import com.framework.common.command.HallCommand;
import com.framework.common.command.SystemCommand;
import com.framework.common.command.WorldCommand;
import com.framework.gateway.network.GatewayController;
import com.framework.gateway.test.GatewayRemotingProcessor;
import com.framework.remoting.CommandCustomHeader;
import com.framework.remoting.exception.RemotingCommandException;
import com.framework.remoting.protocol.RemotingCommand;
import gameserver.login.provider.IConnectProvider;
import gameserver.login.provider.ILoginProvider;

/**
 * Created by ZhangGe on 2017/5/1.
 */
public class GatewayStartup {

    public static void main(String[] args) throws InterruptedException {
        String zookeeperAddr = "192.168.123.181:2181";
        GatewayStartupHelper gatewayStartupHelper =
                new GatewayStartupHelper(zookeeperAddr,
                        new SystemCommand("system"),
                        new GameCommand("game"),
                        new WorldCommand("world"),
                        new HallCommand("hall"));

        gatewayStartupHelper.addServerUrl("127.0.0.1", 5678, "gateway");
        gatewayStartupHelper.addClientUrl("game");
        gatewayStartupHelper.addClientUrl("hall");
        gatewayStartupHelper.addClientUrl("world");

        gatewayStartupHelper.addGatewayRegister("127.0.0.1", 1234, "client");
        gatewayStartupHelper.addGatewaySubscribe("client");

        RpcClient.addRefer(IConnectProvider.class, zookeeperAddr);
        RpcClient.addRefer(ILoginProvider.class, zookeeperAddr);

        gatewayStartupHelper.start();

//        IConnectPorvider refer = RpcClient.getRefer(IConnectPorvider.class);
//        refer.checkTopic("123", "123");

       /* RemotingCommand requestCommand = RemotingCommand.createRequestCommand(2007, new CommandCustomHeader() {
            @Override
            public void checkFields() throws RemotingCommandException {

            }
        });

        //Thread.sleep(30000);

        RemotingCommand remotingCommand = gatewayStartupHelper.getBrokerController().clusterRequest(requestCommand);
        remotingCommand.markOnewayRPC();
        System.out.println("---------------------------");
        System.out.println(remotingCommand);
        System.out.println("---------------------------");*/

    }
}
