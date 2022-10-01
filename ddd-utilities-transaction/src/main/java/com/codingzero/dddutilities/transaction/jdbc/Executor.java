package com.codingzero.dddutilities.transaction.jdbc;

public interface Executor<T> {

    T execute(final ExecutorContext context);

}
