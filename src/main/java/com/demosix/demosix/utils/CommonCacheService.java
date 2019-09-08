/*
 * Project: uc-common-cache
 * 
 * File Created at 2015-10-23
 * 
 * Copyright 2014 zhidier.com All right reserved. This software is the
 * confidential and proprietary information of zhidier.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with zhidier.com .
 */
package com.demosix.demosix.utils;

import java.util.List;
import java.util.Map;

/**
 * @Type CommonCacheService
 * @Desc 普通cache服务
 * @date 2015-10-23
 * @Version V1.0
 */
public interface CommonCacheService {

    /**
     * 设值
     * 
     * @param key
     * @param value
     * @param expireTime 过期时间
     */
    public void setStrValue(String key, String value, long expireTime);

    /**
     * 设值
     * 
     * @param key
     * @param value
     */
    public void setStrValue(String key, String value);

    /**
     * 获取值
     * 
     * @param key
     * @return
     */
    public String getValue(String key);

    /**
     * 入栈
     * 
     * @param key
     * @param value
     * @return 长度
     */
    public long pushStr(String key, String value);

    /**
     * 出栈
     * 
     * @param key
     * @return String
     */
    public String pop(String key);

    /**
     * 入队
     * 
     * @param key
     * @param value
     * @return 长度
     */
    public long in(String key, String value);

    /**
     * 出队
     * 
     * @param key
     * @return String
     */
    public String out(String key);

    /**
     * 队列或者栈的长度
     * 
     * @param key
     * @return 长度
     */
    public long length(String key);

    /**
     * 获取指定范围的队列或栈 的 value列表
     * 
     * @param key
     * @param start
     * @param end
     * @return List<String>
     */
    public List<String> range(String key, long start, long end);

    /**
     * 移除指定位置的值
     * 
     * @param key
     * @param i i > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 i 。 <br>
     *            i < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 i 的绝对值。 <br>
     *            i = 0 : 移除表中所有与 value 相等的值。
     * @param value
     */
    public void remove(String key, long i, String value);

    /**
     * 获取指定位置的值
     * 
     * @param key
     * @param index
     * @return
     */
    public String getIndexValue(String key, long index);

    /**
     * 设置指定位置的值
     * 
     * @param key
     * @param index
     * @param value
     */
    public void setIndexValue(String key, long index, String value);

    /**
     * 裁剪队列或者栈
     * 
     * @param key
     * @param start
     * @param end
     */
    public void trim(String key, long start, long end);

    /**
     * 泛型方法 对 对象进行json 序列化
     * 
     * @param key
     * @param o Object 对象
     */
    public void setObject(String key, Object o);

    /**
     * 泛型方法 对 对象进行json 序列化
     * 
     * @param key
     * @param o Object 对象
     * @param expireTime 过期时间
     */
    public void setObject(String key, Object o, long expireTime);

    /**
     * 获取对象
     * 
     * @param key
     * @param o 对象
     * @return
     */
    public Object getObject(String key, Class<?> cls);

    /**
     * 删除数据
     * 
     * @param key
     */
    public void delKey(String key);

    /**
     * 批量删除key
     * 
     * @param keys
     */
    public void delKeys(String... keys);

    /**
     * 缓存是否存在
     * 
     * @param key
     * @return true: 存在; false: 不存在
     */
    public boolean exist(String key);

    /**
     * 获取列表全部对象
     * 
     * @param key
     * @return
     */
    public List<String> getAll(String key);

    /**
     * 获取 key 的 map 对象
     * 
     * @param key 键
     * @return Map<Object, Object>
     */
    public Map<Object, Object> getHAll(String key);

    /**
     * 获取对应的 key的 map 对象里面的 hKey的值
     * 
     * @param key 主key
     * @param hKey map里面的key值
     * @return Object value 对象
     */
    public Object getHValue(String key, String hKey);

    /**
     * 设置对应的 key的 map 对象里面的 hKey的值
     * 
     * @param key 主key
     * @param hKey map里面的key值
     * @param value 对象
     * @return
     */
    public void setHValue(String key, String hKey, String value);
    
    /**
     * 将 key 中储存的数字值加上增量 
     * @param key 主key
     * @param delta 增量
     * @return
     */
    public long increment(String key, long delta);
    
    /**
     * 将 key 中储存的数字值加上1 
     * @param key 主key
     * @return
     */
    public long increment(String key);
    
    /**
     * 获取订单编号
     * @param key
     * @return
     */
    public String getOrderCode();

    /**
     * redis分布式锁
     * @param key key
     * @param value value
     * @param seconds seconds
     * @return boolean
     */
    public boolean tryLock(String key, String value, long seconds);

    /**
     * 解除redis分布式锁
     * @param lockKey lockKey
     * @param value value
     * @return boolean
     */
    public boolean releaseLock(String lockKey, String value);
}
