package com.liuhao.rpc.test;

import com.liuhao.rpc.register.DefaultServiceRegistry;
import com.liuhao.rpc.serializer.KryoSerializer;
import com.liuhao.rpc.socket.server.SocketServer;
import com.liuhao.rpc.api.HelloService;

public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        DefaultServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.setSerializer(new KryoSerializer());
        socketServer.start(9000);
    }
}
