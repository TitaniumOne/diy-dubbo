package com.liuhao.rpc.test;

import com.liuhao.rpc.provider.ServiceProviderImpl;
import com.liuhao.rpc.serializer.CommonSerializer;
import com.liuhao.rpc.serializer.KryoSerializer;
import com.liuhao.rpc.transport.socket.server.SocketServer;
import com.liuhao.rpc.api.HelloService;

public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl2();
        SocketServer socketServer = new SocketServer("127.0.0.1", 9998, CommonSerializer.PROTOBUF_SERIALIZER);
        socketServer.publishService(helloService, HelloService.class);
    }
}
