package com.liuhao.rpc.test.netty;

import com.liuhao.rpc.api.HelloObject;
import com.liuhao.rpc.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    private String address;
    public HelloServiceImpl(String address) {
        this.address = address;
    }

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到:{}", object.getMessage());
        return "[来自Netty - " + address + "] 这是调用的返回值：id=" + object.getId();
    }
}
