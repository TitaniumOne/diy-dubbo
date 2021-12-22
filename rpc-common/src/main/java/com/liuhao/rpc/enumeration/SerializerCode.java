package com.liuhao.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SerializerCode {
    KRYO(0),
    JSON(1);

    private final int code;
}
