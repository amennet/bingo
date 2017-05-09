/*
package com.framework.gateway.test;

import com.framework.remoting.CommandCustomHeader;
import com.framework.remoting.exception.RemotingCommandException;
import com.framework.remoting.protocol.RemotingCommand;

import java.util.concurrent.CountDownLatch;

*/
/**
 * Created by ZhangGe on 2017/5/2.
 *//*

public class CommandTest {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        CountDownLatch latch=new CountDownLatch(1000);//两个工人的协作

        for (int i = 0 ;i < 1000;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RemotingCommand requestCommand = RemotingCommand.createRequestCommand(1, new CommandCustomHeader() {
                        @Override
                        public void checkFields() throws RemotingCommandException {

                        }
                    });

                    requestCommand.setBody("123456789".getBytes());


                    for (int i = 0; i < 1000; i++) {


                        System.out.println("2---------------------------------");

                        // requestCommand.markResponseType();  // 不能用，会超时返回  ,用默认的

                        RemotingCommand remotingCommand1 = brokerStartupHelper.getBrokerController().clusterRequest(requestCommand);

                        System.out.println(remotingCommand1);

                        System.out.println("3---------------------------------");

                        requestCommand.markOnewayRPC();

                        RemotingCommand remotingCommand2 = brokerStartupHelper.getBrokerController().clusterRequest(requestCommand);

                        System.out.println(remotingCommand2);

                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

                        System.out.println("===============================" + i +"============================");
                    }
                    latch.countDown();//工人完成工作，计数器减一
                }
            }).start();

        }
        latch.await();//等待所有工人完成工作

        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println(start - System.currentTimeMillis());
    }
    }
}
*/
