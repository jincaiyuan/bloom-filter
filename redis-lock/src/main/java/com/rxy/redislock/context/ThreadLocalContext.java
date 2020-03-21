package com.rxy.redislock.context;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ThreadLocalContext {
    private static ThreadLocal<Map<String, Object>> ctx = new ThreadLocal<>();

    static {
        ctx.set(new HashMap<>());
    }

    public static void put(Map<String, Object> map) {
        Map<String, Object> map1 = ctx.get();
        if (map1 == null) {
            map1 = new HashMap<>();
            ctx.set(map1);
        }
        map1.putAll(map);
    }

    public static void put(String key, Object value) {
        Map<String, Object> map1 = ctx.get();
        if (map1 == null) {
            map1 = new HashMap<>();
            ctx.set(map1);
        }
        map1.put(key, value);
    }

    public static Map<String, Object> get() { return ctx.get(); }

    public static boolean contain(String key) {
        Map<String, Object> map = ctx.get();
        if (map != null) {
            return map.containsKey(key);
        }
        return false;
    }

    public static Object get(String  key) {
        Map<String, Object> map = ctx.get();
        if (map != null) {
            return map.get(key);
        }
        return null;
    }

    public static void remove(String key) {
        Map<String, Object> map = ctx.get();
        if (map != null) {
            map.remove(key);
        }
    }

}
