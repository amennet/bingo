package com.framework.common.command;

/**
 * Created by ZhangGe on 2017/5/11.
 */
public class TestCommand extends AbstractCommand{

    public TestCommand(String brokerName) {
        super(brokerName);
    }

    public static final int connect = 99;
    public static final int LOOP1 = 100;
    public static final int TEST_LOGIN = 101;
    public static final int OPERATION = 102;
    public static final int WORLD = 103;
    public static final int HALL = 104;
    public static final int DB = 105;

}
