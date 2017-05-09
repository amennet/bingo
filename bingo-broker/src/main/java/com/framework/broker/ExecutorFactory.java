package com.framework.broker;

import com.framework.common.thread.ThreadFactoryImpl;
import com.framework.remoting.netty.NettyServerConfig;
import io.netty.util.internal.ConcurrentSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ZhangGe on 2017/4/29.
 */
public class ExecutorFactory {

    private static ConcurrentSet<ExecutorService> executorServices = new ConcurrentSet<>();

    public ExecutorService get() {
        ExecutorService executorService = Executors.newFixedThreadPool(
                new NettyServerConfig().getServerWorkerThreads(),
                new ThreadFactoryImpl("RemotingExecutorThread_"));
        executorServices.add(executorService);
        return executorService;
    }

    public void shutdown() {
        for (ExecutorService executorService : executorServices) {
            executorService.shutdown();
        }
        executorServices.clear();
    }
}
