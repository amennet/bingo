/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.framework.gateway.network.client;

import com.framework.gateway.exception.GatewayRuntimeException;
import com.framework.remoting.common.Pair;
import com.framework.remoting.common.RemotingHelper;
import com.framework.remoting.common.RemotingUtil;
import io.netty.channel.Channel;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientManager {
    private static final Logger log = LoggerFactory.getLogger(ClientManager.class);
    private static final long CHANNEL_EXPIRED_TIMEOUT = 1000 * 120;
    private final ConcurrentHashMap<String/* userId */, Pair<ClientChannelInfo, GameChannelInfo>> clientInfoTable = new ConcurrentHashMap<>(8192);
    private final List<ClientIdChangeListener> clientIdChangeListeners;

    public ClientManager(final List<ClientIdChangeListener> clientIdChangeListeners) {
        this.clientIdChangeListeners = clientIdChangeListeners;
    }

    public ClientChannelInfo findChannel(final String userId) {
        return this.clientInfoTable.get(userId).getObject1();
    }

    public boolean addGameChannel(final String userId, GameChannelInfo gameChannelInfo) {
        boolean updated = false;
        Pair<ClientChannelInfo, GameChannelInfo> infoOld = this.clientInfoTable.get(userId);
        if (null == infoOld || null == infoOld.getObject1()) {
            log.error("不存在的用户，不应该有Game连接，请检查：{}, {}", userId, gameChannelInfo);
            throw new GatewayRuntimeException("不存在的用户，不应该有Game连接，请检查: " + userId + gameChannelInfo);
        }
        if (infoOld.getObject2() == null) {
            infoOld.setObject2(gameChannelInfo);
            Pair<ClientChannelInfo, GameChannelInfo> prev = this.clientInfoTable.put(userId, infoOld);
            log.info("new consumer connected, clientId: {} channel: {}", userId, infoOld);
            updated = true;
        } else {
            if (!infoOld.getObject1().getChannel().equals(gameChannelInfo.getChannel())) {
                log.error("[BUG] consumer channel exist in broker, but clientId not equal. clientId: {} OLD: {} NEW: {} ",
                        userId,
                        infoOld.toString(),
                        gameChannelInfo.toString());
                infoOld.setObject2(gameChannelInfo);
                this.clientInfoTable.put(userId, infoOld);
            }
        }

        if (updated) {
            long lastUpdateTimestamp = System.currentTimeMillis();
            infoOld.getObject1().setLastUpdateTimestamp(lastUpdateTimestamp);
        }
        // TODO 为何不是infoNew

        return updated;
    }

    public void doChannelCloseEvent(final String remoteAddr, final Channel channel) {
        Iterator<Entry<String, Pair<ClientChannelInfo, GameChannelInfo>>> it = this.clientInfoTable.entrySet().iterator();
        String userId = null;
        while (it.hasNext()) {
            Entry<String, Pair<ClientChannelInfo, GameChannelInfo>> next = it.next();
            Pair<ClientChannelInfo, GameChannelInfo> info = next.getValue();
            if (channel.equals(info.getObject1().getChannel())) {
                userId = next.getKey();
                break;
            }
        }
        Pair<ClientChannelInfo, GameChannelInfo> removed = null;
        if (userId != null) {
            removed = clientInfoTable.remove(userId);
        }
        if (removed != null) {
            log.info("unregister consumer ok, no any connection, and remove client info table, {}",
                    removed);
            if (clientIdChangeListeners != null) {
                for (ClientIdChangeListener clientIdChangeListener : clientIdChangeListeners) {
                    clientIdChangeListener.clientIdChanged(userId, removed.getObject2().getChannel());
                }
            }
        }
    }

    public boolean registerConsumer(final String userId, final ClientChannelInfo clientChannelInfo) {
        return updateChannel(userId, clientChannelInfo);
    }

    private boolean updateChannel(final String userId, final ClientChannelInfo infoNew) {
        boolean updated = false;
        Pair<ClientChannelInfo, GameChannelInfo> infoOld = this.clientInfoTable.get(userId);
        if (null == infoOld) {
            infoOld = new Pair<>(infoNew, null);
            Pair<ClientChannelInfo, GameChannelInfo> prev = this.clientInfoTable.put(userId, infoOld);
            if (null == prev) {
                log.info("new consumer connected, clientId: {} channel: {}", userId, infoNew);
                updated = true;
            }
        } else {
            if (!infoOld.getObject1().getChannel().equals(infoNew.getChannel())) {
                log.error("[BUG] consumer userId exist in broker, but channel not equal. clientId: {} OLD: {} NEW: {} ",
                        userId,
                        infoOld.toString(),
                        infoNew.toString());
                infoOld.setObject1(infoNew);
                this.clientInfoTable.put(userId, infoOld);
            }
        }

        if (updated) {
            long lastUpdateTimestamp = System.currentTimeMillis();
            infoOld.getObject1().setLastUpdateTimestamp(lastUpdateTimestamp);
        }
        // TODO 为何不是infoNew

        return updated;
    }

    public void unregisterConsumer(final String userId, final ClientChannelInfo clientChannelInfo) {
        Pair<ClientChannelInfo, GameChannelInfo> infoOld = this.clientInfoTable.get(userId);
        if (null != infoOld && infoOld.getObject1() != null) {
            Pair<ClientChannelInfo, GameChannelInfo> oldInfo = unregisterChannel(userId, clientChannelInfo);
            if (clientInfoTable.isEmpty()) {
                log.info("unregister consumer ok, no any connection, {}", clientInfoTable);
            }
            for (ClientIdChangeListener clientIdChangeListener : clientIdChangeListeners) {
                clientIdChangeListener.clientIdChanged(userId, oldInfo.getObject2().getChannel());
            }
        }
    }

    public Pair<ClientChannelInfo, GameChannelInfo> unregisterChannel(final String userId, final ClientChannelInfo clientChannelInfo) {
        Pair<ClientChannelInfo, GameChannelInfo> old = this.clientInfoTable.remove(clientChannelInfo.getTopic());
        if (old != null) {
            log.info("unregister a consumer[{}] from consumerGroupInfo {}", userId, old.toString());
        }
        return old;
    }

    public void scanNotActiveChannel() {
        Iterator<Entry<String, Pair<ClientChannelInfo, GameChannelInfo>>> it = this.clientInfoTable.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Pair<ClientChannelInfo, GameChannelInfo>> nextChannel = it.next();
            Pair<ClientChannelInfo, GameChannelInfo> clientInfo = nextChannel.getValue();
            long diff = System.currentTimeMillis() - clientInfo.getObject1().getLastUpdateTimestamp();
            if (diff > CHANNEL_EXPIRED_TIMEOUT) {
                log.warn(
                        "SCAN: remove expired channel from ClientManager consumerTable. channel={}, ClientChannelInfo={}",
                        RemotingHelper.parseChannelRemoteAddr(clientInfo.getObject1().getChannel()), clientInfo.getObject1());
                RemotingUtil.closeChannel(clientInfo.getObject1().getChannel());
                this.clientInfoTable.remove(clientInfo.getObject1().getTopic());
                for (ClientIdChangeListener clientIdChangeListener : clientIdChangeListeners) {
                    clientIdChangeListener.clientIdChanged(clientInfo.getObject1().getTopic(), clientInfo.getObject2().getChannel());
                }
            }
        }
    }

    public void shutdown() {
        Iterator<Entry<String, Pair<ClientChannelInfo, GameChannelInfo>>> it = this.clientInfoTable.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Pair<ClientChannelInfo, GameChannelInfo>> nextChannel = it.next();
            Pair<ClientChannelInfo, GameChannelInfo> clientChannelInfo = nextChannel.getValue();
            clientChannelInfo.getObject1().getChannel().close();
            clientChannelInfo.getObject2().getChannel().close();
        }
        this.clientInfoTable.clear();
    }
}
