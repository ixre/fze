package net.fze.ext.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务
 */
public class Transaction {
    private final Connection conn;

    Transaction(Connection conn) throws SQLException {
        this.conn = conn;
        if (this.conn.getAutoCommit()) {
            this.conn.setAutoCommit(false);
        }
    }

    /**
     * 回滚事务
     */
    public void rollback() {
        try {
            this.conn.rollback();
        } catch (Throwable ex) {
            throw new Error(ex);
        }
    }

    /**
     * 提交事务
     */
    public void commit() throws SQLException {
        this.conn.commit();
    }
}
