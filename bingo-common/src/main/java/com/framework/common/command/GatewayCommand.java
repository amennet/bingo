package com.framework.common.command;

/**
 * Created by ZhangGe on 2017/5/3.
 */
public class GatewayCommand extends AbstractCommand {
    public GatewayCommand(String brokerName) {
        super(brokerName);
    }

    public static int USER_REGISTER = 1001;
    public static int USER_LOGIN = 1002;

    public static final int GAME_START = 10005;
}
