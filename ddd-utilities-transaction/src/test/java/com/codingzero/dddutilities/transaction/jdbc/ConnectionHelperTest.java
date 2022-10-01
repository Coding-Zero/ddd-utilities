package com.codingzero.dddutilities.transaction.jdbc;

import com.codingzero.dddutilities.transaction.manager.TransactionCoordinatorHelper;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConnectionHelperTest {

    private static String SCHEMA_NAME = "utilities_transaction";
    private static String TABLE_NAME = "table1";
    private static String CREATE_SCHEMA = "CREATE SCHEMA `" + SCHEMA_NAME + "` DEFAULT CHARACTER SET utf8 ;";
    private static String CREATE_TABLE = "CREATE TABLE `" + SCHEMA_NAME + "`.`" + TABLE_NAME + "` (\n"
            + "  `id` INT NOT NULL AUTO_INCREMENT,\n"
            + "  `value` VARCHAR(35) NULL,\n"
            + "  PRIMARY KEY (`id`));";
    private static String DROP_TABLE = "DROP TABLE `" + SCHEMA_NAME + "`.`" + TABLE_NAME + "`;";
    private static String DROP_SCHEMA = "DROP DATABASE `" + SCHEMA_NAME + "`;";

    private TransactionCoordinatorHelper coordinator;
    private ConnectionHelper subject;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws SQLException {
        this.dataSource = getMySQLDataSource();
        this.subject = new ConnectionHelper(dataSource);
        this.coordinator = new TransactionCoordinatorHelper();
        this.coordinator.register(this.subject);
        try {
            runScript(dataSource, DROP_SCHEMA);
        } catch (RuntimeException e) {
            //nothing
        }
        runScript(dataSource, CREATE_SCHEMA);
        runScript(dataSource, CREATE_TABLE);
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        runScript(dataSource, DROP_TABLE);
        runScript(dataSource, DROP_SCHEMA);
        dataSource = null;
    }

    public static DataSource getMySQLDataSource() {
        MysqlDataSource mysqlDS = new MysqlDataSource();
        mysqlDS.setURL("jdbc:mysql://localhost:3306/");
        mysqlDS.setUser("root");
        mysqlDS.setPassword("123456");
        return mysqlDS;
    }

    public void runScript(DataSource source, String sql) throws SQLException {
        Connection conn = source.getConnection();
        PreparedStatement stmt=null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            stmt.close();
            conn.close();
        }
    }

    private void assertNumberOfRows(int rows) {
        TestAccess access = new TestAccess(new ConnectionHelper(dataSource));
        assertEquals(rows, access.count());
    }

    @Test
    public void testNoTransactionInserts() {
        TestAccess access = new TestAccess(this.subject);
        String date = new Date().toString();
        access.insert(date);
        assertNumberOfRows(1);
        access.insert(date);
        assertNumberOfRows(2);
        access.insert(date);
        assertNumberOfRows(3);
        assertEquals(date, access.selectById(1));
        assertEquals(date, access.selectById(2));
        assertEquals(date, access.selectById(3));
    }

    @Test
    public void testSingleTransactionInserts() {
        TestAccess access = new TestAccess(this.subject);
        coordinator.start();
        String date = new Date().toString();
        access.insert(date);
        assertNumberOfRows(0);
        access.insert(date);
        assertNumberOfRows(0);
        access.insert(date);
        assertNumberOfRows(0);
        coordinator.commit();
        assertNumberOfRows(3);
    }

    @Test
    public void testSingleTransactionInserts_Rollback() {
        TestAccess access = new TestAccess(this.subject);
        coordinator.start();
        String date = new Date().toString();
        access.insert(date);
        assertNumberOfRows(0);
        access.insert(date);
        assertNumberOfRows(0);
        access.insert(date);
        assertNumberOfRows(0);
        coordinator.rollback();
        assertNumberOfRows(0);
    }

    @Test
    public void testMultipleNoTransactionInserts() {
        TestAccess access1 = new TestAccess(this.subject);
        TestAccess access2 = new TestAccess(this.subject);
        TestAccess access3 = new TestAccess(this.subject);
        String date = new Date().toString();
        access1.insert(date);
        access2.insert(date);
        access3.insert(date);
        assertNumberOfRows(3);
    }

    @Test
    public void testMultipleTransactionInserts() {
        TestAccess access1 = new TestAccess(this.subject);
        TestAccess access2 = new TestAccess(this.subject);
        TestAccess access3 = new TestAccess(this.subject);
        coordinator.start();
        String date = new Date().toString();
        access1.insert(date);
        assertNumberOfRows(0);
        access2.insert(date);
        assertNumberOfRows(0);
        access3.insert(date);
        assertNumberOfRows(0);
        coordinator.commit();
        assertNumberOfRows(3);
    }

    @Test
    public void testMultipleTransactionInserts_Rollback() {
        TestAccess access1 = new TestAccess(this.subject);
        TestAccess access2 = new TestAccess(this.subject);
        TestAccess access3 = new TestAccess(this.subject);
        coordinator.start();
        String date = new Date().toString();
        access1.insert(date);
        assertNumberOfRows(0);
        access2.insert(date);
        assertNumberOfRows(0);
        access3.insert(date);
        assertNumberOfRows(0);
        coordinator.rollback();
        assertNumberOfRows(0);
    }

    @Test
    public void testCommitBeforeStart() {
        assertThrows(IllegalStateException.class, () -> {
            coordinator.commit();
            coordinator.start();
        });
    }

    @Test
    public void testRollbackBeforeStart() {
        assertThrows(IllegalStateException.class, () -> {
            coordinator.rollback();
            coordinator.start();
        });
    }

    private static class TestAccess {

        private ConnectionHelper connectionHelper;

        public TestAccess(ConnectionHelper connectionHelper) {
            this.connectionHelper = connectionHelper;
        }

        public void insert(String value) {
            connectionHelper.execute(context -> {
                Connection conn = context.getConnection();
                PreparedStatement stmt = null;
                try {
                    String sql = String.format("INSERT INTO %s (%s) VALUES (%s);",
                            SCHEMA_NAME + "." + TABLE_NAME,
                            "value",
                            "?");
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, value);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    context.recyclePreparedStatement(stmt);
                }
                return null;
            });
        }

        public String selectById(int id) {
            return connectionHelper.execute(context -> {
                Connection conn = context.getConnection();
                PreparedStatement stmt = null;
                try {
                    String sql = String.format("SELECT * FROM %s WHERE id=? LIMIT 1;",
                            SCHEMA_NAME + "." + TABLE_NAME);
                    stmt = conn.prepareCall(sql);
                    stmt.setInt(1, id);
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) {
                        return null;
                    } else {
                        return rs.getString("value");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    context.recyclePreparedStatement(stmt);
                }
            });
        }

        public List<String> selectAll() {
            return connectionHelper.execute(context -> {
                Connection conn = context.getConnection();
                PreparedStatement stmt = null;
                try {
                    String sql = String.format("SELECT * FROM %s;",
                            SCHEMA_NAME + "." + TABLE_NAME);
                    stmt = conn.prepareCall(sql);
                    ResultSet rs = stmt.executeQuery();
                    List<String> result = new LinkedList<>();
                    while (rs.next()) {
                        result.add(rs.getString("value"));
                    }
                    return result;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    context.recyclePreparedStatement(stmt);
                }
            });
        }

        public int count() {
            return connectionHelper.execute(context -> {
                Connection conn = context.getConnection();
                PreparedStatement stmt = null;
                try {
                    String sql = String.format("SELECT count(*) FROM %s;",
                            SCHEMA_NAME + "." + TABLE_NAME);
                    stmt = conn.prepareCall(sql);
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    return rs.getInt(1);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    context.recyclePreparedStatement(stmt);
                }
            });
        }
    }

}
