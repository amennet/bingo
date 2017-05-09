package com.framework.gateway.test;

import com.framework.broker.BrokerController;
import com.framework.broker.processor.DefaultRemotingProcessor;
import com.framework.remoting.exception.RemotingCommandException;
import com.framework.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ZhangGe on 2017/5/1.
 */
public class GatewayRemotingProcessor2 extends DefaultRemotingProcessor{

    @Override
    protected RemotingCommand processRequest0(ChannelHandlerContext ctx, RemotingCommand request) throws RemotingCommandException {
        System.out.println("333333333333333333333333333333");
        System.out.println(request + new String(request.getBody()));
        return request;
    }
}
