package com.liuhao.rpc.test.socket;

import com.liuhao.rpc.annotation.ServiceScan;
import com.liuhao.rpc.serializer.CommonSerializer;
import com.liuhao.rpc.transport.socket.server.SocketServer;

@ServiceScan
public class SocketTestServer {
    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer("127.0.0.1", 9998, CommonSerializer.PROTOBUF_SERIALIZER);
        socketServer.launchServer();
    }
}
