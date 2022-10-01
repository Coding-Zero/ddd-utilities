package com.codingzero.dddutilities.transaction.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.List;

public class ExecutorContext {

    private Connection connection;
    private List<PreparedStatement> recyclingPreparedStatements;

    public ExecutorContext(Connection connection) {
        this.connection = connection;
        this.recyclingPreparedStatements = new LinkedList<>();
    }

    public Connection getConnection() {
        return connection;
    }

    public List<PreparedStatement> getRecyclingPreparedStatements() {
        return recyclingPreparedStatements;
    }

    public void recyclePreparedStatement(PreparedStatement preparedStatement) {
        this.recyclingPreparedStatements.add(preparedStatement);
    }
}
