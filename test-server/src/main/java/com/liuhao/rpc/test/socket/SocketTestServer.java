package com.liuhao.rpc.test.socket;

import com.liuhao.rpc.serializer.CommonSerializer;
import com.liuhao.rpc.test.netty.HelloServiceImpl2;
import com.liuhao.rpc.transport.socket.server.SocketServer;
import com.liuhao.rpc.api.HelloService;

public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new SocketHelloServiceImpl();
        SocketServer socketServer = new SocketServer("127.0.0.1", 9998, CommonSerializer.PROTOBUF_SERIALIZER);
        socketServer.publishService(helloService, HelloService.class);
    }
}
