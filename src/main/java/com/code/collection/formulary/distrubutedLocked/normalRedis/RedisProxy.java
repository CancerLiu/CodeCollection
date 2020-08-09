package com.code.collection.formulary.distrubutedLocked.normalRedis;

import com.code.utils.JsonMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 为了便于其他demo使用，这里提供基于jedis的redis普通事务锁。
 * 基于set nx语义。封装在Lock方法中。
 */
public class RedisProxy {

    private JedisCluster jedisCluster;

    private JedisPool jedisPool;


    private static final String OK = "OK";

    public enum EXPX {
        //秒
        EX,
        //毫秒
        PX;
    }

    public enum NXXX {
        //不覆盖设置
        NX,
        //覆盖设置
        XX;
    }

    public boolean set(final String key, final Object value, final NXXX nxxx, final EXPX expx, final long time) {
        return this.set(key, JsonMapper.buildNonNullMapper().toJson(value), nxxx, expx, time);
    }

    public boolean set(final String key, final Object value) {
        return this.set(key, value, NXXX.XX, EXPX.EX, Long.MAX_VALUE);
    }

    public boolean set(final String key, final Object value, Long seconds) {
        return this.set(key, value, NXXX.XX, EXPX.EX, seconds);
    }

    private int getSeconds(final EXPX expx, final long time) {
        if (time <= 0) {
            return 0;
        }

        int seconds = (int) Math.min(time, Integer.MAX_VALUE);
        if (expx == EXPX.PX) {
            seconds = seconds / 1000;
        }
        return seconds;
    }

    public boolean set(final String key, final String value, final NXXX nxxx, final EXPX expx, final long time) {
        //redis client有bug，nx可以，xx不行，所以有以下不可思议的代码
        if (jedisCluster != null) {
            if (nxxx == NXXX.NX) {
                return OK.equalsIgnoreCase(jedisCluster.set(key, value, nxxx.name(), expx.name(), time));
            } else {
                return OK.equalsIgnoreCase(jedisCluster.setex(key, getSeconds(expx, time), value));
            }
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                if (nxxx == NXXX.NX) {
                    return OK.equalsIgnoreCase(jedis.set(key, value, nxxx.name(), expx.name(), time));
                } else {
                    return OK.equalsIgnoreCase(jedis.setex(key, getSeconds(expx, time), value));
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    public boolean set(final String key, final String value) {
        return this.set(key, value, NXXX.XX, EXPX.EX, Long.MAX_VALUE);
    }

    public boolean set(final String key, final String value, Long seconds) {
        return this.set(key, value, NXXX.XX, EXPX.EX, seconds);
    }

    public boolean lock(final String key, final String value, Long seconds) {
        return this.set(key, value, NXXX.NX, EXPX.EX, seconds);
    }

    public boolean lock(final String key, Long seconds) {
        return this.set(key, "true", NXXX.NX, EXPX.EX, seconds);
    }

    public boolean lock(final String key) {
        return this.set(key, "true", NXXX.NX, EXPX.EX, Long.MAX_VALUE);
    }

    public String get(final String key) {
        if (jedisCluster != null) {
            return jedisCluster.get(key);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.get(key);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public Long del(final String... key) {
        if (jedisCluster != null) {
            return jedisCluster.del(key);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.del(key);
            } catch (Exception e) {
            }
        }
        return 0L;
    }

    public Long del(final String key) {
        return this.del(new String[]{key});
    }


    //TODO:更多方法待拓展
    //哈希类型基本操作
    public Map<String, String> hgetAll(String key) {
        if (null != jedisCluster) {
            return jedisCluster.hgetAll(key);
        } else if (null != jedisPool) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hgetAll(key);
            } catch (Exception e) {
            }
        }
        return null;
    }

    //todo 后期看能不能写Lua脚本保证设置键和设置键过期时间事务
    public boolean hmsetAndExpire(String key, Map<String, String> map, Integer seconds) {
        if (null != jedisCluster) {
            //因为设置键和设置键的过期时间两步不是原子性的，此处上锁保证两个命令同时执行，保证原子性;
            if (OK.equalsIgnoreCase(jedisCluster.hmset(key, map))) {
                jedisCluster.expire(key, seconds);
            }
            return true;
        } else if (null != jedisPool) {
            //此处锁住的原因同上，保证原子性
            try (Jedis jedis = jedisPool.getResource()) {
                if (OK.equalsIgnoreCase(jedis.hmset(key, map))) {
                    jedis.expire(key, seconds);
                }
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }


    public RedisProxy setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
        return this;
    }

    public RedisProxy setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        return this;
    }
}