package com.liuhao.rpc.provider;

/**
 * 保存和提供服务实例对象
 */
public interface ServiceProvider {
    /**
     * 将一个服务注册进注册表
     * @param service
     * @param <T>
     */
    <T> void addServiceProvider(T service, String serviceClass);

    /**
     * 根据服务名获取服务实体
     * @param serviceName 即接口名
     * @return
     */
    Object getServiceProvider(String serviceName);
}
