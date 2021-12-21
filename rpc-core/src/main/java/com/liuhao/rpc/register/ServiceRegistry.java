package com.liuhao.rpc.register;

/**
 * 服务注册表的通用接口
 */
public interface ServiceRegistry {
    /**
     * 将一个服务注册进注册表
     * @param service
     * @param <T>
     */
    <T> void register(T service);

    /**
     * 根据服务名获取服务实体
     * @param serviceName 即接口名
     * @return
     */
    Object getService(String serviceName);
}
