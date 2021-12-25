package com.liuhao.rpc.transport.netty.client;

import com.liuhao.rpc.codec.CommonDecoder;
import com.liuhao.rpc.codec.CommonEncoder;
import com.liuhao.rpc.enumeration.RpcError;
import com.liuhao.rpc.exception.RpcException;
import com.liuhao.rpc.serializer.CommonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 用于获取channel对象
 */
public class ChannelProvider {

    private static final Logger logger = LoggerFactory.getLogger(ChannelProvider.class);
    private static EventLoopGroup eventLoopGroup;
    private static Bootstrap bootstrap = initializeBootStrap();

    /**
     * 所有客户端Channel都保存在Map中
     */
    private static Map<String, Channel> channels = new ConcurrentHashMap<>();

    private static final int MAX_RETRY_COUNT = 5;
    private static Channel channel = null;

    private static Bootstrap initializeBootStrap() {
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                // 连接的超时时间，超过则连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // 启动该功能时，TCP会主动探测空闲连接的有效性。可以将此功能视为TCP的心跳机制，默认心跳间隔7200s，即2小时
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 配置Channel参数，nodelay没有延迟，true就代表禁用Nagle算法，减小传输延迟。
                .option(ChannelOption.TCP_NODELAY, true);
        return bootstrap;
    }

    public static Channel get(InetSocketAddress inetSocketAddress, CommonSerializer serializer) {
        String key = inetSocketAddress.toString() + serializer.getCode();
        if(channels.containsKey(key)) {
            Channel channel = channels.get(key);
            if(channel != null && channel.isActive()) {
                return channel;
            } else {
                channels.remove(key);
            }
        }

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new CommonEncoder(serializer))
                        // 设定IdleStateHandler心跳检测，每5秒进行一次写检测，如果5秒内write()方法未被调用，则触发一次userEventTrigger()方法
                        // 实现客户端每5秒向服务端发一次消息
                        .addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS))
                        .addLast(new CommonDecoder())
                        .addLast(new NettyClientHandler());
            }
        });
        Channel channel = null;
        try {
            channel = connect(bootstrap, inetSocketAddress);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("获取Channel时有错误发生" + e);
            return null;
        }
        channels.put(key, channel);
        return channel;
    }

    private static Channel connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if(future.isSuccess()) {
                logger.info("客户端连接成功");
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }
}
