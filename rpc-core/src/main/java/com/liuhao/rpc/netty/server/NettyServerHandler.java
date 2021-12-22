package com.liuhao.rpc.netty.server;

import com.liuhao.rpc.RequestHandler;
import com.liuhao.rpc.entity.RpcRequest;
import com.liuhao.rpc.entity.RpcResponse;
import com.liuhao.rpc.register.DefaultServiceRegistry;
import com.liuhao.rpc.register.ServiceRegistry;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {


    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static ServiceRegistry serviceRegistry;

    static{
        // 在之前注册的时候，是静态实例，相当于单例
        requestHandler = new RequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            logger.info("服务端接受请求：{}", msg);
            String interfaceName = msg.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = requestHandler.handle(msg, service);
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result));
//            ChannelFuture future = ctx.writeAndFlush(result);
            // 添加一个监听器到channelFuture来检测是否所有的数据包都发出，然后关闭通道
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程调用时有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }

}
