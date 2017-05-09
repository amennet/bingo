package com.framework.gateway.processor;

import com.framework.broker.rpc.RpcClient;
import com.framework.common.bean.NetMessage;
import com.framework.common.bean.enums.ErrorCode;
import com.framework.common.business.utils.ProtostuffUtil;
import com.framework.common.command.GameCommand;
import com.framework.common.command.HallCommand;
import com.framework.common.command.SystemCommand;
import com.framework.gateway.exception.GatewayRuntimeException;
import com.framework.remoting.CommandCustomHeader;
import com.framework.remoting.exception.RemotingCommandException;
import com.framework.remoting.protocol.RemotingCommand;
import gameserver.database.all.model.User;
import gameserver.login.provider.IConnectProvider;
import gameserver.login.provider.ILoginProvider;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ZhangGe on 2017/5/3.
 */
public class ClientProcessor extends ClientManageProcessor {
    @Override
    protected NetMessage processRequest0(ChannelHandlerContext ctx, NetMessage request) {
        int command = request.getCommand();

        System.out.println(request);

        if (command == SystemCommand.CONNECT) {
            return connect(ctx, request);
        } else if (command == SystemCommand.REFRESH) {
            return refresh(ctx, request);
        } else if (command == SystemCommand.PHONECODE) {
            return phoneCode(ctx, request);
        } else if (command == SystemCommand.VALIDATEPHONECODE) {
            return validatePhoneCode(ctx, request);
        } else if (command == SystemCommand.LOGIN) {
            return login(ctx, request);
        } else if (command == SystemCommand.REGISTER) {
            return register(ctx, request);
        } else if (command == SystemCommand.LOGOUT) {
            return logout(ctx, request);
        } else if (command == HallCommand.C_CREATE_ROOM) {
            createRoom(ctx, request);
        } else if (command == HallCommand.C_ENTER_ROOM) {
            enterRoom(ctx, request);
        } else if (command == HallCommand.C_ENTER_ROOM_RANDOM) {
            enterRoomRandom(ctx, request);
        } else if (command == GameCommand.C_CALL_LORD) {
            callLord(ctx, request);
        } else {
            throw new GatewayRuntimeException("没有此命令");
        }
        return null;
    }

    private NetMessage connect(ChannelHandlerContext ctx, NetMessage request) {
        String topic = request.getTopic();
        IConnectProvider iConnectProvider = RpcClient.getRefer(IConnectProvider.class);
        boolean result = iConnectProvider.checkTopic(topic);
        if (result) {
            return NetMessage.success();
        } else {
            return NetMessage.fail()
                    .setErrorCode(ErrorCode.CHECK_TOPIC.getErrorCode())
                    .setErrorMSG(ErrorCode.CHECK_TOPIC.getErrorMSG());
        }
    }

    private NetMessage refresh(ChannelHandlerContext ctx, NetMessage request) {
        String topic = request.getTopic();
        IConnectProvider iConnectProvider = RpcClient.getRefer(IConnectProvider.class);
        iConnectProvider.refreshTopic(topic);
        return NetMessage.success();
    }

    private NetMessage phoneCode(ChannelHandlerContext ctx, NetMessage request) {
        String phone = (String) request.get("phone");
        ILoginProvider iLoginProvider = RpcClient.getRefer(ILoginProvider.class);
        iLoginProvider.getPhoneCode(phone);
        return NetMessage.success();
    }

    private NetMessage validatePhoneCode(ChannelHandlerContext ctx, NetMessage request) {
        String phone = (String) request.get("phone");
        String code = (String) request.get("code");
        ILoginProvider iLoginProvider = RpcClient.getRefer(ILoginProvider.class);
        iLoginProvider.validateCode(phone, code);
        return NetMessage.success();
    }

    private NetMessage login(ChannelHandlerContext ctx, NetMessage request) {
        String topic = request.getTopic();
        String phone = (String) request.get("phone");
        String code = (String) request.get("code");
        String url = (String) request.get("url");
        ILoginProvider iLoginProvider = RpcClient.getRefer(ILoginProvider.class);
        User user = iLoginProvider.login(topic, phone, code, url);
        return NetMessage.success().setObject(user);
    }

    private NetMessage register(ChannelHandlerContext ctx, NetMessage request) {
        String topic = request.getTopic();
        String phone = (String) request.get("phone");
        String code = (String) request.get("code");
        String url = (String) request.get("url");
        ILoginProvider iLoginProvider = RpcClient.getRefer(ILoginProvider.class);
        User user = iLoginProvider.register(topic, phone, code, url);
        return NetMessage.success().setObject(user);
    }

    private NetMessage logout(ChannelHandlerContext ctx, NetMessage request) {
        String topic = request.getTopic();
        String userId = String.valueOf(request.getUserId());
        ILoginProvider iLoginProvider = RpcClient.getRefer(ILoginProvider.class);
        iLoginProvider.logout(topic, userId);
        return NetMessage.success();
    }

    private void createRoom(ChannelHandlerContext ctx, NetMessage request) {
        RemotingCommand requestCommand = RemotingCommand.createRequestCommand(HallCommand.C_CREATE_ROOM, new CommandCustomHeader() {
            @Override
            public void checkFields() throws RemotingCommandException {

            }
        });
        requestCommand.setBody(ProtostuffUtil.serialize(request));
        requestCommand.markOnewayRPC();
        brokerController.clusterRequest(requestCommand);
    }

    private void enterRoom(ChannelHandlerContext ctx, NetMessage request) {
        RemotingCommand requestCommand = RemotingCommand.createRequestCommand(HallCommand.C_ENTER_ROOM, new CommandCustomHeader() {
            @Override
            public void checkFields() throws RemotingCommandException {

            }
        });
        requestCommand.setBody(ProtostuffUtil.serialize(request));
        requestCommand.markOnewayRPC();
        brokerController.clusterRequest(requestCommand);
    }

    private void enterRoomRandom(ChannelHandlerContext ctx, NetMessage request) {
        RemotingCommand requestCommand = RemotingCommand.createRequestCommand(HallCommand.C_ENTER_ROOM_RANDOM, new CommandCustomHeader() {
            @Override
            public void checkFields() throws RemotingCommandException {

            }
        });
        requestCommand.setBody(ProtostuffUtil.serialize(request));
        requestCommand.markOnewayRPC();
        brokerController.clusterRequest(requestCommand);
    }

    private void callLord(ChannelHandlerContext ctx, NetMessage request) {
        RemotingCommand requestCommand = RemotingCommand.createRequestCommand(GameCommand.C_CALL_LORD, new CommandCustomHeader() {
            @Override
            public void checkFields() throws RemotingCommandException {

            }
        });
        requestCommand.setBody(ProtostuffUtil.serialize(request));
        requestCommand.markOnewayRPC();
        brokerController.clusterRequest(requestCommand);
    }

}
