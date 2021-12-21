package com.liuhao.rpc.test;

import com.liuhao.rpc.server.RpcServer;
import com.liuhao.rpc.api.HelloService;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        // 注册HelloServiceImpl服务
        rpcServer.register(helloService, 9000);
    }
}
