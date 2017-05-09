package com.framework.gateway.network.client;

import com.framework.broker.BrokerController;
import com.framework.common.command.GatewayCommand;
import com.framework.common.command.SystemCommand;
import com.framework.gateway.network.GatewayController;
import com.framework.remoting.CommandCustomHeader;
import com.framework.remoting.exception.RemotingCommandException;
import com.framework.remoting.protocol.RemotingCommand;
import io.netty.channel.Channel;

public class NotifyClientChangeListener implements ClientIdChangeListener {
    private final BrokerController brokerController;

    public NotifyClientChangeListener(String brokerName, BrokerController brokerController) {
        this.brokerController = brokerController;
    }

    @Override
    public void clientIdChanged(String clientId, Channel channel) {
        RemotingCommand remotingCommand = RemotingCommand.createRequestCommand(SystemCommand.NOTIFY_REGISTER_CLIENT, new CommandCustomHeader() {
            @Override
            public void checkFields() throws RemotingCommandException {

            }
        });
        remotingCommand.setBody(clientId.getBytes());
        remotingCommand.markOnewayRPC();
        channel.writeAndFlush(remotingCommand);
    }
}
