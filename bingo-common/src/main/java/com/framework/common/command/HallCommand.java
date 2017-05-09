package com.framework.common.command;

/**
 * Created by ZhangGe on 2017/5/5.
 */
public class HallCommand extends AbstractCommand{

    public HallCommand(String brokerName) {
        super(brokerName);
    }

    // 斗地主命令

    public final static int C_CREATE_ROOM       = 41003;    //创建房间
    public final static int S_CREATE_ROOM       = 41004;

    public final static int C_RECONNECT_ROOM    = 41005;    //重连房间
    public final static int S_RECONNECT_ROOM    = 41006;

    public final static int C_ENTER_ROOM        = 30003;    //进入房间
    public final static int S_ENTER_ROOM        = 30004;

    public final static int C_ENTER_ROOM_RANDOM = 30013;    //进入随机房间
    public final static int S_ENTER_ROOM_RANDOM = 30014;

    public final static int C_EXIT_ROOM         = 40005;    //退出房间
    public final static int S_EXIT_ROOM         = 40006;

    public final static int C_CONTINUE          = 40007;    //继续游戏
    public final static int S_CONTINUE          = 40008;

    public final static int C_PULL_CARD         = 40011;    //出牌
    public final static int S_PULL_CARD         = 40012;

}

