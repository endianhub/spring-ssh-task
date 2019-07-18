package com.xh.ssh.web.task.common.tool;

import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * <b>Title: Redis</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年8月29日
 */
public class JedisClientUtils {

	private static int index = 7;
	/** 默认缓存时间 */
	private static final int DEFAULT_CACHE_SECONDS = 60 * 60 * 1;// 单位秒 设置成一个钟
	// public static JedisSentinelPool pool = null;
	public static JedisPool jedisPool = null;

	static {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		// 设置最大连接总数
		poolConfig.setMaxTotal(60);
		// 设置最大空闲数
		poolConfig.setMaxIdle(30);
		// 设置最小空闲数
		poolConfig.setMinIdle(8);
		// 设置最大等待时间
		poolConfig.setMaxWaitMillis(5 * 1000);
		// 在获取连接的时候检查有效性, 默认false
		poolConfig.setTestOnBorrow(false);
		// 在空闲时检查有效性, 默认false
		poolConfig.setTestOnReturn(false);
		// 是否启用pool的jmx管理功能, 默认true
		poolConfig.setJmxEnabled(true);
		// Idle时进行连接扫描
		poolConfig.setTestWhileIdle(true);
		// 是否启用后进先出, 默认true
		poolConfig.setLifo(true);
		// 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		// poolConfig.setTimeBetweenEvictionRunsMillis(-1);
		// 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
		// poolConfig.setNumTestsPerEvictionRun(10);
		// 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
		// poolConfig.setMinEvictableIdleTimeMillis(60000);
		// 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
		poolConfig.setBlockWhenExhausted(true);
		// 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
		// poolConfig.setSoftMinEvictableIdleTimeMillis(1800000);

		jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379);

		LogUtils.info(JedisClientUtils.class, "=== JedisClientTool init:" + jedisPool + " ===");
	}

	/**
	 * <b>Title: 处理JedisException，写入日志并返回连接是否中断。</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param jedisException
	 * @return
	 */
	public static void handleJedisException(Exception jedisException) {
		if (jedisException instanceof JedisConnectionException) {
			LogUtils.info(JedisClientUtils.class, "Redis connection lost.");
		} else if (jedisException instanceof JedisDataException) {
			if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
				LogUtils.info(JedisClientUtils.class, "Redis connection  are read-only slave.");
			}
		} else {
			LogUtils.info(JedisClientUtils.class, "Jedis exception happen.");
		}
	}

	/**
	 * <b>Title: 释放redis资源</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param jedis
	 */
	private static void releaseResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
			LogUtils.info(JedisClientUtils.class, "redis close.");
		}
	}

	/**
	 * <b>Title: 删除Redis中的所有key</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 */
	public static void flushAll() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.flushAll();
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache清空失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 保存一个对象到Redis中</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key 键
	 * @param value 缓存对象
	 * @return
	 */
	public static boolean set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);// 选择redis库号---不指定库号，默认存入到0号库
			jedis.set(key, value);
			return true;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache保存失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return false;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 保存一个对象到redis中</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param keyBytes 键
	 * @param valueBytes 缓存
	 * @return
	 */
	public static boolean set(byte[] keyBytes, byte[] valueBytes) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			jedis.set(keyBytes, valueBytes);
			return true;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache保存失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return false;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 根据缓存键获取Redis缓存中的值</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			return jedis.get(key);
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache获取失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return null;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 根据缓存键获取Redis缓存中的值</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] get(byte[] key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			return jedis.get(key);
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache获取失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return null;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 根据缓存键清除Redis缓存中的值</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param keys
	 * @return
	 */
	public static Boolean del(String... keys) {
		Jedis jedis = null;
		try {
			for (String key : keys) {
				jedis = jedisPool.getResource();
				jedis.select(index);
				jedis.del(key);
			}
			return true;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache删除失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return false;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 根据缓存键清除Redis缓存中的值</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param keys
	 * @return
	 */
	public static Boolean del(byte[]... keys) {
		Jedis jedis = null;
		try {
			for (byte[] key : keys) {
				jedis = jedisPool.getResource();
				jedis.select(index);
				jedis.del(keys);
			}
			return true;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache删除失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return false;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 设置超时</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @param seconds 超时时间（单位为秒）
	 * @return
	 */
	public static Boolean expire(String key, int seconds) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			jedis.expire(key, seconds);
			return true;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache设置超时时间失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return false;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 设置超时</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @param seconds 超时时间（单位为秒）
	 * @return
	 */
	public static Boolean expire(byte[] key, int seconds) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			jedis.expire(key, seconds);
			return true;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache设置超时时间失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return false;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 添加一个内容到指定key的hash中</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public static Boolean addHash(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			jedis.hset(key, field, value);
			return true;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache保存失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return false;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 添加一个内容到指定key的hash中</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public static Boolean addHash(byte[] key, byte[] field, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			jedis.hset(key, field, value);
			return true;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache保存失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return false;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 从指定hash中拿一个对象</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static String getHash(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			return jedis.hget(key, field);
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache读取失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return null;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 从指定hash中拿一个对象</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static byte[] getHash(byte[] key, byte[] field) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			byte[] obj = jedis.hget(key, field);
			return obj;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache读取失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return null;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 从hash中删除指定filed的值</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static Boolean delHash(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			long result = jedis.hdel(key, field);
			return result == 1;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache删除失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return null;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 从hash中删除指定filed的值</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static Boolean delHash(byte[] key, byte[] field) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			long result = jedis.hdel(key, field);
			return result == 1;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache删除失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return null;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 拿到缓存中所有符合pattern的key</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param pattern
	 * @return
	 */
	public static Set<String> getKeys(String pattern) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			Set<String> allKey = jedis.keys(("*" + pattern + "*"));
			return allKey;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache获取失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return null;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 拿到缓存中所有符合pattern的key</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param pattern
	 * @return
	 */
	public static Set<byte[]> getKeys2(String pattern) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			Set<byte[]> allKey = jedis.keys(("*" + pattern + "*").getBytes());
			return allKey;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache获取失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return null;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 获得hash中的所有key value</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @return
	 */
	public static Map<String, String> getAllHash(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			Map<String, String> map = jedis.hgetAll(key);
			return map;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache获取失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return null;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 获得hash中的所有key value</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @return
	 */
	public static Map<byte[], byte[]> getAllHash(byte[] key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			Map<byte[], byte[]> map = jedis.hgetAll(key);
			return map;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache获取失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return null;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 判断一个key是否存在</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @return
	 */
	public static Boolean exists(String key) {
		Jedis jedis = null;
		Boolean result = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			result = jedis.exists(key);
			return result;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache获取失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return false;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 判断一个key是否存在</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @return
	 */
	public static Boolean exists(byte[] key) {
		Jedis jedis = null;
		Boolean result = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);
			result = jedis.exists(key);
			return result;
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, "Cache获取失败：" + e.getMessage());
			LogUtils.error(JedisClientUtils.class, e);
			return false;
		} finally {
			releaseResource(jedis);
		}
	}

	/**
	 * <b>Title: 增加业务幂等性验证</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean isExist(String key) {
		Jedis jedis = null;
		boolean isExist = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);// 选择redis库号---不指定库号，默认存入到0号库
			// 如果数据存在则返回0，不存在返回1
			if (jedis.setnx(key, key) == 0) {
				return true;
			}
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, e);
			return false;
		} finally {
			// 业务操作完成，将连接返回给连接池
			releaseResource(jedis);
		}
		return isExist;
	}

	/**
	 * <b>Title: 增加业务幂等性验证</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean isExist(String key, String value) {
		Jedis jedis = null;
		boolean isExist = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(index);// 选择redis库号---不指定库号，默认存入到0号库
			// 如果数据存在则返回0，不存在返回1
			if (jedis.setnx(key, value) == 0) {
				return true;
			}
		} catch (Exception e) {
			handleJedisException(e);
			LogUtils.error(JedisClientUtils.class, e);
			return false;
		} finally {
			// 业务操作完成，将连接返回给连接池
			releaseResource(jedis);
		}
		return isExist;
	}

	public static void main(String[] args) {
		System.out.println();
		 set("AAAAAAAAAA", "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
	}
}
