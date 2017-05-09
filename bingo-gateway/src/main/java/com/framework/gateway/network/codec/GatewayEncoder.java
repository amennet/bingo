package com.framework.gateway.network.codec;

import com.framework.common.bean.NetMessage;
import com.framework.common.business.utils.ProtostuffUtil;
import com.framework.common.command.SystemCommand;
import com.framework.gateway.exception.GatewayException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by ZhangGe on 2017/4/4.
 */
public class GatewayEncoder extends MessageToByteEncoder<NetMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, NetMessage netMessage, ByteBuf byteBuf) throws Exception {
        if (netMessage == null) {
            throw new GatewayException("protobufMessage不能为空");
        }

        byte[] data = ProtostuffUtil.serialize(netMessage);

        byteBuf.writeInt(netMessage.getCommand());
        byteBuf.writeInt(data.length);
        if (data.length > 0) {
            byteBuf.writeBytes(data);
        }
    }
}
