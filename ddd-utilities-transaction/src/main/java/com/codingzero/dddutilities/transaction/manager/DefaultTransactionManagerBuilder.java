package com.codingzero.dddutilities.transaction.manager;

import com.codingzero.dddutilities.transaction.TransactionManager;
import com.codingzero.dddutilities.transaction.TransactionManagerBuilder;

public class DefaultTransactionManagerBuilder extends TransactionManagerBuilder {

    @Override
    public TransactionManager build() {
        return new TransactionManagerImpl();
    }

}
