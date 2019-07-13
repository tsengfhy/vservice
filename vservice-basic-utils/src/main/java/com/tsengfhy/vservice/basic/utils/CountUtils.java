package com.tsengfhy.vservice.basic.utils;

import lombok.experimental.UtilityClass;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@UtilityClass
public final class CountUtils {

    private static Cache cache;

    private static ReentrantLock lock = new ReentrantLock();

    public static void setCacheManager(CacheManager cacheManager) {
        cache = cacheManager.getCache(com.tsengfhy.vservice.basic.constant.Cache.COUNT.name());
    }

    public static long add(String type, String key) throws UnsupportedOperationException {
        try {
            lock.lock();
            long count = Optional.ofNullable(getCache().get(type.concat(":".concat(key)))).map(valueWrapper -> Long.parseLong(valueWrapper.get().toString()) + 1).orElse(1L);
            getCache().put(type.concat(":".concat(key)), count);
            return count;
        } finally {
            lock.unlock();
        }
    }

    public static long reduce(String type, String key) throws UnsupportedOperationException {
        try {
            lock.lock();
            long count = Optional.ofNullable(getCache().get(type.concat(":".concat(key)))).map(valueWrapper -> Long.parseLong(valueWrapper.get().toString()) - 1).orElse(-1L);
            getCache().put(type.concat(":".concat(key)), count);
            return count;
        } finally {
            lock.unlock();
        }
    }

    public static void clear(String type, String key) throws UnsupportedOperationException {
        try {
            lock.lock();
            getCache().evict(type.concat(":".concat(key)));
        } finally {
            lock.unlock();
        }
    }

    public static void clearAll() throws UnsupportedOperationException {
        try {
            lock.lock();
            getCache().clear();
        } finally {
            lock.unlock();
        }
    }

    private static Cache getCache() {
        return Optional.ofNullable(CountUtils.cache)
                .orElseThrow(() -> {
                    StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[5];
                    return new UnsupportedOperationException(MessageSourceUtils.getMessage("Common.methodNotSupport", new Object[]{stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + "()"}, "Method " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + "() not support"));
                });
    }
}
