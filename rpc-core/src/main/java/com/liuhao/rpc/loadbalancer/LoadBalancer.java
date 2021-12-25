package com.liuhao.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡接口
 */
public interface LoadBalancer {
    /**
     * 从instances集合中选择一个
     * @param instances
     * @return
     */
    Instance select(List<Instance> instances);
}
