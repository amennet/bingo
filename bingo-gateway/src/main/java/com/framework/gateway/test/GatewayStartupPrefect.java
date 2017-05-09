/*
package com.framework.gateway.test;

import com.framework.broker.BrokerStartupHelper;
import com.framework.common.command.SystemCommand;
import com.framework.gateway.test.GatewayRemotingProcessor;
import com.framework.gateway.test.GatewayRemotingProcessor1;
import com.framework.gateway.test.GatewayRemotingProcessor2;
import com.framework.gateway.test.GatewayRemotingProcessor3;

*/
/**
 * Created by ZhangGe on 2017/5/1.
 *//*

public class GatewayStartupPrefect {

    public static void main(String[] args) throws InterruptedException {
        BrokerStartupHelper brokerStartupHelper = new BrokerStartupHelper("192.168.123.181:2181,192.168.123.181:2183",
                new SystemCommand("system") */
/* 暂时使用枚举，需要传入实例 *//*
);

        brokerStartupHelper.addServerUrl("127.0.0.1", 1234, "gateway", new GatewayRemotingProcessor());
        brokerStartupHelper.addServerUrl("127.0.0.1", 2234, "gateway", new GatewayRemotingProcessor1());
        brokerStartupHelper.addServerUrl("127.0.0.1", 3234, "gateway", new GatewayRemotingProcessor2());
        brokerStartupHelper.addServerUrl("127.0.0.1", 4234, "gateway", new GatewayRemotingProcessor3());

        brokerStartupHelper.addClientUrl("game");
        brokerStartupHelper.addClientUrl("hall");
        brokerStartupHelper.addClientUrl("gateway");

        brokerStartupHelper.start();
    }
}
*/
