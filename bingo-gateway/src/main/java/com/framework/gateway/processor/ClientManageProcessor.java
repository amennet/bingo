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
package com.framework.gateway.processor;

import com.framework.broker.BrokerController;
import com.framework.common.bean.NetMessage;
import com.framework.common.command.SystemCommand;
import com.framework.gateway.network.GatewayController;
import com.framework.gateway.network.client.ClientChannelInfo;
import com.framework.remoting.common.RemotingHelper;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ClientManageProcessor implements GatewayProcessor {
    private static final Logger log = LoggerFactory.getLogger(ClientManageProcessor.class);

    protected GatewayController gatewayController;

    protected BrokerController brokerController;

    public GatewayProcessor registerGatewayController(GatewayController gatewayController) {
        this.gatewayController = gatewayController;
        return this;
    }

    public GatewayProcessor registerBrokerController(BrokerController brokerController) {
        this.brokerController = brokerController;
        return this;
    }

    @Override
    public NetMessage processRequest(ChannelHandlerContext ctx, NetMessage request) {
        switch (request.getCommand()) {
            case SystemCommand.HEART_BEAT:
                return this.heartBeat(ctx, request);
            case SystemCommand.UNREGISTER_CLIENT:
                return this.unregisterClient(ctx, request);
            default:
                break;
        }
        return processRequest0(ctx, request);
    }

    protected abstract NetMessage processRequest0(ChannelHandlerContext ctx, NetMessage request);

    @Override
    public boolean rejectRequest() {
        return false;
    }

    public NetMessage heartBeat(ChannelHandlerContext ctx, NetMessage request) {
        NetMessage response = NetMessage.success(request.getUserId());
        ClientChannelInfo clientChannelInfo = new ClientChannelInfo(
                ctx.channel(),
                request.getTopic(),
                null,
                0
        );

        boolean changed = this.gatewayController.getClientManager().registerConsumer(
                clientChannelInfo.getTopic(),
                clientChannelInfo);

        if (changed) {
            log.info("registerConsumer info changed {} {}",
                    clientChannelInfo.toString(),
                    RemotingHelper.parseChannelRemoteAddr(ctx.channel())
            );
        }

        return response;
    }

    public NetMessage unregisterClient(ChannelHandlerContext ctx, NetMessage request) {
        NetMessage response = NetMessage.success(request.getUserId());

        ClientChannelInfo clientChannelInfo = new ClientChannelInfo(
                ctx.channel(),
                request.getTopic(),
                null,
                0
        );
        this.gatewayController.getClientManager().unregisterConsumer(clientChannelInfo.getTopic(), clientChannelInfo);

        return response;
    }
}
