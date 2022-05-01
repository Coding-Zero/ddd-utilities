package com.codingzero.dddutilities.transaction;

public interface TransactionContext {

    void setProperty(String name, Object property);

    Object getProperty(String name);

    Object removeProperty(String name);

}
