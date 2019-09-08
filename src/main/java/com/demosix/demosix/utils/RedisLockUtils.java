package com.demosix.demosix.utils;

import redis.clients.jedis.Jedis;

import java.util.Collections;

public class RedisLockUtils {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * 获取redis 分布式锁
     * @param key key
     * @param value value
     * @param seconds seconds
     * @return
     */
    public static boolean tryLock(String key, String value, long seconds) {
        Jedis jedis = new Jedis();
        String s = jedis.set(key, value, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, seconds);
        System.out.println("-----------分布式锁:"+s);
        return LOCK_SUCCESS.equals(s);
    }

    /**
     * redis分布式锁解锁
     * @param lockKey lockKey
     * @param value value
     * @return
     */
    public static boolean releaseLock(String lockKey, String value) {
        Jedis jedis = new Jedis();
        Object result = jedis.eval(RELEASE_LOCK_SCRIPT, Collections.singletonList(lockKey),
                Collections.singletonList(value));
        System.out.println("-------------分布式解锁"+result);
        return true;
    }
}
