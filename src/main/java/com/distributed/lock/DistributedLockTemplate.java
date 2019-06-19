package com.distributed.lock;

/**
 * 分布式锁模板类
 * @author xuze
 * @create 2019-06-19 18:18
 */
public interface DistributedLockTemplate {
    /**
     *
     * @param lockId 锁id（对应业务唯一ID）
     * @param timeout 单位毫秒
     * @param callback 回调函数
     * @return
     */
    public Object execute(String lockId,int timeout,Callback callback);
}
