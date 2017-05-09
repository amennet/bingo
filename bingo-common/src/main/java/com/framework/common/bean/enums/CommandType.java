package com.framework.common.bean.enums;/*
package com.framework.gateway.bean.enums;

import java.util.HashMap;
import java.util.Map;

import static gameserver.bean.enums.MessageType.*;

public enum CommandType {

    // TODO 以后用命令大小进行判断，不用标志位

    // 系统命令
    HEARTBEAT(0x00001, NET),  // 心跳

    CONNECT(0x00002, RPC),  // 连接网关

    DISCONNCET(0X00011, RPC), // 用户断开网络连接，暂时不实现，有可能用户存在闪短的情况，不清除topic。

    REFRESH(0x00009, RPC), // 刷新用户连接

    LOGIN(0x00003, RPC), // 登录

    LOGOUT(0x00007, RPC), // 退出登录

    REGISTER(0x00004, RPC), // 注册

    PHONECODE(0x00005, RPC),  // 获取手机验证码

    VALIDATEPHONECODE(0x00006,RPC), // 验证手机验证码

    PUSH(0x00008, RPC), // 测试用命令，实际运行中是服务器对客户端的推送
    PUSHTO(0x00010, NET), // 测试用命令，实际运行中是服务器对客户端的推送

    // 斗地主命令

    C_CREATE_ROOM(0X41003, RPC),    //创建房间
    S_CREATE_ROOM(0X41004, RPC),

    C_RECONNECT_ROOM(0X41005, NET),    //重连房间
    S_RECONNECT_ROOM(0X41006, NET),

    C_ENTER_ROOM(0x30003, RPC),    //进入房间
    S_ENTER_ROOM(0x30004, RPC),

    C_ENTER_ROOM_RANDOM(0x30013, RPC),    //进入随机房间
    S_ENTER_ROOM_RANDOM(0x30014, RPC),

    C_EXIT_ROOM(0x40005, NET),    //退出房间
    S_EXIT_ROOM(0x40006, NET),

    C_CONTINUE(0x40007, NET),    //继续游戏
    S_CONTINUE(0x40008, NET),

    C_CALL_LORD(0x40009, NET),      //叫地主
    S_CALL_LORD(0x4000A, NET),

    C_PULL_CARD(0x4000B, NET),    //出牌
    S_PULL_CARD(0x4000C, NET),

    S_START_GAME(0x4000D, NET),      //开始游戏
    S_DEAL(0x4000E, NET),      //发牌
    S_SCENE(0x40010, NET),      //游戏场景
    S_GAME_END(0x40011, NET),      //游戏结束
    S_THE_WHO(0x40012, NET),      //轮到谁
    S_CALL_LORD_RESULT(0x40013, NET),      //叫地主结果
    S_KICK_GAME(0x40014, NET),      //踢出玩家离开游戏

    C_USER_INFO(0x40015,RPC),    //用户数据
    S_USER_INFO(0x40016,RPC),

    S_NOTIFY_PLAYER_DATA(0x40017, NET),  //通知其他玩家,玩家数数据变化

    C_USE_NOTE_CARD(0x40018, NET),  //打开记牌器界面
    S_USE_NOTE_CARD(0x40019, NET),

    C_CHAT(0x40020, NET),      //聊天
    S_CHAT(0x40021, NET),

    C_AGENT(0x40022, NET), //托管
    S_AGENT(0x40023, NET),

    S_BEISHU(0x40030, NET),

    S_TIME_OUT(0x40031, NET), //超时通知

	*/
/*Global*//*


    C_AUTH_SERVER(0x10007,RPC),     //认证玩家是否可进入该服务器
    S_AUTH_SERVER(0x10008,RPC),
    S_GOLD_NOT_ENOUGH(0x10009, NET),     //游戏中通知钱不够了
    S_GOLD_CHANGE(0x10010, NET),     //游戏中通知金钱变化,
    C_NOT_ADDGOLD(0x10011, NET),     //不充值、领取救济金、购买欢乐豆
    S_ADDGOLD_OVER(0x10012, NET),        //玩家A在游戏中处理完充值

    */
/*hall*//*

    C_UPDATE_USER(0x00020034, NET),        //更改用户信息
    S_UPDATE_USER(0x00020035, NET),
    S_MONEY_UPDATE(0X20091, NET);  // 货币变化

    private int value;

    private MessageType messageType;

    CommandType(int value, MessageType messageType) {
        this.value = value;
        this.messageType = messageType;
    }

    public int getValue() {
        return this.value;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    private static final Map<Integer, CommandType> commandTypes = new HashMap<>();

    private static boolean init = false;

    private static void init() {
        if (init) {
            return;
        }
        for (CommandType commandType : CommandType.values()) {
            commandTypes.put(commandType.getValue(), commandType);
        }
    }

    public static CommandType getCommandType(int value) {
        if (!init) {
            init();
        }
        return commandTypes.get(value);
    }

    public static boolean typeofHeartBeat(int command) {
        return HEARTBEAT.getValue() == command;
    }
}
*/
