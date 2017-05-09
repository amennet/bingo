package com.framework.gateway.network.client;

import io.netty.channel.Channel;

import java.util.List;

public interface ClientIdChangeListener {
    void clientIdChanged(final String clientId, final Channel channel);
}
