package com.framework.gateway.processor;

import com.framework.broker.processor.DefaultRemotingProcessor;
import com.framework.common.bean.NetMessage;
import com.framework.common.business.utils.ProtostuffUtil;
import com.framework.common.command.GatewayCommand;
import com.framework.gateway.network.GatewayController;
import com.framework.remoting.exception.RemotingCommandException;
import com.framework.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ZhangGe on 2017/5/2.
 */
public class DefaultGatewayProcessor extends DefaultRemotingProcessor {

    private GatewayController gatewayController;

    public DefaultGatewayProcessor registerGatewayController(GatewayController gatewayController) {
        this.gatewayController = gatewayController;
        return this;
    }

    @Override
    protected RemotingCommand processRequest0(ChannelHandlerContext ctx, RemotingCommand request) throws RemotingCommandException {

        int code = request.getCode();

        System.out.println("========================");
        System.out.println(request);
        System.out.println("========================");

        System.out.println(gatewayController);
        try {
            byte[] body = request.getBody();
            NetMessage deserialize = ProtostuffUtil.deserialize(body, NetMessage.class);
            deserialize.setCommand(GatewayCommand.GAME_START);
            gatewayController.sendToClient(deserialize);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (GatewayCommand.GAME_START == code) {
            log.info("GAME_START request : {}, ", request);
        }  else {
            log.info("request : {}, ", request);
        }
        return request;
    }
}
