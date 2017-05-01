package com.framework.common.command;

import com.framework.common.api.ICommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZhangGe on 2017/5/1.
 */
public enum GatewayCommand implements ICommand {
    PUSH(1, "gateway"),
    GETUSER(2, "gateway"),
    TEST1(3, "gateway"),
    TEST2(4, "gateway"),
    TEST3(5, "gateway");

    private int code;
    private String brokerName;

    GatewayCommand(int code, String brokerName) {
        this.code = code;
        this.brokerName = brokerName;
    }

    public int getCode() {
        return code;
    }

    public String getBrokerName() {
        return brokerName;
    }

    @Override
    public Map<Integer, String> getCommands() {
        Map<Integer, String> commands  = new HashMap<>();
        for (GatewayCommand command : GatewayCommand.values()) {
            commands.put(command.getCode(), command.getBrokerName());
        }
        return commands;
    }
}
