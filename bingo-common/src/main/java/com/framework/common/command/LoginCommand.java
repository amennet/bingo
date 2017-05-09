package com.framework.common.command;

/**
 * Created by ZhangGe on 2017/5/4.
 */
public class LoginCommand extends AbstractCommand {

    public LoginCommand(String brokerName) {
        super(brokerName);
    }

    public final static int CONNECT = 3001;
    public final static int REFRESH = 3002;
    public final static int PHONECODE = 3003;
    public final static int VALIDATEPHONECODE = 3004;
    public final static int LOGIN = 3005;
    public final static int REGISTER = 3006;
    public final static int LOGOUT = 3007;

}
