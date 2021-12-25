package com.liuhao.rpc.test.socket;

import com.liuhao.rpc.api.HelloObject;
import com.liuhao.rpc.api.HelloService;
import com.liuhao.rpc.test.netty1.HelloServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketHelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到:{}", object.getMessage());
        return "[来自Socket - ] 这是调用的返回值：id=" + object.getId();
    }
}
