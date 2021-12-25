package com.liuhao.rpc.transport.netty.server;

import com.liuhao.rpc.handler.RequestHandler;
import com.liuhao.rpc.entity.RpcRequest;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {


    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private RequestHandler requestHandler;

    public NettyServerHandler() {
        requestHandler = new RequestHandler();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                logger.info("长时间未收到心跳包，断开连接...");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) {
        try{
            if(msg.getHeartBeat()) {
                logger.info("接收到客户端心跳包");
                return;
            }
            logger.info("服务端接收到请求：{}", msg);
            Object response = requestHandler.handle(msg);
            // 注意这里的通道是workGroup中的，而NettyServer中创建的是bossGroup的，不要混淆
            ChannelFuture future = ctx.writeAndFlush(response);
            // 添加一个监听器到ChannelFuture来检测是否所有的数据包都发出，然后关闭通道
            future.addListener(ChannelFutureListener.CLOSE);
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("处理过程调用时有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }

}
