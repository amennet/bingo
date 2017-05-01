package com.framework.broker;

import com.framework.common.thread.ThreadFactoryImpl;
import com.framework.remoting.netty.NettyServerConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ZhangGe on 2017/4/29.
 */
public class ExecutorFactory {

    public ExecutorService get() {
        return Executors.newFixedThreadPool(
                new NettyServerConfig().getServerWorkerThreads(),
                new ThreadFactoryImpl("RemotingExecutorThread_"));
    }
}
