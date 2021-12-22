package com.liuhao.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 服务端需要哪些信息，才能唯一确定服务端需要调用哪个接口的哪个方法呢？
 *
 * 首先，肯定要知道接口的名字和方法的名字，但是由于方法重载的缘故，还需要这个方法的所有参数的类型，
 * 最后，服务端处理时，还需要参数的实际值，那么服务端知道以上四个条件，就可以找到这个方法并且调用处理了。
 * 把这四个条件写到一个对象里，到时候传输时传输这个对象就行了。即RpcRequest对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {
    /**
     * 待调用接口名称
     */
    private String interfaceName;
    /**
     * 待调用方法名称
     */
    private String methodName;
    /**
     * 待调用方法的参数
     */
    private Object[] parameters;
    /**
     * 待调用方法的参数类型
     */
    private Class<?>[] paramTypes;
}
