package cn.com.fd.active;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {

	private static String JEDIS_IP;
	private static int JEDIS_PORT;
	private static String JEDIS_PASSWORD;
	private static String JEDIS_SLAVE;

	private static JedisPool jedisPool;

	static {
		JEDIS_IP = "192.168.0.121";
		JEDIS_PORT = 6379;
		JEDIS_PASSWORD = null;
		
		JedisPoolConfig config = new JedisPoolConfig();
		
		config.setMaxTotal(Integer.valueOf(240));
		config.setMaxIdle(16);
		config.setMinIdle(8);
		config.setMaxWaitMillis(12000);
		config.setTestOnBorrow(true);//在获取连接的时候检查有效性, 默认false
		config.setTestOnReturn(true);//在返回的时候检查有效性
		config.setTestWhileIdle(true);//在空闲时检查有效性, 默认false
		config.setMinEvictableIdleTimeMillis(1800000);//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		config.setTimeBetweenEvictionRunsMillis(3000l);//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		config.setNumTestsPerEvictionRun(3);//每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
		
		jedisPool = new JedisPool(config, JEDIS_IP, JEDIS_PORT, 60000);
	}

	/**
	 * 获取数据
	 * @param key
	 * @return
	 */
	public static String get(String key) {

		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}

		return value;
	}

	public static void close(Jedis jedis) {
		try {
			jedisPool.returnResource(jedis);

		} catch (Exception e) {
			if (jedis.isConnected()) {
				jedis.quit();
				jedis.disconnect();
			}
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] get(byte[] key) {

		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}

		return value;
	}

	public static void set(byte[] key, byte[] value) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}

	public static void set(byte[] key, byte[] value, int time) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
			jedis.expire(key, time);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}

	public static void hset(byte[] key, byte[] field, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}

	public static void hset(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static String hget(String key, String field) {

		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.hget(key, field);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}

		return value;
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] hget(byte[] key, byte[] field) {

		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.hget(key, field);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}

		return value;
	}

	public static void hdel(byte[] key, byte[] field) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hdel(key, field);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}

	/**
	 * 存储REDIS队列 顺序存储
	 * @param byte[] key reids键名
	 * @param byte[] value 键值
	 */
	public static void lpush(byte[] key, byte[] value) {

		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			jedis.lpush(key, value);

		} catch (Exception e) {

			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			//返还到连接池
			close(jedis);

		}
	}

	/**
	 * 存储REDIS队列 反向存储
	 * @param byte[] key reids键名
	 * @param byte[] value 键值
	 */
	public static void rpush(byte[] key, byte[] value) {

		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			jedis.rpush(key, value);

		} catch (Exception e) {

			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			//返还到连接池
			close(jedis);

		}
	}

	/**
	 * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
	 * @param byte[] key reids键名
	 * @param byte[] value 键值
	 */
	public static void rpoplpush(byte[] key, byte[] destination) {

		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			jedis.rpoplpush(key, destination);

		} catch (Exception e) {

			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			//返还到连接池
			close(jedis);

		}
	}

	/**
	 * 获取队列数据
	 * @param byte[] key 键名
	 * @return
	 */
	public static List lpopList(byte[] key) {

		List list = null;
		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			list = jedis.lrange(key, 0, -1);

		} catch (Exception e) {

			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			//返还到连接池
			close(jedis);

		}
		return list;
	}

	/**
	 * 获取队列数据
	 * @param byte[] key 键名
	 * @return
	 */
	public static byte[] rpop(byte[] key) {

		byte[] bytes = null;
		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			bytes = jedis.rpop(key);

		} catch (Exception e) {

			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			//返还到连接池
			close(jedis);

		}
		return bytes;
	}

	public static void hmset(Object key, Map hash) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hmset(key.toString(), hash);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			//返还到连接池
			close(jedis);

		}
	}

	public static void hmset(Object key, Map hash, int time) {
		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			jedis.hmset(key.toString(), hash);
			jedis.expire(key.toString(), time);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			//返还到连接池
			close(jedis);

		}
	}

	public static List hmget(Object key, String... fields) {
		List result = null;
		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			result = jedis.hmget(key.toString(), fields);

		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			//返还到连接池
			close(jedis);

		}
		return result;
	}

	public static Set hkeys(String key) {
		Set result = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = jedis.hkeys(key);

		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			//返还到连接池
			close(jedis);

		}
		return result;
	}

	public static List lrange(byte[] key, int from, int to) {
		List result = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = jedis.lrange(key, from, to);

		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			//返还到连接池
			close(jedis);

		}
		return result;
	}

	public static Map hgetAll(byte[] key) {
		Map result = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = jedis.hgetAll(key);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			//返还到连接池
			close(jedis);
		}
		return result;
	}

	public static void del(byte[] key) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}

	public static long llen(byte[] key) {

		long len = 0;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.llen(key);
		} catch (Exception e) {
			//释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
		return len;
	}
}
