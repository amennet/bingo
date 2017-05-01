package com.framework.gateway;

import com.framework.remoting.CommandCustomHeader;
import com.framework.remoting.exception.RemotingCommandException;
import com.framework.remoting.protocol.RemotingCommand;

/**
 * Created by ZhangGe on 2017/5/1.
 */
public class CommandType {

    public static void main(String[] args) {
        RemotingCommand remotingCommand = RemotingCommand.createRequestCommand(1, new CommandCustomHeader() {
            @Override
            public void checkFields() throws RemotingCommandException {

            }
        });

        System.out.println(remotingCommand);

        remotingCommand.markOnewayRPC();

        System.out.println(remotingCommand);

        remotingCommand.markResponseType();

        System.out.println(remotingCommand);

        remotingCommand.markOnewayRPC();

        System.out.println(remotingCommand);

        remotingCommand.markResponseType();

        System.out.println(remotingCommand);
    }
}
