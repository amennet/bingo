package com.framework.gateway.network.client;

import com.framework.common.register.URL;
import com.framework.gateway.processor.GatewayProcessor;
import com.framework.register.NotifyListener;
import com.framework.register.support.ZookeeperRegisterHelper;

import java.util.List;

/**
 * Created by ZhangGe on 2017/5/2.
 */
public class GatewayConfig {

    private String host;
    private int port;

    private URL registerUrl;
    private URL subsribeUrl;

    private NotifyListener notifyListener;
    private GatewayProcessor gatewayProcessor;

    private List<ClientIdChangeListener> clientIdChangeListeners;
    private ZookeeperRegisterHelper zookeeperRegisterHelper;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {

        this.port = port;
    }

    public List<ClientIdChangeListener> getClientIdChangeListeners() {
        return clientIdChangeListeners;
    }

    public void setClientIdChangeListeners(List<ClientIdChangeListener> clientIdChangeListeners) {
        this.clientIdChangeListeners = clientIdChangeListeners;
    }

    public ZookeeperRegisterHelper getZookeeperRegisterHelper() {
        return zookeeperRegisterHelper;
    }

    public void setZookeeperRegisterHelper(ZookeeperRegisterHelper zookeeperRegisterHelper) {
        this.zookeeperRegisterHelper = zookeeperRegisterHelper;
    }

    public NotifyListener getNotifyListener() {
        return notifyListener;
    }

    public void setNotifyListener(NotifyListener notifyListener) {
        this.notifyListener = notifyListener;
    }

    public GatewayProcessor getGatewayProcessor() {
        return gatewayProcessor;
    }

    public void setGatewayProcessor(GatewayProcessor gatewayProcessor) {
        this.gatewayProcessor = gatewayProcessor;
    }

    public URL getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(URL registerUrl) {
        this.registerUrl = registerUrl;
    }

    public URL getSubsribeUrl() {
        return subsribeUrl;
    }

    public void setSubsribeUrl(URL subsribeUrl) {
        this.subsribeUrl = subsribeUrl;
    }
}


