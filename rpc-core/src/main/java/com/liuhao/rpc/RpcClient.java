package com.liuhao.rpc;

import com.liuhao.rpc.entity.RpcRequest;
import com.liuhao.rpc.serializer.CommonSerializer;

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer serializer);
}
