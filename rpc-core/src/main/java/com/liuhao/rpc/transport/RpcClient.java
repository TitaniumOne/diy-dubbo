package com.liuhao.rpc.transport;

import com.liuhao.rpc.entity.RpcRequest;
import com.liuhao.rpc.serializer.CommonSerializer;

public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);
}
