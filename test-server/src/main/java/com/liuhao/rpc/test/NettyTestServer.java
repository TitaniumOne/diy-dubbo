package com.liuhao.rpc.test;

import com.liuhao.rpc.api.HelloService;
import com.liuhao.rpc.netty.server.NettyServer;
import com.liuhao.rpc.register.DefaultServiceRegistry;
import com.liuhao.rpc.register.ServiceRegistry;
import com.liuhao.rpc.serializer.KryoSerializer;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.setSerializer(new KryoSerializer());
        server.start(9999);
    }
}
