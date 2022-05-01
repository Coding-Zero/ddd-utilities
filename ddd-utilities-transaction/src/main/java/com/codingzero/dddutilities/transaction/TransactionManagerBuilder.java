package com.codingzero.dddutilities.transaction;

import com.codingzero.dddutilities.transaction.manager.DefaultTransactionManagerBuilder;

public abstract class TransactionManagerBuilder {

    protected TransactionManagerBuilder() {

    }

    public static TransactionManagerBuilder create() {
        return new DefaultTransactionManagerBuilder();
    }

    abstract public TransactionManager build();

}
