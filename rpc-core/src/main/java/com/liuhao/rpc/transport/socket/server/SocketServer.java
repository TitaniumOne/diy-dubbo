package com.liuhao.rpc.transport.socket.server;

import com.liuhao.rpc.hook.ShutdownHook;
import com.liuhao.rpc.transport.AbstractRpcServer;
import com.liuhao.rpc.transport.RpcServer;
import com.liuhao.rpc.enumeration.RpcError;
import com.liuhao.rpc.exception.RpcException;
import com.liuhao.rpc.provider.ServiceProvider;
import com.liuhao.rpc.handler.RequestHandler;
import com.liuhao.rpc.provider.ServiceProviderImpl;
import com.liuhao.rpc.register.NacosServiceRegistry;
import com.liuhao.rpc.register.ServiceRegistry;
import com.liuhao.rpc.serializer.CommonSerializer;
import com.liuhao.rpc.factory.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 利用线程池创建线程，对多线程情况进行处理
 * 不再负责注册服务，只负责启动
 */
public class SocketServer extends AbstractRpcServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private final ExecutorService threadPool;
    private CommonSerializer serializer;
    private RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public SocketServer(String host, int port, Integer serializerCode){
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
        serializer = CommonSerializer.getByCode(serializerCode);
        //创建线程池
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        //自动注册服务
//        scanServices();
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            logger.info("服务器正在启动");
            //添加钩子，服务端关闭时会注销服务
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            // 没有接收到连接请求时，accept会一直阻塞
            while((socket = serverSocket.accept()) != null) {
                logger.info("客户端已经连接:{}:{}" + socket.getInetAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandlerThread(socket, requestHandler, serializer));
            }
        } catch (IOException e) {
            logger.info("连接时有错误发生：" + e);
        }
    }
}
