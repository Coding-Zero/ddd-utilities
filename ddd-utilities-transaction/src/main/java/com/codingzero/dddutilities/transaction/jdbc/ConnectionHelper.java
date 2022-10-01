package com.codingzero.dddutilities.transaction.jdbc;

import com.codingzero.dddutilities.transaction.TransactionContext;
import com.codingzero.dddutilities.transaction.TransactionalService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class ConnectionHelper implements TransactionalService {

    private DataSource dataSource;
    private boolean isTransactionStarted;
    private Connection currentConnection;

    public ConnectionHelper(DataSource dataSource) {
        this.dataSource = dataSource;
        this.isTransactionStarted = false;
        this.currentConnection = null;
    }

    public <T> T execute(Executor<T> executor) {
        ExecutorContext context = new ExecutorContext(getConnection());
        T result = executor.execute(context);
        for (PreparedStatement preparedStatement: context.getRecyclingPreparedStatements()) {
            if (Objects.nonNull(preparedStatement)) {
                closePreparedStatement(preparedStatement);
            }
        }
        closeConnection();
        return result;
    }

    private Connection getConnection() {
        if (isTransactionStarted) {
            checkForCurrentConnectionNotExist();
        } else {
            checkForCurrentConnectionAlreadyExist();
            this.currentConnection = newConnection(false);
        }
        return this.currentConnection;
    }

    private Connection closeConnection() {
        checkForCurrentConnectionNotExist();
        if (!isTransactionStarted) {
            closeCurrentConnection();
        }
        return null;
    }

    private void closePreparedStatement(PreparedStatement stmt) {
        try {
            if (null != stmt) {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeCurrentConnection() {
        try {
            this.currentConnection.setAutoCommit(true);
            this.currentConnection.close();
            this.currentConnection = null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void commitCurrentConnection() {
        try {
            this.currentConnection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void rollbackCurrentConnection() {
        try {
            this.currentConnection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection newConnection(boolean transactional) {
        try {
            Connection connection = dataSource.getConnection();
            if (transactional) {
                connection.setAutoCommit(false);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onRegister() {

    }

    @Override
    public void onStart(TransactionContext context) {
        checkForTransactionAlreadyStarted();
        checkForCurrentConnectionAlreadyExist();
        this.isTransactionStarted = true;
        this.currentConnection = newConnection(true);

    }

    @Override
    public void onCommit(TransactionContext context) {
        checkForNoTransactionStarted();
        checkForCurrentConnectionNotExist();
        commitCurrentConnection();
        closeCurrentConnection();
        this.isTransactionStarted = false;
    }

    @Override
    public void onRollback(TransactionContext context) {
        checkForNoTransactionStarted();
        checkForCurrentConnectionNotExist();
        rollbackCurrentConnection();
        closeCurrentConnection();
        this.isTransactionStarted = false;
    }

    private void checkForTransactionAlreadyStarted() {
        if (this.isTransactionStarted) {
            throw new IllegalStateException("One transaction already started.");
        }
    }

    private void checkForNoTransactionStarted() {
        if (!this.isTransactionStarted) {
            throw new IllegalStateException("No transaction started.");
        }
    }

    private void checkForCurrentConnectionAlreadyExist() {
        if (Objects.nonNull(this.currentConnection)) {
            throw new IllegalStateException("Current connection already existed.");
        }
    }

    private void checkForCurrentConnectionNotExist() {
        if (Objects.isNull(this.currentConnection)) {
            throw new IllegalStateException("Current connection doesn't exist.");
        }
    }
}
