package com.liuhao.rpc.entity;

import com.liuhao.rpc.enumeration.ResponseCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 如果调用成功的话，显然需要返回值，
 * 如果调用失败了，就需要失败的信息，这里封装一个RpcResponse对象作为返回
 */
@Data
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {
    /**
     * 状态响应码
     */
    private Integer statusCode;
    /**
     * 状态响应码对应的信息
     */
    private String message;
    /**
     * 成功时的响应数据
     */
    private T data;

    /**
     * 成功时服务端返回的对象
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RpcResponse<T> success(T data) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
