package com.distributed.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author xuze
 * @create 2019-06-19 18:18
 */
public interface DistributedReentrantLock {
    public boolean tryLock(long timeout, TimeUnit util) throws InterruptedException;

    public void unlock();
}
