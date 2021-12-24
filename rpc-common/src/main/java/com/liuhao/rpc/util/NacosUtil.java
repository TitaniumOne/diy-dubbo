package com.liuhao.rpc.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.liuhao.rpc.enumeration.RpcError;
import com.liuhao.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 管理Nacos的连接
 */
public class NacosUtil {

    private static final Logger logger = LoggerFactory.getLogger(NacosUtil.class);

    private static final String SERVER_ADDR = "127.0.0.1:8848";

    private static final NamingService namingService;

    static {
        namingService = getNacosNamingService();
    }
    /**
     * 连接到Nacos创建的命名空间
     * @return
     */
    public static NamingService getNacosNamingService() {
        try {
            // 连接Nacos创建命名服务
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            logger.error("连接Nacos时有错误发生：" + e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    /**
     * 注册服务到Nacos
     * @param serviceName
     * @param inetSocketAddress
     * @throws NacosException
     */
    public static void registerService(String serviceName, InetSocketAddress inetSocketAddress) throws NacosException {
        // 向Nacos注册服务
        namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
    }

    /**
     * 返回所有提供该服务的服务端地址
     * @param serviceName
     * @return
     * @throws NacosException
     */
    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }
}
