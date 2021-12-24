package com.liuhao.rpc.test;

import com.liuhao.rpc.api.HelloObject;
import com.liuhao.rpc.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl2 implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl2.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到:{}", object.getMessage());
        return "[来自Socket] 这是调用的返回值：id=" + object.getId();
    }
}
