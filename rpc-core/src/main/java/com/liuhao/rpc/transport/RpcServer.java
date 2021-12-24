package com.liuhao.rpc.transport;

import com.liuhao.rpc.serializer.CommonSerializer;

public interface RpcServer {
    void start();
    void setSerializer(CommonSerializer serializer);

    /**
     * 向Nacos注册服务
     * @param service
     * @param serviceClass
     * @param <T>
     */
    <T> void publishService(T service, Class<T> serviceClass);
}
