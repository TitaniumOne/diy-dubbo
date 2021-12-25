package com.liuhao.rpc.transport;

import com.liuhao.rpc.serializer.CommonSerializer;

public interface RpcServer {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;
    void start();

    /**
     * 向Nacos注册服务
     * @param service
     * @param serviceName
     * @param <T>
     */
    <T> void publishService(T service, String serviceName);
}
