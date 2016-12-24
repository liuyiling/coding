package com.liuyiling.common.util;

import redis.clients.jedis.Tuple;

import java.io.*;
import java.util.*;

/**
 * mc与redis等缓存的key仅支持byte数组
 * 当需要将对象序列化成byte数组
 * 你可以使用该类来进行处理
 *
 * Created by liuyl on 2016/12/20.
 */
public class CodecHandler {

    public static <T extends Serializable> T decode(byte[] bytes, Class<T> clazz) {
        if (bytes == null) {
            return null;
        }

        if (clazz == String.class) {
            return (T) new String(bytes);
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(new String(bytes));
        }
        if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(new String(bytes));
        }
        if (clazz == float.class || clazz == Float.class) {
            return (T) Float.valueOf(new String(bytes));
        }
        if (clazz == double.class || clazz == Double.class) {
            return (T) Double.valueOf(new String(bytes));
        }
        if (clazz.isArray() && clazz.getComponentType() == byte.class) {
            return (T) bytes;
        }

        T t;
        try {
            ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bytes));
            t = (T) oin.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error decoding byte[] data to instantiate java object - "
                    + "data at key may not have been of this type or even an object", e);
        }
        return t;
    }

    public static <T extends Serializable> byte[] encode(T obj) {
        if (obj instanceof String) {
            return ((String) obj).getBytes();
        }
        if (obj instanceof Number) {
            return String.valueOf(obj).getBytes();
        }
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }

        byte[] bytes;
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(obj);
            bytes = bout.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error serializing object" + obj + " => " + e);
        }
        return bytes;
    }

   public static <T extends Serializable> Map<T, Double> decodeTuples(Set<Tuple> tuples, Class<T> clazz) {
        Map<T, Double> result = new LinkedHashMap<T, Double>(tuples.size());
        for (Tuple tuple : tuples) {
            result.put(decode(tuple.getBinaryElement(), clazz), tuple.getScore());
        }
        return result;
    }

    public static <T extends Serializable> List<T> decodeList(List<byte[]> temp, Class<T> clazz) {
        List<T> result = new ArrayList<T>(temp.size());
        for (byte[] bytes : temp) {
            result.add(decode(bytes, clazz));
        }
        return result;
    }

    public static <T extends Serializable> Set<T> decodeSet(Set<byte[]> temp, Class<T> clazz) {
        Set<T> result = new HashSet<T>(temp.size());
        for (byte[] bytes : temp) {
            result.add(decode(bytes, clazz));
        }
        return result;
    }

    public static <T extends Serializable> byte[][] encodeParams(T[] params) {
        byte[][] result = new byte[params.length][];
        for (int i = 0; i < params.length; i++) {
            result[i] = encode(params[i]);
        }
        return result;
    }


}
