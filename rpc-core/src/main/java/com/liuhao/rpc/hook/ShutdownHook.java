package com.liuhao.rpc.hook;

import com.liuhao.rpc.util.NacosUtil;
import com.liuhao.rpc.util.ThreadPoolFactory;
import org.omg.SendingContext.RunTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class ShutdownHook {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    private final ExecutorService threadPool = ThreadPoolFactory.createDefaultThreadPool("shutdown-hook");

    /**
     * 单例模式创建钩子，保证全局只有这一个钩子
     */
    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    // 注销服务的钩子
    /**
     * Runtime对象是JVM虚拟机的运行时环境
     * 调用其addShutdownHook()方法增加一个钩子函数，创建一个新线程调用clearRegistry()完成注销工作。
     * 这个钩子函数会在JVM关闭之前被调用。
     * 这样只需要把钩子放在服务端，启动服务端时就能注册钩子了，以NettyServer为例，启动服务端后再关闭，就会发现Nacos中的注册信息已经被注销了。
     */
    public void addClearAllHook() {
        logger.info("服务端关闭前注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            threadPool.shutdown();
        }));
    }
}
