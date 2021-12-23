package com.liuhao.rpc;

import com.liuhao.rpc.serializer.CommonSerializer;

public interface RpcServer {
    void start(int port);
    void setSerializer(CommonSerializer serializer);
}
