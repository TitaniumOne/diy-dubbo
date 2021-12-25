package com.liuhao.rpc.test.netty;

import com.liuhao.rpc.api.HelloService;
import com.liuhao.rpc.serializer.CommonSerializer;
import com.liuhao.rpc.transport.netty.server.NettyServer;

public class NettyTestServer2 {
    static final String HOST = "127.0.0.1";
    static final int PORT = 9995;
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl2(HOST + ":" + PORT);
        NettyServer server = new NettyServer(HOST, PORT, CommonSerializer.PROTOBUF_SERIALIZER);
        server.publishService(helloService, HelloService.class);
    }
}
