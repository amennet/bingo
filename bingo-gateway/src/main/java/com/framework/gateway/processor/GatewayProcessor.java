package com.framework.gateway.processor;

import com.framework.broker.BrokerController;
import com.framework.common.bean.NetMessage;
import com.framework.gateway.network.GatewayController;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ZhangGe on 2017/5/2.
 */
public interface GatewayProcessor {

    NetMessage processRequest(ChannelHandlerContext ctx, NetMessage request)
            throws Exception;

    boolean rejectRequest();
}
