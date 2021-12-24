package com.liuhao.rpc.test;

import com.liuhao.rpc.api.HelloService;
import com.liuhao.rpc.transport.netty.server.NettyServer;
import com.liuhao.rpc.serializer.ProtostuffSerializer;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1", 9999);
        server.setSerializer(new ProtostuffSerializer());
        server.publishService(helloService, HelloService.class);
    }
}
