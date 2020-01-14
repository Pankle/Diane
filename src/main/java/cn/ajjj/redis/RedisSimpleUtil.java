package cn.ajjj.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class RedisSimpleUtil {
    private JedisCluster client = null;
    private static RedisSimpleUtil ourInstance = new RedisSimpleUtil();
    public static JedisCluster getInstance() {
        return ourInstance.client;
    }

    private RedisSimpleUtil() {

        init();

    }
    private void init()  {

        JedisPoolConfig jedisPoolConfig = generateRedisConfig();
        String[] strings = "hostname:7000,hostname:7001,hostname:7002".split(",");
        HashSet<String> hashSet = new HashSet<String>(strings.length);
        for (String string : strings) {
            hashSet.add(string.trim());
        }
        Set<String> redisServer = hashSet;
        if (redisServer == null) {
            throw new RuntimeException("获取配置失败");
        }
        HashSet<InetSocketAddress> nodes = new HashSet<InetSocketAddress>();
        InetSocketAddress node = null;
        for (String server : redisServer) {
            String[] addrPort = server.split(":");
            if (addrPort.length == 2) {
                node = new InetSocketAddress(addrPort[0], Integer.parseInt(addrPort[1]));
                nodes.add(node);
            }
        }
        System.out.println("读取redis服务器个数：【{}】"+ nodes.size());
        HashSet<HostAndPort> jedisClientNodes = new HashSet<HostAndPort>();
        HostAndPort hostAndPort = null;
        for (InetSocketAddress addr : nodes) {
            hostAndPort = new HostAndPort(addr.getHostName(), addr.getPort());
            jedisClientNodes.add(hostAndPort);
        }

        client = new JedisCluster(jedisClientNodes, 1000, 1, jedisPoolConfig);
    }

    private JedisPoolConfig generateRedisConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 设置最大实例总数
        poolConfig.setMaxTotal(10000);
        // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        poolConfig.setMaxIdle(20);
        poolConfig.setMinIdle(5);
        poolConfig.setMaxWaitMillis(1000);
        return poolConfig;
    }
}
