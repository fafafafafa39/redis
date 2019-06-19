package com.distributed.lock;

/**
 * @author xuze
 * @create 2019-06-19 18:14
 */
public interface Callback {

    public Object onGetLock() throws InterruptedException;

    public Object onTimeOut() throws InterruptedException;
}
