package com.framework.common.api;

import java.util.Map;

/**
 * Created by ZhangGe on 2017/5/3.
 */
public interface ICommand {

    String getBrokerName();

    Map<Integer/* command */, String/* brokerName */> getCommands();
}
