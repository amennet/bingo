package com.framework.gateway.network.codec;

import com.framework.common.bean.Constants;
import com.framework.common.bean.NetMessage;
import com.framework.common.business.utils.ProtostuffUtil;
import com.framework.common.command.SystemCommand;
import com.framework.gateway.exception.GatewayRuntimeException;
import com.framework.gateway.test.CommandType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ZhangGe on 2017/4/4.
 */
public class GatewayDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(GatewayDecoder.class);

    private int maxContentLength = 2048;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < Constants.HEADER_SIZE) {
            return;
        }

        byteBuf.markReaderIndex();

        int command = byteBuf.readInt();
        int length = byteBuf.readInt();

        NetMessage netMessage = null;
        if (length > 0) {
            if (byteBuf.readableBytes() < length) {
                byteBuf.resetReaderIndex();  // 可能是其他消息，直接返回
                return;
            }
            if (length > 0 && length > maxContentLength) {
                logger.warn("NettyDecoder transport data frame length over of limit, size: {}  > {}. remote={} local={}",
                        new Object[]{length, maxContentLength, channelHandlerContext.channel().remoteAddress(), channelHandlerContext.channel().remoteAddress()});
                Exception e = new GatewayRuntimeException(String.format("NettyDecoder transport data frame length over of limit, size: %s > %s ", length, maxContentLength));

                // TODO 写回并抛出异常
                // channel.write(response);
                throw e;
            }
            byte[] data = new byte[length];
            byteBuf.readBytes(data);

            netMessage = ProtostuffUtil.deserialize(data, NetMessage.class);
        }

        if (netMessage != null) {
            list.add(netMessage);
        } else {
            logger.warn("netMessage is null." + command + " " + length);
        }
    }
}
