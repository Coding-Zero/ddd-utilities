package com.codingzero.dddutilities.transaction;

/**
 * This is an observer for getting notifications for different states of a transaction.
 *
 */
public interface TransactionalService {

    void onRegister();

    void onStart(TransactionContext context);

    void onCommit(TransactionContext context);

    void onRollback(TransactionContext context);

}
