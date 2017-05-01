package com.framework.common.utils;

import java.util.*;

public class TimeCacheMap<K, V> {
    //this default ensures things expire at most 50% past the expiration time
    private static final int DEFAULT_NUM_BUCKETS = 10;

    //回调函数实现这个接口就可以，至少可以把删掉的元素传回去
    public static interface ExpiredCallback<K, V> {
        public void expire(K key, V val);
    }

    //把数据分成多个桶，用链表是因为在头尾的增减操作时O（1）
    private LinkedList<HashMap<K, V>> _buckets;

    private final Object _lock = new Object();
    private Thread _cleaner;
    private ExpiredCallback _callback;

    public TimeCacheMap(int expirationSecs, int numBuckets, ExpiredCallback<K, V> callback) {
        if (numBuckets < 2) {
            throw new IllegalArgumentException("numBuckets must be >= 2");
        }
        //构造函数中，按照桶的数量，初始桶
        _buckets = new LinkedList<HashMap<K, V>>();
        for (int i = 0; i < numBuckets; i++) {
            _buckets.add(new HashMap<K, V>());
        }


        _callback = callback;
        final long expirationMillis = expirationSecs * 1000L;
        final long sleepTime = expirationMillis / (numBuckets - 1);
        _cleaner = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        Map<K, V> dead = null;
                        Thread.sleep(sleepTime);
                        synchronized (_lock) {
                            //删掉最后一个桶，在头补充一个新的桶，最后一个桶的数据是最旧的
                            dead = _buckets.removeLast();
                            _buckets.addFirst(new HashMap<K, V>());
                        }
                        if (_callback != null) {
                            for (Map.Entry<K, V> entry : dead.entrySet()) {
                                _callback.expire(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                } catch (InterruptedException ex) {

                }
            }
        });
        //作为守护线程运行，一旦主线程不在，这个线程自动结束
        _cleaner.setDaemon(true);
        _cleaner.start();
    }

    public TimeCacheMap(int expirationSecs, ExpiredCallback<K, V> callback) {
        this(expirationSecs, DEFAULT_NUM_BUCKETS, callback);
    }

    public TimeCacheMap(int expirationSecs) {
        this(expirationSecs, DEFAULT_NUM_BUCKETS);
    }

    public TimeCacheMap(int expirationSecs, int numBuckets) {
        this(expirationSecs, numBuckets, null);
    }


    public boolean containsKey(K key) {
        synchronized (_lock) {
            for (HashMap<K, V> bucket : _buckets) {
                if (bucket.containsKey(key)) {
                    return true;
                }
            }
            return false;
        }
    }

    public V get(K key) {
        synchronized (_lock) {
            for (HashMap<K, V> bucket : _buckets) {
                if (bucket.containsKey(key)) {
                    return bucket.get(key);
                }
            }
            return null;
        }
    }

    public void put(K key, V value) {
        synchronized (_lock) {
            Iterator<HashMap<K, V>> it = _buckets.iterator();
            HashMap<K, V> bucket = it.next();
            //在第一个桶上更新数据
            bucket.put(key, value);
            //去掉后面桶的数据
            while (it.hasNext()) {
                bucket = it.next();
                bucket.remove(key);
            }
        }
    }

    public Object remove(K key) {
        synchronized (_lock) {
            for (HashMap<K, V> bucket : _buckets) {
                if (bucket.containsKey(key)) {
                    return bucket.remove(key);
                }
            }
            return null;
        }
    }

    public int size() {
        synchronized (_lock) {
            int size = 0;
            for (HashMap<K, V> bucket : _buckets) {
                size += bucket.size();
            }
            return size;
        }
    }

    public Set<K> keySet() {
        Set<K> keySet = new HashSet<K>();
        synchronized (_lock) {
            for (HashMap<K, V> bucket : _buckets) {
                keySet.addAll(bucket.keySet());
            }
        }
        return keySet;
    }

    //这个方法也太迷惑人了，作用就是把清理线程杀掉，这样数据就不会过期了，应该改名叫neverCleanup
    public void cleanup() {
        //中断清理线程中的sleep，_cleaner线程会抛出异常，然后_cleaner线程就死了，不再清理过期数据了
        _cleaner.interrupt();  //调用了interrupt后，再跑sleep就会抛InterruptedException异常

    }
}