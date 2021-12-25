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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 用于获取channel对象
 */
public class ChannelProvider {

    private static final Logger logger = LoggerFactory.getLogger(ChannelProvider.class);
    private static EventLoopGroup eventLoopGroup;
    private static Bootstrap bootstrap = initializeBootStrap();

//    /**
//     * 所有客户端Channel都保存在Map中
//     */
//    private static Map<String, Channel> channels = new ConcurrentHashMap<>();

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
        // 设置计数器值为1
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            connect(bootstrap, inetSocketAddress, countDownLatch);
            // 阻塞当前线程，直到计数器值为0
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("获取Channel时有错误发生" + e);
        }
        return channel;
    }

    private static void connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress, CountDownLatch countDownLatch) {
        connect(bootstrap, inetSocketAddress, MAX_RETRY_COUNT, countDownLatch);
    }

    private static void connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress, int retry, CountDownLatch countDownLatch) {
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if(future.isSuccess()) {
                logger.info("客户端连接成功");
                channel = future.channel();
                // 计数器-1
                countDownLatch.countDown();
                return;
            }
            if(retry == 0) {
                logger.error("客户端连接失败：重试次数已经用完，放弃连接");
                countDownLatch.countDown();
                throw new RpcException(RpcError.CLIENT_CONNECT_SERVER_FAILURE);
            }
            // 第n次重连
            int order = (MAX_RETRY_COUNT - retry) + 1;
            // 重连时间间隔，相当于1乘以2的order次方
            int delay = 1 << order;
            logger.error("{}：连接失败，第{}次重连", new Date(), order);
            // 利用schedule()在给定的延迟时间后，执行connect重连
            bootstrap.config().group().schedule(() -> connect(bootstrap, inetSocketAddress, retry - 1, countDownLatch), delay, TimeUnit.SECONDS);
        });
    }
}
