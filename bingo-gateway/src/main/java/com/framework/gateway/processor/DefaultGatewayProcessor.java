package com.framework.gateway.processor;

import com.framework.broker.processor.DefaultRemotingProcessor;
import com.framework.common.bean.NetMessage;
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

            NetMessage netMessage = NetMessage.client("123456789").setCommand(1);
            netMessage.setUserId("123456789");
            gatewayController.sendToClient(netMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*if (SystemCommand.PUSH.getCode() == code) {

        } else if (SystemCommand.GETUSER.getCode() == code) {

        } else if (SystemCommand.TEST1.getCode() == code) {

        } else if (SystemCommand.TEST2.getCode() == code) {

        } else if (SystemCommand.TEST3.getCode() == code) {

        } else {
            // processClientRequest();
        }*/
        return request;
    }
}
