package com.codingzero.dddutilities.transaction;

/**
 * Aggregates services which implements interface <tt>TransactionalService</tt>
 *
 * Encapsulates transaction life cycle -- start, commit and rollback
 */
public interface TransactionCoordinator {

    void register(TransactionalService service);

    void start();

    void commit();

    void rollback();

}
