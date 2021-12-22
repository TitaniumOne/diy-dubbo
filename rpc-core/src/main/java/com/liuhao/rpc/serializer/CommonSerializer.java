package com.liuhao.rpc.serializer;

import sun.java2d.opengl.OGLContext;

public interface CommonSerializer {
    byte[] serialize(Object obj);
    Object deserialize(byte[] bytes, Class<?> clazz);
    int getCode();

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
