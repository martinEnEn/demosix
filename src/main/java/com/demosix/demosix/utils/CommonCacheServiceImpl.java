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

import com.alibaba.fastjson.JSON;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 通用缓存服务
 * 
 * @author zhaojh
 * @date 2018年9月7日
 */
@Service
public class CommonCacheServiceImpl implements CommonCacheService {

	/** 日志信息 */
	private static final Logger logger = LoggerFactory.getLogger(CommonCacheServiceImpl.class);

	private static final Long RELEASE_SUCCESS = 1L;

	private static final String LOCK_SUCCESS = "OK";

	private static final String SET_IF_NOT_EXIST = "NX";

	// 当前设置 过期时间单位, EX = seconds; PX = milliseconds
	private static final String SET_WITH_EXPIRE_TIME = "EX";

	// if get(key) == value return del(key)
	private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";


	@Autowired
	private StringRedisTemplate template;


	/**
	 * 构造函数
	 * 
	 * @param template
	 */
	public CommonCacheServiceImpl(StringRedisTemplate template) {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.niubang.uc.cache.redis.CommonCacheService#setStrValue(java.lang.
	 * String , java.lang.String, long)
	 */
	@Override
	public void setStrValue(String key, String value, long expireTime) {
		logger.info("<[info=setStrValue缓存],[msg=key:{} &&& value:{} &&& expireTime:{}]>", key, value, expireTime);
		template.opsForValue().set(key, value);
		if (expireTime > 0) {
			template.expire(key, expireTime, TimeUnit.MILLISECONDS);
		}

		template.boundHashOps("").entries();
		template.opsForHash().get("", "");
	}
	public void setLockValue(){

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.niubang.uc.cache.redis.CommonCacheService#setStrValue(java.lang.
	 * String , java.lang.String)
	 */
	@Override
	public void setStrValue(String key, String value) {
		setStrValue(key, value, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.niubang.uc.cache.redis.CommonCacheService#getValue(java.lang.String)
	 */
	@Override
	public String getValue(String key) {
		String value = template.opsForValue().get(key);
		logger.info("<[info=getValue获取缓存],[msg=key:{} &&& value:{}]>", key, value);

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.niubang.uc.cache.redis.CommonCacheService#pushStr(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public long pushStr(String key, String value) {
		logger.info("<[info=push缓存],[msg=key:{} &&& value:{}]>", key, value);
		return template.opsForList().leftPush(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.niubang.uc.cache.redis.CommonCacheService#pop(java.lang.String)
	 */
	@Override
	public String pop(String key) {
		String popStr = template.opsForList().leftPop(key);
		logger.info("<[info=pop缓存],[msg=key:{} &&& popStr:{}]>", key, popStr);
		return popStr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.niubang.uc.cache.redis.CommonCacheService#in(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public long in(String key, String value) {
		logger.info("<[info=in缓存],[msg=key:{} &&& value:{}]>", key, value);
		return template.opsForList().rightPush(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.niubang.uc.cache.redis.CommonCacheService#out(java.lang.String)
	 */
	@Override
	public String out(String key) {
		String outStr = template.opsForList().leftPop(key);
		logger.info("<[info=out缓存],[msg=key:{} &&& outStr:{}]>", key, outStr);
		return outStr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.niubang.uc.cache.redis.CommonCacheService#length(java.lang.String)
	 */
	@Override
	public long length(String key) {
		long len = template.opsForList().size(key);
		logger.info("<[info=length缓存],[msg=key:{} &&& len:{}]>", key, len);
		return len;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.niubang.uc.cache.redis.CommonCacheService#range(java.lang.String,
	 * int, int)
	 */
	@Override
	public List<String> range(String key, long start, long end) {
		List<String> strList = template.opsForList().range(key, start, end);
		StringBuilder sb = new StringBuilder();
		for (String string : strList) {
			sb.append(string).append("=");
		}
		logger.info("<[info=range缓存],[msg=key:{} &&& start:{} &&&  end:{} &&& strList:{}]>", key, start, end,
				sb.toString());
		return strList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.niubang.uc.cache.redis.CommonCacheService#remove(java.lang.String,
	 * long, java.lang.String)
	 */
	@Override
	public void remove(String key, long i, String value) {
		template.opsForList().remove(key, i, value);
		logger.info("<[info=remove移除缓存],[msg=key:{} &&& index:{} &&&  value:{}]>", key, i, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.niubang.uc.cache.redis.CommonCacheService#getIndexValue(java.lang
	 * .String, long)
	 */
	@Override
	public String getIndexValue(String key, long index) {
		String rs = template.opsForList().index(key, index);
		logger.info("<[info=getIndexValue获取缓存],[msg=key:{} &&& index:{} &&&  value:{}]>", key, index, rs);
		return rs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.niubang.uc.cache.redis.CommonCacheService#setIndexValue(java.lang
	 * .String, long, java.lang.String)
	 */
	@Override
	public void setIndexValue(String key, long index, String value) {
		template.opsForList().set(key, index, value);
		logger.info("<[info=setIndexValue设置缓存],[msg=key:{} &&& long:{} &&& value:{}]>", key, index, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.niubang.uc.cache.redis.CommonCacheService#trim(java.lang.String,
	 * long, long)
	 */
	@Override
	public void trim(String key, long start, long end) {
		template.opsForList().trim(key, start, end);
		logger.info("<[info=trim截取缓存],[msg=key:{} &&& start:{} &&& end:{}]>", key, start, end);
	}

	@Override
	public void setObject(String key, Object o) {
		String jsonStr = JSON.toJSONString(o);
		this.setStrValue(key, jsonStr);
		logger.info("<[info=setObject插入缓存],[msg=key:{} &&& jsonStr:{}]>", key, jsonStr);
	}

	@Override
	public Object getObject(String key, Class<?> cls) {
		String text = this.getValue(key);
		if (StringUtils.isEmpty(text)) {
			return null;
		}
		Object rs = JSON.parseObject(text, cls);
		logger.info("<[info=getObject获取缓存],[msg=key:{} &&& jsonStr:{}]>", key, text);
		return rs;
	}

	@Override
	public void setObject(String key, Object o, long expireTime) {
		if (null == o) {
			return;
		}
		String jsonStr = JSON.toJSONString(o);
		this.setStrValue(key, jsonStr, expireTime);
		logger.info("<[info=setObject插入缓存],[msg=key:{} &&& jsonStr:{}]>", key, jsonStr);
	}

	@Override
	public void delKey(String key) {
		template.delete(key);
		logger.info("<[info=delKey删除缓存],[msg=key:{}]>", key);
	}

	@Override
	public void delKeys(String... keys) {
		List<String> keyList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keys.length; i++) {
			keyList.add(keys[i]);
			sb.append(keys[i]).append("=");
		}
		template.delete(keyList);
		logger.info("<[info=delKeys删除缓存],[msg=keys:{}]>", sb);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.niubang.uc.cache.redis.CommonCacheService#exist(java.lang.String)
	 */
	@Override
	public boolean exist(String key) {
		String val = this.getValue(key);
		long len = this.length(key);
		if (StringUtils.isEmpty(val) && len == 0) {
			logger.info("<[info=exist 缓存],[msg=keys:{} &&& rs:{}]>", key, false);
			return false;
		}
		logger.info("<[info=exist 缓存],[msg=keys:{} &&& rs:{}]>", key, true);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.niubang.uc.cache.redis.CommonCacheService#getAll(java.lang.String)
	 */
	@Override
	public List<String> getAll(String key) {
		long len = length(key);
		return range(key, 0, len - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.niubang.uc.cache.redis.CommonCacheService#getHAll(java.lang.String)
	 */
	@Override
	public Map<Object, Object> getHAll(String key) {
		return template.boundHashOps(key).entries();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.niubang.uc.cache.redis.CommonCacheService#getHValue(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Object getHValue(String key, String hKey) {
		Map<Object, Object> map = this.getHAll(key);
		if (null != map) {
			return map.get(hKey);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.niubang.uc.cache.redis.CommonCacheService#setHValue(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void setHValue(String key, String hKey, String value) {
		template.boundHashOps(key).put(hKey, value);
	}

	@Override
	public long increment(String key, long delta) {
		return template.boundValueOps(key).increment(delta);
	}

	@Override
	public long increment(String key) {
		return increment(key, 1L);
	}

	@Override
	public String getOrderCode() {
//		String pre = DateUtils.format("yyMMddHHmmssSSS");
//		return pre + String.format("%03d", increment(CachePrefixConstant.ORDER_CODE_PRE + pre));
		return null;

	}

	/**
	 * 获取redis 分布式锁
	 * @param key key
	 * @param value value
	 * @param seconds seconds
	 * @return
	 */
	@Override
	public boolean tryLock(String key, String value, long seconds) {

		Jedis jedis = new Jedis();
		String s = jedis.set(key, value, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, seconds);
		System.out.println("-----------分布式锁:"+s);
		return LOCK_SUCCESS.equals(s);
//		});
//		return true;
	}

	/**
	 * redis分布式锁解锁
	 * @param lockKey lockKey
	 * @param value value
	 * @return
	 */
	@Override
	public boolean releaseLock(String lockKey, String value) {

		Jedis jedis = new Jedis();
		Object result = jedis.eval(RELEASE_LOCK_SCRIPT, Collections.singletonList(lockKey),
				Collections.singletonList(value));

		System.out.println("-------------分布式解锁"+result);
		return true;
//		return template.execute((RedisCallback<Boolean>) connection -> {
//			Jedis jedis = (Jedis) connection.getNativeConnection();
//			Object result = jedis.eval(RELEASE_LOCK_SCRIPT, Collections.singletonList(lockKey),
//					Collections.singletonList(value));
//			return RELEASE_SUCCESS.equals(result);
//		});
	}


}
