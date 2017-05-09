package com.framework.common.command;

import com.framework.common.api.ICommand;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZhangGe on 2017/5/3.
 */
public class AbstractCommand implements ICommand {

    protected String brokerName;

    public AbstractCommand(String brokerName) {
        this.brokerName = brokerName;
    }

    @Override
    public String getBrokerName() {
        return brokerName;
    }

    @Override
    public Map<Integer, String> getCommands() {
        Map<Integer, String> commands = new HashMap<>();
        Class<? extends AbstractCommand> clazz = this.getClass();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.getName().contains("brokerName")) {
                continue;
            }
            try {
                commands.put(field.getInt(this), getBrokerName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return commands;
    }
}
