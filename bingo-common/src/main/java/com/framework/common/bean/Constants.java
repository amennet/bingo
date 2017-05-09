package com.framework.common.bean;


import io.netty.util.AttributeKey;

/**
 * Created by ZhangGe on 2017/4/4.
 */
public class Constants {

    public static final int HEADER_SIZE = 8;

    public final static AttributeKey<NettyChannel> NETTY_CHANNEL_USER_ID = AttributeKey.valueOf("userId");

}
