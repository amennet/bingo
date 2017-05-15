package com.framework.gateway;

import com.framework.broker.rpc.RpcClient;
import com.framework.common.command.*;
import com.framework.common.utils.PropertiesUtil;
import com.framework.gateway.network.GatewayController;
import com.framework.gateway.test.GatewayRemotingProcessor;
import com.framework.remoting.CommandCustomHeader;
import com.framework.remoting.exception.RemotingCommandException;
import com.framework.remoting.protocol.RemotingCommand;
import gameserver.login.provider.IConnectProvider;
import gameserver.login.provider.ILoginProvider;
import gameserver.world.provider.IWorldProvider;

/**
 * Created by ZhangGe on 2017/5/1.
 */
public class GatewayStartup {

    public static void main(String[] args) throws InterruptedException {
        String zookeeperAddr = PropertiesUtil.getPropertyByProperties("application.properties", "zookeeper.address");
        GatewayStartupHelper gatewayStartupHelper =
                new GatewayStartupHelper(zookeeperAddr,
                        new SystemCommand("system"),
                        new GameCommand("game"),
                        new WorldCommand("world"),
                        new HallCommand("hall"));

        String serverAddr = PropertiesUtil.getPropertyByProperties("application.properties", "server.address");
        gatewayStartupHelper.addServerUrl(serverAddr, 5678, "gateway");
        gatewayStartupHelper.addClientUrl("game");
        gatewayStartupHelper.addClientUrl("hall");
        gatewayStartupHelper.addClientUrl("world");

        gatewayStartupHelper.addGatewayRegister(serverAddr, 1234, "client");
        gatewayStartupHelper.addGatewaySubscribe("client");

        RpcClient.addRefer(IConnectProvider.class, zookeeperAddr);
        RpcClient.addRefer(ILoginProvider.class, zookeeperAddr);
        RpcClient.addRefer(IWorldProvider.class, zookeeperAddr);

        gatewayStartupHelper.start();
    }
}
