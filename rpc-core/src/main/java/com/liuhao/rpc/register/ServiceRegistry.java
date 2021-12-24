package com.liuhao.rpc.register;

import java.net.InetSocketAddress;

public interface ServiceRegistry {
    /**
     * 将一个服务注册到注册表
     * @param serviceName 服务名称
     * @param inetSocketAddress 服务提供者的地址
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * 根据服务名字查找服务实体
     * @param serviceName
     * @return 服务实体
     */
    InetSocketAddress lookupService(String serviceName);
}
