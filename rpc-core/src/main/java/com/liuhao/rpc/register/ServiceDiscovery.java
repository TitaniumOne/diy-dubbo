package com.liuhao.rpc.register;

import java.net.InetSocketAddress;

/**
 * 服务发现接口
 */
public interface ServiceDiscovery {

    /**
     * 根据服务名字查找服务实体
     * @param serviceName
     * @return 服务实体
     */
    InetSocketAddress lookupService(String serviceName);
}
