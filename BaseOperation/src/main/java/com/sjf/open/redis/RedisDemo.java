package com.sjf.open.redis;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * Created by xiaosi on 17-6-1.
 */
public class RedisDemo {

    private static Jedis jedis = new Jedis("localhost");

    public static void strTest(){
        jedis.set("name", "yoona");
        String name = jedis.get("name");
        System.out.println("name----"+name);
    }

    public static void hashTest(){
        jedis.hset("car.1", "name", "宝马");
        jedis.hset("car.1", "color", "黑色");
        String name = jedis.hget("car.1", "name");
        String color = jedis.hget("car.1", "color");
        System.out.println("name---"+name + "  color---"+color);
    }

    public static void listTest(){
        jedis.lpush("fruit", "apple");
        jedis.lpush("fruit", "pear");
        jedis.lpush("fruit", "banana");
        List<String> fruitList = jedis.lrange("fruit", 0, -1);
        System.out.println(fruitList.toString());
    }

    public static void setTest(){
        jedis.sadd("letters", "a");
        jedis.sadd("letters", "b");
        jedis.sadd("letters", "a");
        jedis.sadd("letters", "c");
        Set<String> letters = jedis.smembers("letters");
        System.out.println(letters);
    }

    public static void sortSetTest(){
        jedis.zadd("score", 89, "Tom");
        jedis.zadd("score", 67, "Petter");
        jedis.zadd("score", 100, "David");
        Set<String> scoreSet = jedis.zrange("score", 0, -1);
        System.out.println(scoreSet.toString());
    }

    public static void main(String[] args) {
        strTest();
        hashTest();
        listTest();
        setTest();
        sortSetTest();
    }
}
