package com.apachee.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/*
127.0.0.1:6379> set test 1
OK
127.0.0.1:6379> get test
"1"
127.0.0.1:6379> del test
(integer) 1
127.0.0.1:6379> get test
(nil)
127.0.0.1:6379> hset myhash username lisi
(integer) 1
127.0.0.1:6379> hset myhash password 123
(integer) 1
127.0.0.1:6379> hget myhash username
"lisi"
127.0.0.1:6379> hgetall myhash
1) "username"
2) "lisi"
3) "password"
4) "123"
127.0.0.1:6379> hdel myhash
(error) ERR wrong number of arguments for 'hdel' command
127.0.0.1:6379> hdel myhash usernmae
(integer) 0
127.0.0.1:6379> hget myhash usernmae
(nil)
127.0.0.1:6379> lpush name zhangsan
(integer) 1
127.0.0.1:6379> rpush name lisi
(integer) 2
127.0.0.1:6379> lrange name zhangsan lisi
(error) ERR value is not an integer or out of range
127.0.0.1:6379> lpop
(error) ERR wrong number of arguments for 'lpop' command
127.0.0.1:6379> lpush mylist a
(integer) 1
127.0.0.1:6379> lpush mylist b
(integer) 2
127.0.0.1:6379> rpush mylist c
(integer) 3
127.0.0.1:6379> lrange mylist 0 -1
1) "b"
2) "a"
3) "c"
127.0.0.1:6379> lpop mylist
"b"
127.0.0.1:6379> lrange mylist 0 -1
1) "a"
2) "c"
127.0.0.1:6379> rpop mylist
"c"
127.0.0.1:6379> lrange mylist 0 -1
1) "a"
127.0.0.1:6379> lpush mylist a
(integer) 2
127.0.0.1:6379> sadd myset a
(integer) 1
127.0.0.1:6379> sadd myset b
(integer) 1
127.0.0.1:6379> smembers myset
1) "b"
2) "a"
127.0.0.1:6379> sadd myset a b c d
(integer) 2
127.0.0.1:6379> smembers myset
1) "d"
2) "b"
3) "a"
4) "c"
127.0.0.1:6379> zadd mysort 60 zhangsan
(integer) 1
127.0.0.1:6379> zadd mysort 50 lisi
(integer) 1
127.0.0.1:6379> zadd mysort 40 wangwu
(integer) 1
127.0.0.1:6379> zrange mysort
(error) ERR wrong number of arguments for 'zrange' command
127.0.0.1:6379> zrange mysort 0 -1
1) "wangwu"
2) "lisi"
3) "zhangsan"
127.0.0.1:6379> zrange mysort 0 -1 withscores
1) "wangwu"
2) "40"
3) "lisi"
4) "50"
5) "zhangsan"
6) "60"
127.0.0.1:6379> zadd mysort 500 lisi
(integer) 0
127.0.0.1:6379> zrange mysort 0 -1 withscores
1) "wangwu"
2) "40"
3) "zhangsan"
4) "60"
5) "lisi"
6) "500"
127.0.0.1:6379> zrem mysort list
(integer) 0
127.0.0.1:6379> zrem mysort lisi
(integer) 1
127.0.0.1:6379> zrange mysort 0 -1 withscores
1) "wangwu"
2) "40"
3) "zhangsan"
4) "60"
127.0.0.1:6379> keys *
1) "mylist"
2) "myset"
3) "mysort"
4) "name"
5) "myhash"
127.0.0.1:6379> type username
none
127.0.0.1:6379> get mylist
(error) WRONGTYPE Operation against a key holding the wrong kind of value
127.0.0.1:6379> type myylist
none
127.0.0.1:6379> type mylist
list
127.0.0.1:6379> get mylist
(error) WRONGTYPE Operation against a key holding the wrong kind of value
127.0.0.1:6379> lrange mylist 0 -1
1) "a"
2) "a"
127.0.0.1:6379> keys *
1) "mylist"
2) "myset"
3) "mysort"
4) "name"
5) "myhash"
127.0.0.1:6379> smembers myset
1) "d"
2) "b"
3) "a"
4) "c"
127.0.0.1:6379> zrange mysort 0 -1
1) "wangwu"
2) "zhangsan"
127.0.0.1:6379> hgetall myhash
1) "username"
2) "lisi"
3) "password"
4) "123"
127.0.0.1:6379> keys *
1) "mylist"
2) "myset"
3) "mysort"
4) "name"
5) "myhash"
 */
public class Demo {
    Jedis jedis = null;

    @Before
    public void before(){
        jedis = new Jedis("localhost", 6379);
    }
    @After
    public void after(){
        jedis.close();
    }

    @Test
    public void test(){
        jedis.set("username", "zhangxudong");
        jedis.hset("myhash", "username", "zhangsan");
        jedis.lpush("mylist", "a", "b", "c");
        jedis.sadd("myset", "a", "a", "b", "b");
        jedis.zadd("mysort", 400,"abc");
        jedis.zadd("mysort", 500,"abc");
    }

    @Test
    public void test2(){
        jedis.setex("activecode", 20, "hehe");
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(1);
        JedisPool pool = new JedisPool(config,"localhost", 6379);
        Jedis jedis1 = pool.getResource();
        jedis1.close();
    }




}
