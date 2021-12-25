package com.liuhao.rpc.test.netty2;

import com.liuhao.rpc.annotation.ServiceScan;
import com.liuhao.rpc.api.HelloService;
import com.liuhao.rpc.serializer.CommonSerializer;
import com.liuhao.rpc.transport.netty.server.NettyServer;

@ServiceScan
public class NettyTestServer2 {
    static final String HOST = "127.0.0.1";
    static final int PORT = 9995;
    public static void main(String[] args) {
        NettyServer server = new NettyServer(HOST, PORT, CommonSerializer.PROTOBUF_SERIALIZER);
        server.start();
    }
}
