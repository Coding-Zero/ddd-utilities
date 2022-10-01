package com.codingzero.dddutilities.transaction.manager;

import com.codingzero.dddutilities.transaction.TransactionContext;
import com.codingzero.dddutilities.transaction.TransactionCoordinator;
import com.codingzero.dddutilities.transaction.TransactionalService;

import java.util.LinkedHashMap;
import java.util.Map;

public class TransactionCoordinatorHelper implements TransactionCoordinator {

    private Map<String, TransactionalService> services;
    private TransactionContext context;

    public TransactionCoordinatorHelper() {
        this.services = new LinkedHashMap<>();
        this.context = null;
    }

    private TransactionContext getContext() {
        return context;
    }

    @Override
    public void register(TransactionalService service) {
        String name = service.getClass().getCanonicalName();
        services.put(name.toLowerCase(), service);
        service.onRegister();
    }

    public TransactionalService deregister(String name) {
        return services.remove(name.toLowerCase());
    }

    @Override
    public void start() {
        this.context = new TransactionContext(this);
        for (TransactionalService service: services.values()) {
            service.onStart(getContext());
        }
    }

    @Override
    public void commit() {
        for (TransactionalService service: services.values()) {
            service.onCommit(getContext());
        }
        this.context = null;
    }

    @Override
    public void rollback() {
        for (TransactionalService service: services.values()) {
            service.onRollback(getContext());
        }
        this.context = null;
    }

}
