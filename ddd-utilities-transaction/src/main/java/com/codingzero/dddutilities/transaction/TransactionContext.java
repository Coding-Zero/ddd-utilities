package com.codingzero.dddutilities.transaction;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represent a transaction context;
 */
public class TransactionContext {

    private TransactionCoordinator manager;
    private Map<String, Object> caches;

    public TransactionContext(TransactionCoordinator manager) {
        this.manager = manager;
        this.caches = new LinkedHashMap<>();
    }

    public TransactionCoordinator getManager() {
        return manager;
    }

    public <T> void addCache(String key, T value) {
        caches.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getCache(String key) {
        return (T) caches.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T removeCache(String key) {
        return (T) caches.remove(key);
    }

}
