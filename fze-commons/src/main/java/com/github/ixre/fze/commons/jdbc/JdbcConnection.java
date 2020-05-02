package com.github.ixre.fze.commons.jdbc;

import java.sql.*;

/**
 * JDBC连接
 */
public class JdbcConnection {
    private final Connection conn;
    private Transcation trans;
    private boolean ignoreErr = false;

    public JdbcConnection(Connection conn) {
        this.conn = conn;
    }

    /**
     * 忽略错误
     */
    public void ignoreError() {
        this.ignoreErr = true;
    }

    /**
     * 获取原始的连接
     *
     * @return JdbcConnection
     */
    public Connection raw() {
        return this.conn;
    }

    /**
     * 开始事务
     */
    public Transcation transcation() {
        if (this.trans == null) {
            try {
                this.trans = new Transcation(this.conn);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return this.trans;
    }

    /**
     * 查询SQL语句，返回第一行第一列，如果没有结果，则返回null
     *
     * @param sql    SQL语句
     * @param params 参数
     * @return 结果
     */
    public Object execScalar(String sql, Object... params) {
        PreparedStatement smt = null;
        ResultSet rs = null;
        boolean except = false;
        try {
            smt = this.conn.prepareStatement(sql);
            this.addParams(smt, params);
            rs = smt.executeQuery();
            if (rs.next()) {
                return rs.getObject(1);
            }
        } catch (SQLException ex) {
            except = true;
            ex.printStackTrace();
            throw this.parseSQLException(ex);
        } finally {
            this.closeAll(except ? this.conn : null, smt, rs);
        }
        return null;
    }

    /**
     * 转换为内部错误
     *
     * @return
     */
    private Error parseSQLException(Throwable ex) {
        Error err = new Error(ex.getMessage());
        err.setStackTrace(ex.getStackTrace());
        throw err;
    }

    /**
     * 执行查询，并返回受影响的行数或主键编号
     *
     * @param sql     SQL语句
     * @param autoKey 是否返回主键
     * @param params  参数
     * @return 结果
     */
    public long execNonQuery(String sql, boolean autoKey, Object... params) {
        PreparedStatement smt = null;
        ResultSet rs = null;
        boolean except = false;
        try {
            if (!autoKey) {
                smt = this.conn.prepareStatement(sql);
                this.addParams(smt, params);
                return smt.executeUpdate();
            }
            smt = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            this.addParams(smt, params);
            smt.executeUpdate();
            rs = smt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException ex) {
            except = true;
            ex.printStackTrace();
            throw this.parseSQLException(ex);
        } finally {
            this.closeAll(except ? this.conn : null, smt, rs);
        }
        return 0;
    }

    /**
     * 查询行
     *
     * @param sql    SQL语句
     * @param scan   扫描
     * @param params 参数
     */
    public void queryRow(String sql, IRowScanner scan, Object... params) {
        PreparedStatement smt = null;
        ResultSetWrapper rs = null;
        boolean except = false;
        try {
            smt = this.conn.prepareStatement(sql);
            this.addParams(smt, params);
            rs = new ResultSetWrapper(smt.executeQuery(), this.ignoreErr);
            while (rs.next()) {
                if (rs.isStopped()) break;
                scan.scan(rs);
            }
        } catch (SQLException ex) {
            except = true;
            ex.printStackTrace();
            throw this.parseSQLException(ex);
        } finally {
            this.closeAll(except ? this.conn : null, smt, rs == null ? null : rs.unwrap());
        }
    }

    /**
     * 关闭所有连接
     *
     * @param conn 连接
     * @param smt  命令
     * @param rs   结果游标
     */
    private void closeAll(Connection conn, PreparedStatement smt, ResultSet rs) {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
            if (rs != null && !rs.isClosed()) rs.close();
            if (smt != null && !smt.isClosed()) smt.close();
        } catch (Throwable ex) {
            throw new Error("[ JDBC][ Exception]: 关闭资源失败:" + ex.getMessage());
        }
    }

    private void addParams(PreparedStatement smt, Object... params) {
        if (params != null) {
            int i = 1;
            for (Object p : params) {
                try {
                    smt.setObject(i++, p);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    throw this.parseSQLException(ex);
                }
            }
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        this.closeAll(this.conn, null, null);
    }
}
