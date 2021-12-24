package com.liuhao.rpc.register;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 */
public interface ServiceRegistry {
    /**
     * 将一个服务注册到注册表
     * @param serviceName 服务名称
     * @param inetSocketAddress 服务提供者的地址
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);
}
