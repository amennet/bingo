package com.framework.broker.processor;

import com.framework.broker.BrokerController;
import com.framework.remoting.common.RemotingHelper;
import com.framework.remoting.exception.RemotingCommandException;
import com.framework.remoting.netty.NettyRequestProcessor;
import com.framework.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ZhangGe on 2017/4/29.
 */
public abstract class DefaultRemotingProcessor implements NettyRequestProcessor {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected BrokerController brokerController;

    public DefaultRemotingProcessor registerBrokerController(BrokerController brokerController) {
        this.brokerController = brokerController;
        return this;
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws RemotingCommandException {
        /*if (log.isDebugEnabled()) {
            log.debug("receive request, {} {} {}",
                    request.getCode(),
                    RemotingHelper.parseChannelRemoteAddr(ctx.channel()),
                    request);
        }*/
        return processRequest0(ctx, request);
    }

    protected abstract RemotingCommand processRequest0(ChannelHandlerContext ctx, RemotingCommand request) throws RemotingCommandException;

    @Override
    public boolean rejectRequest() {
        return false;
    }
}
