package com.liuhao.rpc.test.netty2;

import com.liuhao.rpc.annotation.Service;
import com.liuhao.rpc.api.HelloObject;
import com.liuhao.rpc.api.HelloService;
import com.liuhao.rpc.test.netty1.HelloServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class HelloServiceImpl2 implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到:{}", object.getMessage());
        return "[来自NettyServer2] 这是调用的返回值：id=" + object.getId();
    }
}
