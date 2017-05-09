package com.framework.common.command;

/**
 * Created by ZhangGe on 2017/5/3.
 */
public class WorldCommand extends AbstractCommand {

    public WorldCommand(String brokerName) {
        super(brokerName);
    }

    public final static int S_GOLD_NOT_ENOUGH = 2009;  //游戏中通知钱不够了
    public final static int S_GOLD_CHANGE  	  = 2010;  //游戏中通知金钱变化
    public final static int C_NOT_ADDGOLD     = 2011;  //不充值、领取救济金、购买欢乐豆
    public final static int S_ADDGOLD_OVER 	  = 2012;  //玩家A在游戏中处理完充值
    public final static int C_REG = 	0x00020001;				//注册
    public final static int S_REG = 	0x00020002;

    public final static int C_LOGIN = 	0x00020003;				//登录
    public final static int S_LOGIN = 	0x00020004;

    public final static int C_HALL = 	0x00020005;				//大厅列表
    public final static int S_HALL = 	0x00020006;

    public final static int C_SIGNIN =  0x00020007;             //签到
    public final static int S_SIGNIN =  0x00020008;

    //好友关注
    public final static int C_FOLLOWING = 0x0002000B;		 	//关注朋友
    public final static int S_FOLLOWING = 0x0002000C;

    public final static int C_REMOVE_FOLLOWING = 0x0002000D;		 //删除关注
    public final static int S_REMOVE_FOLLOWING = 0x0002000E;

    public final static int C_FRIEND_SHIPS   =  0x00020010;		 	 //获得关系列表
    public final static int S_FRIEND_SHIPS   =  0x00020011;

    public final static int C_SEARCH_USER    = 0x00020027;		 	 //查找用户
    public final static int S_SEARCH_USER    = 0x00020028;

    public final static int C_REMOVE_FOLLOWER = 0x00020010;		 	 //删除粉丝
    public final static int S_REMOVE_FOLLOWER = 0x00020011;

    public final static int C_CHAT 			 =  0x00020012;		//聊天
    public final static int S_CHAT 			 =  0x00020013;

    public final static int C_CLEAR_CHATS 	 =  0x00020016;		//清空聊天消息
    public final static int S_CLEAR_CHATS 	 =  0x00020017;

    public final static int C_ENTER_CHAT 	 =  0x00020018;		//进入聊天
    public final static int S_ENTER_CHAT 	 =  0x00020019;

    public final static int C_USER_PACK		 =  0x0002001A;		//用户背包物品列表
    public final static int S_USER_PACK		 =  0x0002001B;

    public final static int C_USE_PROPS		 =  0x0002001C;		//用户使用背包中的道具
    public final static int S_USE_PROPS		 =  0x0002001D;

    public final static int C_PROPS_LIST 	 =  0x0002001E;		//系统道具商城列表
    public final static int S_PROPS_LIST 	 =  0x00020020;

    public final static int C_USER_MSGS 	 =  0x00020021;		//用户消息列表
    public final static int S_USER_MSGS 	 =  0x00020022;

    public final static int C_USER_MSG_VIEW  =  0x00020023;		//预览用户消息
    public final static int S_USER_MSG_VIEW  =  0x00020024;

    public final static int C_DEL_USER_MSG 	 =  0x00020025;		//删除用户消息
    public final static int S_DEL_USER_MSG 	 =  0x00020026;

    public final static int C_SEND_USER_MSG  =  0x00020029;		//给用户发送消息
    public final static int S_SEND_USER_MSG  =  0x0002002A;

    public final static int C_USER_MSG_KIT_PROC  =  0x0002002B;	//用户消息附件受理
    public final static int S_USER_MSG_KIT_PROC  =  0x0002002C;

    // 充值
    public final static int C_PAY_LIST 		 =  0x0002002D;		//充值列表
    public final static int S_PAY_LIST  	 =  0x0002002E;

    public final static int C_PAY	    	 =  0x00020030;		//充值(查询)
    public final static int S_PAY  			 =  0x00020031;

    public final static int C_PAY_LOG   	 =  0x00020032;		//充值日志数据
    public final static int S_PAY_LOG		 =  0x00020033;

    public final static int C_UPDATE_USER    =  0x00020034;		//更改用户信息
    public final static int S_UPDATE_USER	 =  0x00020035;

    public final static int C_TICK   		 =  0x00020036;		//心跳数据
    public final static int S_TICK	    	 =  0x00020037;

    public final static int S_SCROLLMSG	  	 =  0x00020038;		//发送滚动消息（后台主动推送）

    public final static int C_PRIZE_LIST	 =  0x00020039;		//商品兑换列表
    public final static int S_PRIZE_LIST	 =  0x0002003A;

    public final static int C_PRIZE_EXCHANGE =  0x0002003B;		//兑换商品
    public final static int S_PRIZE_EXCHANGE =  0x0002003C;

    public final static int C_PRIZE_EXCHANGE_LOG =  0x0002003D;	//兑换商品日志
    public final static int S_PRIZE_EXCHANGE_LOG =  0x0002003E;

    public final static int C_TASK			 =  0x00020040;		//任务变化
    public final static int S_TASK 			 =  0x00020041;		//任务列表

    public final static int C_BUY_PROPS		 =  0x00020042;		//购买道具
    public final static int S_BUY_PROPS		 =  0x00020043;

    public final static int C_PRIZE_ACCEPT	 =  0x00020044;		//领取存入背包中的奖品
    public final static int S_PRIZE_ACCEPT	 =  0x00020045;

    public final static int C_GAME_RATING	 =  0x00020046;		//游戏排名
    public final static int S_GAME_RATING	 =  0x00020047;

    public final static int C_PHONE_BOUND_SMS    =  0x00020048;     //获取验证码验证手机绑定
    public final static int S_PHONE_BOUND_SMS    =  0x00020049;

    public final static int C_PHONE_BOUND_CODE   =  0x0002004A;     //发送获取的验证码，绑定手机
    public final static int S_PHONE_BOUND_CODE   =  0x0002004B;

    public final static int S_NOTIFY_OTHER_LOGIN   =  0x0002004C;	//通知该账号在其他地方登了

    public final static int C_AUTH_SERVER    =  0x0002004D;     //认证玩家是否可进入该服务器
    public final static int S_AUTH_SERVER  	 =  0x0002004E;

    public final static int C_UPMP_TRADE     =  0x00020050;     //银联支付
    public final static int S_UPMP_TRADE  	 =  0x00020051;

    // 比赛
    public final static int C_MATCH_LIST 				= 0x20052;	  //获得斗地主比赛列表
    public final static int S_MATCH_LIST 				= 0x20053;

    public final static int C_SIGN_UP_MATCH	 			= 0x20054;	  //报名参加比赛
    public final static int S_SIGN_UP_MATCH	 			= 0x20055;

    public final static int C_CANCEL_SIGN_UP		 	= 0x20056;	  //取消报名
    public final static int S_CANCEL_SIGN_UP			= 0x20057;

    public final static int S_MATCH_NOTIFY				= 0x20058;	 //通知进入比赛（客户端显示倒计时，玩家决定是否进入）

    public final static int C_MATCH_VIDEO_LIST			= 0x20059;	 //斗地主比赛录影记录列表
    public final static int S_MATCH_VIDEO_LIST			= 0x2005A;

    public final static int C_MATCH_VIDEO_USERS 		= 0x2005B;	 //某一局比赛的玩家列表
    public final static int S_MATCH_VIDEO_USERS	    	= 0x2005C;

    public final static int C_MATCH_VIDEO_RECORD_LIST	= 0x2005D;	 //某一局比赛的玩家比赛列表
    public final static int S_MATCH_VIDEO_RECORD_LIST  	= 0x2005E;

    public final static int C_MATCH_VIDEO_RECORD		= 0x20060;	 //比赛录影记录
    public final static int S_MATCH_VIDEO_RECORD		= 0x20061;

    public final static int C_REQ_USER_INFO	 		    = 0x20062;	 //请求一次用户信息
    public final static int S_REQ_USER_INFO			    = 0x20063;

    public final static int C_USER_PROPS_VALUE	 		= 0x20064;	 //得到道具数量或到期时间
    public final static int S_USER_PROPS_VALUE		    = 0x20065;

    public final static int C_ONLINE_USER_LIST	 		= 0x20066;	 //在线用户列表
    public final static int S_ONLINE_USER_LIST		    = 0x20067;

    public final static int C_AD_WALL	  	 		    = 0x20068;	 //积分墙广告商列表
    public final static int S_AD_WALL		            = 0x20069;

    public final static int C_WALL_TASK_LIST	  	    = 0x2006A;	 //完成积分墙，获得的奖励列表
    public final static int S_WALL_TASK_LIST		    = 0x2006B;

    public final static int C_WALL_TASK_AWARD 		    = 0X2006C;   //领取任务奖励
    public final static int S_WALL_TASK_AWARD 		    = 0X2006D;

    // 任务
    public final static int S_TASK_LIST 			    = 0X2006E;   //任务列表
    public final static int S_TASK_UPDATE			    = 0X20070;   //任务变化

    public final static int C_TASK_AWARD 			    = 0X20071;   //领取任务奖励
    public final static int S_TASK_AWARD 			    = 0X20072;

    public final static int C_INGOT_TO_GOLD 			= 0X20073;   //欢乐卡兑换成欢乐豆
    public final static int S_INGOT_TO_GOLD 			= 0X20074;

    public final static int S_NOTIFY_PLAYER_DATA		= 0X20075;  //通知用户数据变化

    public final static int C_CONFIG_VERSION 			= 0X2007B;   //检测版本更新(配置数据)
    public final static int S_CONFIG_VERSION  			= 0X2007C;

    public final static int C_ENTER_SPACE 				= 0X20081;   //进入那种级别的场次
    public final static int S_ENTER_SPACE  				= 0X20082;

    public final static int S_PACK_UPDATE  				= 0X20090;  // 背包道具变化消息
    public final static int S_MONEY_UPDATE  			= 0X20091;  // 货币变化
    public final static int S_GO_WHERE  				= 0X20092;  // 已经开始和将要开始的比赛

    public final static int C_MATCH_PEOPLE  			= 0X20093;  // 比赛的人数信息
    public final static int S_MATCH_PEOPLE  			= 0X20094;

    public final static int C_ROOM_PEOPLE	  			= 0X20095;  // 房间人数
    public final static int S_ROOM_PEOPLE  				= 0X20096;

    public final static int C_WORLD_MSG					= 0X200A0;   //发送世界消息
    public final static int S_WORLD_MSG  				= 0X200A1;

    public final static int C_ALMS						= 0X200A2;   //发送请求救济金持有数量
    public final static int S_ALMS		 				= 0X200A3;

    public final static int C_GET_ALMS					= 0X200A4;   //领取救济金
    public final static int S_GET_ALMS  				= 0X200A5;

    public final static int C_CHANGE_PWD				= 0X200A6;   //修改密码
    public final static int S_CHANGE_PWD  				= 0X200A7;

    public final static int C_GAME_USER_INFO			= 0X200A8;	//用户数据
    public final static int S_GAME_USER_INFO 			= 0X200A9;

    public final static int C_PAY_GET_RESULT 			= 0x000200B0;		//其他充值接口
    public final static int S_PAY_RESULT  	 			= 0x000200B1;

    public final static int S_NOTIFY_MATCH_UPDATE  	 	= 0x000200B2; //比赛信息改变通知
    public final static int S_SCROLMSG_LIST  	 		= 0x000200B3; //滚动消息列表

    public final static int C_BUY_GOLD  	 			= 0x000200B4; //购买欢乐豆
    public final static int S_BUY_GOLD  	 			= 0x000200B5;

    public final static int C_EXCHANGE_CODE 	 		= 0x000200B6; //兑换码
    public final static int S_EXCHANGE_CODE  			= 0x000200B7;

    public final static int C_INVITE_FRIEND 	 		= 0x000200B8; //邀请好友
    public final static int S_INVITE_FRIEND  			= 0x000200B9;

    public final static int C_CLIENT_VERSION  			= 0x000200C1; //版本更新
    public final static int S_CLIENT_VERSION  			= 0x000200C2;

    public final static int C_RETURN_COLD  				= 0x000200C3; //请求邀请码数据
    public final static int S_RETURN_COLD  				= 0x000200C4;

    public final static int C_DIAL_OPEN  				= 0x000200C5; //打开转盘
    public final static int S_DIAL_OPEN   				= 0x000200C6;

    public final static int C_DIAL_ROTATE  				= 0x000200C7; //抽奖
    public final static int S_DIAL_ROTATE   			= 0x000200C8;

    public final static int S_DIAL_NOTICE  				= 0x000200C9; //通知大家中奖信息

}
