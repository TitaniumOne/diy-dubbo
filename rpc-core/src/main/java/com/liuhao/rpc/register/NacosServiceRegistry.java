package com.liuhao.rpc.register;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.liuhao.rpc.enumeration.RpcError;
import com.liuhao.rpc.exception.RpcException;
import com.liuhao.rpc.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NacosServiceRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    /**
     * 将服务的名称和地址注册进服务注册中心
     * @param serviceName 服务名称
     * @param inetSocketAddress 服务提供者的地址
     */
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            // 向Nacos注册服务
            NacosUtil.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生" + e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }
}
