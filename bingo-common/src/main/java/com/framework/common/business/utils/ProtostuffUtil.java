package com.framework.common.business.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffUtil {
    public static <T> byte[] serialize(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        LinkedBuffer buffer = LinkedBuffer.allocate(1024);
        byte[] data = null;
        try {
            data = ProtobufIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }

        return data;
    }

    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T message = schema.newMessage();
        ProtobufIOUtil.mergeFrom(data, message, schema);

        return message;
    }
}