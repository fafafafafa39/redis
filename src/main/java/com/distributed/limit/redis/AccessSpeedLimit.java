package com.distributed.limit.redis;

import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * 分布式速率限制 例如：
 * @author xuze
 * @create 2019-06-19 18:06
 */
public class AccessSpeedLimit {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AccessSpeedLimit.class);

    private JedisPool jedisPool;

    public AccessSpeedLimit() {
    }

    public AccessSpeedLimit(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 针对资源key，每seconds秒最多访问maxCount次，超过maxCount次返回false
     * @param key
     * @param seconds
     * @param limitCount
     * @return
     */
    public boolean tryAccess(String key,int seconds,int limitCount){
        LimitRule limitRule = new LimitRule();
        limitRule.setLimitCount(limitCount);
        limitRule.setSeconds(seconds);
        return tryAccess(key,limitRule);
    }

    /**
     * 针对资源key，没limitrule.seconds秒最多访问limitRule.limitCount,超过limitCount次返回false
     * 超过lockCount 锁定lockTime
     * @param key
     * @param limitRule
     * @return
     */
    public boolean tryAccess(String key,LimitRule limitRule){
        String newKey = "Limit:"+key;
        Jedis jedis = null;
        boolean broken = false;
        long count = -1;
        try {
            jedis = jedisPool.getResource();
            List<String> keys = new ArrayList<>();
            keys.add(newKey);
            List<String> args = new ArrayList<>();
            args.add(Math.max(limitRule.getLimitCount(),limitRule.getLockTime())+"");
            args.add(limitRule.getSeconds()+"");
            args.add(limitRule.getLockCount()+"");
            args.add(limitRule.getLockTime()+"");
            count=Long.parseLong(jedis.eval(buildLuaScript(limitRule),keys,args)+"");
            return count<=limitRule.getLimitCount();
        }finally {
            if(jedis!=null)jedis.close();
        }
    }

    private String buildLuaScript(LimitRule limitRule){
        StringBuilder lua = new StringBuilder();
        lua.append("\nlocal c");
        lua.append("\nc = redis.call('get',KEYS[1])");
        lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");

        return lua.toString()
    }






}
