package com.liuhao.rpc.transport.netty.server;

import com.liuhao.rpc.transport.RpcServer;
import com.liuhao.rpc.codec.CommonDecoder;
import com.liuhao.rpc.codec.CommonEncoder;
import com.liuhao.rpc.enumeration.RpcError;
import com.liuhao.rpc.exception.RpcException;
import com.liuhao.rpc.provider.ServiceProvider;
import com.liuhao.rpc.provider.ServiceProviderImpl;
import com.liuhao.rpc.register.NacosServiceRegistry;
import com.liuhao.rpc.register.ServiceRegistry;
import com.liuhao.rpc.serializer.CommonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final String host;
    private final int port;

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    private CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
    }

    @Override
    public <T> void publishService(T service, Class<T> serviceClass) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.addServiceProvider(service, serviceClass);
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }

    @Override
    public void start() {
        // main reactor：可以理解为专门用于处理客户端连接的主线程池
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // sub reactor：可以理解为专门用于处理IO事件的子线程池
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 初始化Netty的启动引导类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 给引导类配置两大线程组
            serverBootstrap.group(bossGroup, workerGroup)
                    // (非必备)为父通道装配流水线。打印日志
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 设置服务端通道类型
                    // NioServerSocketChannel和NioSocketChannel的概念可以和 BIO 编程模型中的ServerSocket以及Socket两个概念对应上
                    .channel(NioServerSocketChannel.class)
                    // 配置ServerChannel参数，服务端接受连接的最大队列长度，如果队列已满，客户端连接将被拒绝
                    .option(ChannelOption.SO_BACKLOG, 256)
                    // 配置Channel参数，nodelay没有延迟，true就代表禁用Nagle算法，减小传输延迟
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 装配子通道的流水线
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            // 初始化管道
                            ChannelPipeline p = ch.pipeline();
                            // 往管道中添加Handler，注意入站Handler与出站Handler都必须按实际执行顺序添加，比如先解码再Server处理，那Decoder()就要放在前面。
                            // 但入站和出站Handler之间则互不影响，这里我就是先添加的出站Handler再添加的入站
                            p.addLast(new CommonEncoder(serializer))
                                    .addLast(new CommonDecoder())
                                    .addLast(new NettyServerHandler());
                        }
                    });
            // 绑定端口，启动Netty，sync()代表阻塞主Server线程，以执行Netty线程，如果不阻塞Netty就直接被下面shutdown了
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            // 等确定通道关闭了，关闭future回到主Server线程
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生", e);
        } finally {
            // 优雅关闭Netty服务端且清理掉内存
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
