package com.framework.gateway;

import com.framework.broker.processor.DefaultRemotingProcessor;
import com.framework.remoting.exception.RemotingCommandException;
import com.framework.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ZhangGe on 2017/5/1.
 */
public class GatewayRemotingProcessor extends DefaultRemotingProcessor{
    @Override
    protected RemotingCommand processRequest0(ChannelHandlerContext ctx, RemotingCommand request) throws RemotingCommandException {
        System.out.println("1111111111111111111111111111111111");
        System.out.println(request);
        return request;
    }
}
