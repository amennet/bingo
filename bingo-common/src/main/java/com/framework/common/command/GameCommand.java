package com.framework.common.command;

/**
 * Created by ZhangGe on 2017/5/3.
 */
public class GameCommand extends AbstractCommand {
    public GameCommand(String brokerName) {
        super(brokerName);
    }

    public final static int C_CREATE_ROOM        = 51003;    //创建房间
    public final static int S_CREATE_ROOM        = 51004;

    public final static int C_RECONNECT_ROOM     = 51005;    //重连房间
    public final static int S_RECONNECT_ROOM     = 51006;

    public final static int C_NEW_DESK           = 51007;    //新建牌桌
    public final static int S_NEW_DESK           = 51008;

    public final static int C_ENTER_DESK         = 51009;    //进入牌桌
    public final static int S_ENTER_DESK         = 51010;

    public final static int C_ENTER_DESK_RANDOM  = 51011;    //随机进入牌桌
    public final static int S_ENTER_DESK_RANDOM  = 51012;

    public final static int C_ENTER_RECONNECT    = 51013;    //牌桌重连
    public final static int S_ENTER_RECONNECT    = 51014;

    // 斗地主

    public final static int C_HEART 		 	 = 40001;  //心跳指令
    public final static int S_HEART 			 = 40002;

    public final static int C_ENTER_ROOM 		 = 40003;  //进入房间
    public final static int S_ENTER_ROOM 		 = 40004;

    public final static int C_EXIT_ROOM 		 = 40005;  //退出房间
    public final static int S_EXIT_ROOM 		 = 40006;

    public final static int C_CONTINUE	 		 = 40007;  //继续游戏
    public final static int S_CONTINUE	 		 = 40008;

    public final static int C_CALL_LORD			 = 40009;	 //叫地主
    public final static int S_CALL_LORD			 = 40010;

    public final static int C_PULL_CARD			 = 40011;  //出牌
    public final static int S_PULL_CARD			 = 40012;

    public final static int S_START_GAME		 = 40013;	 //开始游戏
    public final static int S_DEAL 				 = 40014;	 //发牌
    public final static int S_SCENE	 			 = 40010;	 //游戏场景
    public final static int S_GAME_END			 = 40011;	 //游戏结束
    public final static int S_THE_WHO			 = 40012;	 //轮到谁
    public final static int S_CALL_LORD_RESULT	 = 40013;	 //叫地主结果
    public final static int S_KICK_GAME			 = 40014;	 //踢出玩家离开游戏

    public final static int C_USER_INFO			 = 40015;	 //用户数据
    public final static int S_USER_INFO 		 = 40016;

    public final static int S_NOTIFY_PLAYER_DATA = 40017;  //通知其他玩家,玩家数数据变化

    public final static int C_USE_NOTE_CARD		 = 40018;  //打开记牌器界面
    public final static int S_USE_NOTE_CARD		 = 40019;

    public final static int C_CHAT				 = 40020;	 //聊天
    public final static int S_CHAT				 = 40021;

    public final static int C_AGENT				 = 40022;  //托管
    public final static int S_AGENT				 = 40023;

    public final static int S_BEISHU			 = 40030;

    public final static int S_TIME_OUT			 = 40031;  //超时通知

}
