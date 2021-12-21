package com.liuhao.rpc.test;

import com.liuhao.rpc.register.DefaultServiceRegistry;
import com.liuhao.rpc.server.RpcServer;
import com.liuhao.rpc.api.HelloService;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        DefaultServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        // 注册HelloServiceImpl服务
        rpcServer.start(9000);
    }
}
