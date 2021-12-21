package com.liuhao.rpc.client;

import com.liuhao.rpc.entity.RpcRequest;
import com.liuhao.rpc.entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 为了屏蔽方法调用的底层细节（比如网络传输），使用动态代理
 */
public class RpcClientProxy implements InvocationHandler {

    private String host;
    private int port;

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        // 远程调用的客户端
        RpcClient rpcClient = new RpcClient();
        return ((RpcResponse)rpcClient.sendRequest(rpcRequest, host, port)).getData();
    }
}
