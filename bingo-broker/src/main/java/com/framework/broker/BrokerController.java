package com.framework.broker;

import com.framework.broker.cluster.HaStrategy;
import com.framework.broker.cluster.LoadBalance;
import com.framework.common.api.INotify;
import com.framework.common.exception.BingoException;
import com.framework.common.register.Constants;
import com.framework.common.register.URL;
import com.framework.common.utils.StringUtils;
import com.framework.register.NotifyListener;
import com.framework.register.support.ZookeeperRegisterHelper;
import com.framework.remoting.RemotingClient;
import com.framework.remoting.RemotingServer;
import com.framework.remoting.exception.RemotingConnectException;
import com.framework.remoting.exception.RemotingSendRequestException;
import com.framework.remoting.exception.RemotingTimeoutException;
import com.framework.remoting.exception.RemotingTooMuchRequestException;
import com.framework.remoting.netty.*;
import com.framework.remoting.protocol.RemotingCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ZhangGe on 2017/4/28.
 */
public class BrokerController implements NotifyListener {
    private static final Logger log = LoggerFactory.getLogger(BrokerController.class);

    private long timeoutMillisSync = 20000;
    private long timeoutMillisOneway = 1000;

    private final BrokerConfig brokerConfig;

    private final ZookeeperRegisterHelper zookeeperRegisterHelper;
    private final ExecutorFactory executorFactory;

    private final ConcurrentMap<URL, RemotingServer> serverMap;
    private volatile ConcurrentMap<String/* brokerName */, Set<URL>> connected;
    private RemotingClient client;
    private INotify iNotify;

    private NotifyListener notifyListener;
    private LoadBalance loadBalance;
    private HaStrategy haStrategy;
    private Map<Integer/* reqeustCode */, String/* brokerName */> requestBrokerName;
    private boolean dontStartNettyClient;

    private boolean init;

    public BrokerController(BrokerConfig brokerConfig) {
        this.brokerConfig = brokerConfig;
        this.executorFactory = new ExecutorFactory();
        this.serverMap = new ConcurrentHashMap<>();
        this.connected = new ConcurrentHashMap<>();
        this.zookeeperRegisterHelper = brokerConfig.getZookeeperRegisterHelper();
        this.iNotify = brokerConfig.getiNotify();
    }

    private void init() {
        this.notifyListener = this;
        this.loadBalance = brokerConfig.getLoadBalance();
        this.haStrategy = brokerConfig.getHaStrategy();
        this.requestBrokerName = brokerConfig.getRequestBrokerName();
        this.dontStartNettyClient = brokerConfig.isDontStartNettyClient();
    }

    public void start() {
        if (!init) {
            init();
            init = true;
        }

        Map<URL, NettyRequestProcessor> registerUrls = brokerConfig.getRegisterUrls();
        List<URL> subsribeUrls = brokerConfig.getSubsribeUrls();

        // 启动注册服务器
        if (registerUrls != null && registerUrls.size() > 0) {
            for (Map.Entry<URL, NettyRequestProcessor> url : registerUrls.entrySet()) {
                NettyServerConfig nettyServerConfig = new NettyServerConfig();
                nettyServerConfig.setHostname(url.getKey().getHost());
                nettyServerConfig.setListenPort(url.getKey().getPort());
                RemotingServer remotingServer = new NettyRemotingServer(nettyServerConfig);
                remotingServer.registerDefaultProcessor(
                        url.getValue(),
                        executorFactory.get());

                remotingServer.start();

                registerServer(url.getKey());

                serverMap.put(url.getKey(), remotingServer);
            }

        }

        // 启动客户端，拉取客户端
        if (subsribeUrls != null && subsribeUrls.size() > 0) {
            if (!dontStartNettyClient) {
                NettyClientConfig nettyClientConfig = new NettyClientConfig();
                RemotingClient remotingClient = new NettyRemotingClient(nettyClientConfig);

                remotingClient.start();
                this.client = remotingClient;
            }

            for (URL subscibeUrl : subsribeUrls) {
                subscribe(subscibeUrl, notifyListener);
            }

        }

    }

    public RemotingCommand clusterRequest(RemotingCommand request) {
        if (request.isResponseType()) {
            log.error("request responseType is wrong, isResponseType : {}, flag : {}", request.isResponseType(), request.getFlag());
        }

        HashMap<String, String> extFields = request.getExtFields();

        String dst = null;
        if (extFields != null && extFields.size() > 0) {
            dst = extFields.get("dst");  // 127.0.0.1:1234
        }
        if (StringUtils.isBlank(dst)) {
            synchronized (this) {
                int code = request.getCode();
                String brokerName = requestBrokerName.get(code);
                Set<URL> urls = connected.get(brokerName);
                if (urls == null || urls.size() <= 0) {
                    log.error("dst is not exist url set : {}, {}, {}, {}", urls, connected, new Date(), brokerName);
                    return null;  // todo 封装报错remotingCommand
                }
                URL url = loadBalance.select(urls, request);
                dst = url.getAddress();
            }
        }

        RemotingCommand response = null;
        try {
            if (request.isOnewayRPC()) {
                client.invokeOneway(dst, request, timeoutMillisOneway);
            } else {
                response = client.invokeSync(dst, request, timeoutMillisSync);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingConnectException e) {
            e.printStackTrace();
        } catch (RemotingTooMuchRequestException e) {
            e.printStackTrace();
        } catch (RemotingTimeoutException e) {
            e.printStackTrace();
        } catch (RemotingSendRequestException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void registerServer(URL url) {
        zookeeperRegisterHelper.register(url);
    }

    private void subscribe(URL url, NotifyListener notifyListener) {
        zookeeperRegisterHelper.subscribe(url, notifyListener);
    }

    private void shutdownServer(URL url) {
        if (url == null) {
            log.warn("shutdown server url is null");
            return;
        }
        zookeeperRegisterHelper.unRegister(url);
        RemotingServer remotingServer = serverMap.remove(url);
        if (remotingServer == null) {
            log.warn("server is not exist : {}", url);
        }
        remotingServer.shutdown();
        log.info("shutdown server success ... serverUrl : {}", url);
    }

    private void shutdownClient(String brokerName) {
        Set<URL> urlSet = connected.remove(brokerName);
        for (URL url : urlSet) {
            zookeeperRegisterHelper.unSubscribe(url, notifyListener);
        }
        if (connected == null || connected.size() <= 0) {
            connected.clear();
            client.shutdown();
        }
        log.info("shutdown client success ... brokerName : {}", brokerName);
    }

    private void shutdownClient() {
        for (String brokerName : connected.keySet()) {
            shutdownClient(brokerName);
        }
        log.info("shutdown client success ... ");
    }

    private void shutdown() {
        for (URL url : serverMap.keySet()) {
            shutdownServer(url);
        }
        shutdownClient();
        log.info("shutdown all success ...");
    }

    @Override
    public void notify(List<URL> urls) {
        log.info("{} start urls {}, {}", new Date(), urls, connected);
        Set<URL> connecting = new HashSet<>();
        for (URL url : urls) {
            if (url.getProtocol().equals(Constants.EMPTY_PROTOCOL)) {
                connected.remove(url.getServiceInterface());
                continue;
            }
            connecting = insertConnect(connecting, url);
        }
        connected.put(urls.get(0).getServiceInterface(), connecting);
        log.info("{} now connected : {}", new Date(), urls, connected);
        if (iNotify != null) {
            iNotify.notify(urls);
        }
    }

    private Set<URL> insertConnect(Set<URL> connecting, URL url) {
        if (connecting == null || connecting.size() <= 0) {
            connecting = new HashSet<>();
        }
        connecting.add(url);
        return connecting;
    }

    public RemotingClient getRemotingClient() {
        return client;
    }

    public URL getLoadBalanceUrl(String brokerName) {
        return getLoadBalanceUrl(getConnected(brokerName));
    }

    public URL getLoadBalanceUrl(Set<URL> urls) {
        if (urls == null || urls.size() <= 0) {
            throw new BingoException("urls 不能为空");
        }
        return loadBalance.select(urls, null);
    }

    public Set<URL> getConnected(String brokerName) {
        return connected.get(brokerName);
    }

    public Map<String, Set<URL>> getConnected() {
        return connected;
    }
}
