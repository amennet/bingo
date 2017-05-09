package com.framework.gateway.network.server;

import com.framework.common.bean.NetMessage;
import com.framework.gateway.exception.GatewayException;
import com.framework.gateway.network.client.GatewayRequestTask;
import com.framework.gateway.network.codec.GatewayDecoder;
import com.framework.gateway.network.codec.GatewayEncoder;
import com.framework.gateway.processor.GatewayProcessor;
import com.framework.remoting.ChannelEventListener;
import com.framework.remoting.common.*;
import com.framework.remoting.netty.NettyEvent;
import com.framework.remoting.netty.NettyEventType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GatewayRemotingServer {
    private static final Logger log = LoggerFactory.getLogger(RemotingHelper.ROCKETMQ_REMOTING);
    private final ServerBootstrap serverBootstrap;
    private final EventLoopGroup eventLoopGroupSelector;
    private final EventLoopGroup eventLoopGroupBoss;
    private final GatewayRemotingConfig gatewayRemotingConfig;

    private final Timer timer = new Timer("ClientHouseKeepingService", true);

    private final ExecutorService publicExecutor;
    private final ChannelEventListener channelEventListener;
    private DefaultEventExecutorGroup defaultEventExecutorGroup;
    protected final GatewayEventExecuter gatewayEventExecuter = new GatewayEventExecuter();

    private final HashMap<Integer/* request code */, Pair<GatewayProcessor, ExecutorService>> processorTable =
            new HashMap<Integer, Pair<GatewayProcessor, ExecutorService>>(64);

    private Pair<GatewayProcessor, ExecutorService> defaultRequestProcessor;

    private final Semaphore semaphoreOneway;

    private int port = 0;

    public GatewayRemotingServer(final GatewayRemotingConfig gatewayRemotingConfig) {
        this(gatewayRemotingConfig, null);
    }

    public GatewayRemotingServer(final GatewayRemotingConfig gatewayRemotingConfig, final ChannelEventListener channelEventListener) {
        this.serverBootstrap = new ServerBootstrap();
        this.gatewayRemotingConfig = gatewayRemotingConfig;
        this.semaphoreOneway = new Semaphore(gatewayRemotingConfig.getServerOnewaySemaphoreValue(), true);
        this.channelEventListener = channelEventListener;
        int publicThreadNums = gatewayRemotingConfig.getServerCallbackExecutorThreads();
        if (publicThreadNums <= 0) {
            publicThreadNums = 4;
        }

        this.publicExecutor = Executors.newFixedThreadPool(publicThreadNums, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "NettyServerPublicExecutor_" + this.threadIndex.incrementAndGet());
            }
        });

        this.eventLoopGroupBoss = new NioEventLoopGroup(1, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("NettyBoss_%d", this.threadIndex.incrementAndGet()));
            }
        });

        if (RemotingUtil.isLinuxPlatform() //
                && gatewayRemotingConfig.isUseEpollNativeSelector()) {
            this.eventLoopGroupSelector = new EpollEventLoopGroup(gatewayRemotingConfig.getServerSelectorThreads(), new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);
                private int threadTotal = gatewayRemotingConfig.getServerSelectorThreads();

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyServerEPOLLSelector_%d_%d", threadTotal, this.threadIndex.incrementAndGet()));
                }
            });
        } else {
            this.eventLoopGroupSelector = new NioEventLoopGroup(gatewayRemotingConfig.getServerSelectorThreads(), new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);
                private int threadTotal = gatewayRemotingConfig.getServerSelectorThreads();

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyServerNIOSelector_%d_%d", threadTotal, this.threadIndex.incrementAndGet()));
                }
            });
        }
    }

    public void start() {
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(
                gatewayRemotingConfig.getServerWorkerThreads(),
                new ThreadFactory() {

                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyServerCodecThread_" + this.threadIndex.incrementAndGet());
                    }
                });

        ServerBootstrap childHandler =
                this.serverBootstrap.group(this.eventLoopGroupBoss, this.eventLoopGroupSelector).channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .option(ChannelOption.SO_KEEPALIVE, false)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .option(ChannelOption.SO_SNDBUF, gatewayRemotingConfig.getServerSocketSndBufSize())
                        .option(ChannelOption.SO_RCVBUF, gatewayRemotingConfig.getServerSocketRcvBufSize())
                        .localAddress(new InetSocketAddress(this.gatewayRemotingConfig.getHostname(), this.gatewayRemotingConfig.getListenPort()))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(
                                        defaultEventExecutorGroup,
                                        new GatewayEncoder(),
                                        new GatewayDecoder(),
                                        new IdleStateHandler(0, 0, gatewayRemotingConfig.getServerChannelMaxIdleTimeSeconds()),
                                        new NettyConnetManageHandler(),
                                        new NettyServerHandler());
                            }
                        });

        if (gatewayRemotingConfig.isServerPooledByteBufAllocatorEnable()) {
            childHandler.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }

        try {
            ChannelFuture sync = this.serverBootstrap.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) sync.channel().localAddress();
            this.port = addr.getPort();
        } catch (InterruptedException e1) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e1);
        }

        if (this. channelEventListener != null) {
            this.gatewayEventExecuter.start();
        }

    }

    public void shutdown() {
        try {
            this.eventLoopGroupBoss.shutdownGracefully();

            this.eventLoopGroupSelector.shutdownGracefully();

            if (this.defaultEventExecutorGroup != null) {
                this.defaultEventExecutorGroup.shutdownGracefully();
            }

            if (this.gatewayEventExecuter != null) {
                this.gatewayEventExecuter.shutdown();
            }

        } catch (Exception e) {
            log.error("NettyRemotingServer shutdown exception, ", e);
        }

        if (this.publicExecutor != null) {
            try {
                this.publicExecutor.shutdown();
            } catch (Exception e) {
                log.error("NettyRemotingServer shutdown exception, ", e);
            }
        }
    }

    public void putGatewayEvent(final NettyEvent event) {
        this.gatewayEventExecuter.putNettyEvent(event);
    }

    public void registerProcessor(int requestCode, GatewayProcessor processor, ExecutorService executor) {
        ExecutorService executorThis = executor;
        if (null == executor) {
            executorThis = this.publicExecutor;
        }

        Pair<GatewayProcessor, ExecutorService> pair = new Pair<GatewayProcessor, ExecutorService>(processor, executorThis);
        this.processorTable.put(requestCode, pair);
    }

    public ChannelEventListener getChannelEventListener() {
        return channelEventListener;
    }

    public void registerDefaultProcessor(GatewayProcessor processor, ExecutorService executor) {
        this.defaultRequestProcessor = new Pair<GatewayProcessor, ExecutorService>(processor, executor);
    }

    public int localListenPort() {
        return this.port;
    }

    public Pair<GatewayProcessor, ExecutorService> getProcessorPair(int requestCode) {
        return processorTable.get(requestCode);
    }

    public void invoke(Channel channel, NetMessage request, long timeoutMillis) throws GatewayException {
        this.invokeImpl(channel, request, timeoutMillis);
    }

    public void invokeImpl(final Channel channel, final NetMessage request, final long timeoutMillis) throws GatewayException {
        final SemaphoreReleaseOnlyOnce once = new SemaphoreReleaseOnlyOnce(this.semaphoreOneway);
        try {
            channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    once.release();
                    if (!f.isSuccess()) {
                        log.warn("send a request command to channel <" + channel.remoteAddress() + "> failed.");
                    }
                }
            });
        } catch (Exception e) {
            once.release();
            log.warn("write send a request command to channel <" + channel.remoteAddress() + "> failed.");
            throw new GatewayException(RemotingHelper.parseChannelRemoteAddr(channel), e);
        }
    }

    public void processRequestCommand(final ChannelHandlerContext ctx, final NetMessage cmd) {
        final Pair<GatewayProcessor, ExecutorService> matched = this.processorTable.get(cmd.getCommand());
        final Pair<GatewayProcessor, ExecutorService> pair = null == matched ? this.defaultRequestProcessor : matched;

        if (pair != null) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        pair.getObject1().processRequest(ctx, cmd);
                    } catch (Throwable e) {
                        log.error("process request exception", e);
                        log.error(cmd.toString());

                        final NetMessage response = NetMessage.fail();
                        ctx.writeAndFlush(response);
                    }
                }
            };
            try {
                final GatewayRequestTask requestTask = new GatewayRequestTask(run, ctx.channel(), cmd);
                pair.getObject2().submit(requestTask);
            } catch (Exception e) {
                if ((System.currentTimeMillis() % 10000) == 0) {
                    log.warn(RemotingHelper.parseChannelRemoteAddr(ctx.channel()) //
                            + ", too many requests and system thread pool busy, RejectedExecutionException " //
                            + pair.getObject2().toString() //
                            + " request code: " + cmd.getCommand());
                }
                log.error("send error", e);
            }
        } else {
            String error = " request type " + cmd.getCommand() + " not supported";
            final NetMessage response = NetMessage.fail();
            ctx.writeAndFlush(response);
            log.error(RemotingHelper.parseChannelRemoteAddr(ctx.channel()) + error);
        }
    }

    class NettyServerHandler extends SimpleChannelInboundHandler<NetMessage> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, NetMessage msg) throws Exception {
            processRequestCommand(ctx, msg);
        }
    }

    class NettyConnetManageHandler extends ChannelDuplexHandler {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.info("NETTY SERVER PIPELINE: channelRegistered {}", remoteAddress);
            super.channelRegistered(ctx);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.info("NETTY SERVER PIPELINE: channelUnregistered, the channel[{}]", remoteAddress);
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.info("NETTY SERVER PIPELINE: channelActive, the channel[{}]", remoteAddress);
            super.channelActive(ctx);

            if (GatewayRemotingServer.this.channelEventListener != null) {
                GatewayRemotingServer.this.putGatewayEvent(new NettyEvent(NettyEventType.CONNECT, remoteAddress.toString(), ctx.channel()));
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.info("NETTY SERVER PIPELINE: channelInactive, the channel[{}]", remoteAddress);
            super.channelInactive(ctx);

            if (GatewayRemotingServer.this.channelEventListener != null) {
                GatewayRemotingServer.this.putGatewayEvent(new NettyEvent(NettyEventType.CLOSE, remoteAddress.toString(), ctx.channel()));
            }
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent evnet = (IdleStateEvent) evt;
                if (evnet.state().equals(IdleState.ALL_IDLE)) {
                    final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
                    log.warn("NETTY SERVER PIPELINE: IDLE exception [{}]", remoteAddress);
                    RemotingUtil.closeChannel(ctx.channel());
                    if (GatewayRemotingServer.this.channelEventListener != null) {
                        GatewayRemotingServer.this
                                .putGatewayEvent(new NettyEvent(NettyEventType.IDLE, remoteAddress.toString(), ctx.channel()));
                    }
                }
            }
            ctx.fireUserEventTriggered(evt);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.warn("NETTY SERVER PIPELINE: exceptionCaught {}", remoteAddress);
            log.warn("NETTY SERVER PIPELINE: exceptionCaught exception.", cause);

            if (GatewayRemotingServer.this.channelEventListener != null) {
                GatewayRemotingServer.this.putGatewayEvent(new NettyEvent(NettyEventType.EXCEPTION, remoteAddress.toString(), ctx.channel()));
            }

            RemotingUtil.closeChannel(ctx.channel());
        }
    }

    class GatewayEventExecuter extends ServiceThread {
        private final LinkedBlockingQueue<NettyEvent> eventQueue = new LinkedBlockingQueue<NettyEvent>();
        private final int maxSize = 10000;

        public void putNettyEvent(final NettyEvent event) {
            if (this.eventQueue.size() <= maxSize) {
                this.eventQueue.add(event);
            } else {
                log.warn("event queue size[{}] enough, so drop this event {}", this.eventQueue.size(), event.toString());
            }
        }

        @Override
        public void run() {
            log.info(this.getServiceName() + " service started");

            final ChannelEventListener listener = GatewayRemotingServer.this.getChannelEventListener();

            while (!this.isStopped()) {
                try {
                    NettyEvent event = this.eventQueue.poll(3000, TimeUnit.MILLISECONDS);
                    if (event != null && listener != null) {
                        switch (event.getType()) {
                            case IDLE:
                                listener.onChannelIdle(event.getRemoteAddr(), event.getChannel());
                                break;
                            case CLOSE:
                                listener.onChannelClose(event.getRemoteAddr(), event.getChannel());
                                break;
                            case CONNECT:
                                listener.onChannelConnect(event.getRemoteAddr(), event.getChannel());
                                break;
                            case EXCEPTION:
                                listener.onChannelException(event.getRemoteAddr(), event.getChannel());
                                break;
                            default:
                                break;

                        }
                    }
                } catch (Exception e) {
                    log.warn(this.getServiceName() + " service has exception. ", e);
                }
            }

            log.info(this.getServiceName() + " service end");
        }

        @Override
        public String getServiceName() {
            return GatewayEventExecuter.class.getSimpleName();
        }
    }
}