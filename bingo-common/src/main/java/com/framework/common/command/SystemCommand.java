package com.framework.common.command;

/**
 * Created by ZhangGe on 2017/5/1.
 */
public class SystemCommand extends AbstractCommand {

    public SystemCommand(String brokerName) {
        super(brokerName);
    }

    public final static int HEART_BEAT = 1;
    public final static int REGISTER_CLIENT = 22;
    public final static int UNREGISTER_CLIENT = 23;
    public final static int NOTIFY_REGISTER_CLIENT = 24;
    public final static int NOITFY_UNGISTER_CLIENT = 25;

    // 系统命令
    public final static int CONNECT            = 2;  // 连接网关

    public final static int DISCONNCET         = 11; // 用户断开网络连接，暂时不实现，有可能用户存在闪短的情况，不清除topic。

    public final static int REFRESH            = 9;  // 刷新用户连接

    public final static int LOGIN              = 3;  // 登录

    public final static int LOGOUT             = 7;  // 退出登录

    public final static int REGISTER           = 4;  // 注册

    public final static int PHONECODE          = 5;  // 获取手机验证码

    public final static int VALIDATEPHONECODE  = 6;  // 验证手机验证码

    public final static int PUSH               = 8;  // 测试用命令，实际运行中是服务器对客户端的推送
    public final static int PUSHTO             = 10; // 测试用命令，实际运行中是服务器对客户端的推送

}