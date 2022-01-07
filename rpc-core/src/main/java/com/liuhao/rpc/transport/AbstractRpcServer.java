package com.liuhao.rpc.transport;

import com.liuhao.rpc.annotation.Service;
import com.liuhao.rpc.annotation.ServiceScan;
import com.liuhao.rpc.enumeration.RpcError;
import com.liuhao.rpc.exception.RpcException;
import com.liuhao.rpc.provider.ServiceProvider;
import com.liuhao.rpc.register.ServiceRegistry;
import com.liuhao.rpc.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * 扫描服务类并进行服务注册
 */
public abstract class AbstractRpcServer implements RpcServer {

    protected Logger logger = LoggerFactory.getLogger(AbstractRpcServer.class);


    protected String host;
    protected int port;

    protected ServiceRegistry serviceRegistry;
    protected ServiceProvider serviceProvider;

    public void scanServices() {
        // 获取main()入口所在类的类名，即启动类
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            // 获取启动类对应的实例对象
            startClass = Class.forName(mainClassName);
            // 判断启动类是否存在ServiceScan注解
            if(!startClass.isAnnotationPresent(ServiceScan.class)) {
                logger.error("启动类缺少@ServiceScan注解");
                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            logger.info("出现未知错误");
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }
        // 获取ServiceScan注解接口对应的value()的值，默认设置的""
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if("".equals(basePackage)) {
            // 获取启动类所在的包，因为服务类也放在这个包下面
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }
        // 获取包下面的所有类Class对象
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for(Class<?> clazz : classSet) {
            // 利用Service注解判断该类是否是服务类
            if(clazz.isAnnotationPresent(Service.class)) {
                // 获取Service注解接口对应的name()的值，默认设置的""
                String serviceName = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    // 创建服务Impl类的实例
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("创建" + clazz + "时有错误发生");
                    continue;
                }
                if("".equals(serviceName)) {
                    // 一个服务Impl类可能实现了多个服务接口
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for(Class<?> oneInterface : interfaces) {
                        publishService(obj, oneInterface.getCanonicalName());
                    }
                } else {
                    publishService(obj, serviceName);
                }
            }
        }
    }

    @Override
    public <T> void publishService(T service, String serviceName) {
        serviceProvider.addServiceProvider(service, serviceName);
        serviceRegistry.register(serviceName, new InetSocketAddress(host, port));
    }

    public final void launchServer() {
        scanServices();
        start();
    }
}
