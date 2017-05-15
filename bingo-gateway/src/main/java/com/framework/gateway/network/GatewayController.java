package com.framework.gateway.network;

import com.framework.broker.BrokerController;
import com.framework.broker.ExecutorFactory;
import com.framework.common.bean.NetMessage;
import com.framework.common.register.Constants;
import com.framework.common.register.URL;
import com.framework.gateway.exception.GatewayRuntimeException;
import com.framework.gateway.network.client.*;
import com.framework.gateway.network.server.GatewayRemotingConfig;
import com.framework.gateway.network.server.GatewayRemotingServer;
import com.framework.register.NotifyListener;
import com.framework.register.support.ZookeeperRegisterHelper;
import com.framework.remoting.protocol.RemotingCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ZhangGe on 2017/5/2.
 */
public class GatewayController implements NotifyListener {
    private static final Logger log = LoggerFactory.getLogger(BrokerController.class);

    private long timeoutMillisOneway = 1000;
    private final GatewayConfig gatewayConfig;
    private final List<ClientIdChangeListener> clientIdChangeListeners;
    private final ClientManager clientManager;
    private GatewayRemotingServer gatewayRemotingServer;
    private ZookeeperRegisterHelper zookeeperRegisterHelper;
    private final ExecutorFactory executorFactory;
    private NotifyListener notifyListener;
    private volatile Set<URL> connected;

    public GatewayController(GatewayConfig gatewayConfig) {
        this.gatewayConfig = gatewayConfig;
        this.executorFactory = new ExecutorFactory();
        this.clientIdChangeListeners = gatewayConfig.getClientIdChangeListeners();
        this.zookeeperRegisterHelper = gatewayConfig.getZookeeperRegisterHelper();
        this.clientManager = new ClientManager(this.clientIdChangeListeners);
        this.connected = new HashSet<>();
        this.notifyListener = this;
    }

    public void start() {
        GatewayRemotingConfig gatewayRemotingConfig = new GatewayRemotingConfig();
        gatewayRemotingConfig.setHostname(gatewayConfig.getHost());
        gatewayRemotingConfig.setListenPort(gatewayConfig.getPort());
        this.gatewayRemotingServer = new GatewayRemotingServer(
                gatewayRemotingConfig, new ClientHousekeepingService(this));
        this.gatewayRemotingServer.registerDefaultProcessor(
                gatewayConfig.getGatewayProcessor(), executorFactory.get());

        URL registerUrl = gatewayConfig.getRegisterUrl();
        URL subsribeUrl = gatewayConfig.getSubsribeUrl();

        // 启动注册服务器
        this.gatewayRemotingServer.start();
        registerServer(registerUrl);

        // 启动客户端，拉取客户端
        subscribe(subsribeUrl, notifyListener);
    }

    public RemotingCommand sendToClient(NetMessage netMessage) {
        if (netMessage == null) {
            log.error("the message is null");
            throw new GatewayRuntimeException("the message is null");
        }

        // String userId = netMessage.getUserId();
        String userId = netMessage.getTopic();

        RemotingCommand response = null;
        try {
            ClientChannelInfo channel = this.clientManager.findChannel(userId);
            channel.getChannel().writeAndFlush(netMessage);
        } catch (Exception e) {
            log.info("send error {}", netMessage, e);

            throw new GatewayRuntimeException("send error " + netMessage);
        }

        return response;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public GatewayRemotingServer getGatewayRemotingServer() {
        return this.gatewayRemotingServer;
    }

    private void registerServer(URL url) {
        zookeeperRegisterHelper.register(url);
    }

    private void subscribe(URL url, NotifyListener notifyListener) {
        zookeeperRegisterHelper.subscribe(url, notifyListener);
    }


    private void shutdown() {
        gatewayRemotingServer.shutdown();
        clientManager.shutdown();
        executorFactory.shutdown();
        log.info("shutdown all success ...");
    }

    @Override
    public void notify(List<URL> urls) {
        log.info("{} start urls {}, {}", new Date(), urls, connected);
        Set<URL> connecting = new HashSet<>();
        for (URL url : urls) {
            if (url.getProtocol().equals(Constants.EMPTY_PROTOCOL)) {
                connected.remove(url);
                continue;
            }
            connecting = insertConnect(connecting, url);
        }
        connected = connecting;
        log.info("{} now connected : {}", new Date(), urls, connected);
    }

    private Set<URL> insertConnect(Set<URL> connecting, URL url) {
        if (connecting == null || connecting.size() <= 0) {
            connecting = new HashSet<>();
        }
        connecting.add(url);
        return connecting;
    }
}
