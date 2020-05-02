package com.github.ixre.fze.commons.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集包装器
 */
public class ResultSetWrapper {
    private final ResultSet rs;
    private final boolean ignoreErr;
    private boolean _stopped;


    ResultSetWrapper(ResultSet rs, boolean ignoreErr) {
        this.rs = rs;
        this.ignoreErr = ignoreErr;
    }

    /**
     * 是否有新行
     *
     * @return
     * @throws SQLException
     */
    public boolean next() throws SQLException {
        return this.rs.next();
    }

    /**
     * 是否关闭
     *
     * @return
     * @throws SQLException
     */
    public boolean isClosed() throws SQLException {
        return this.rs.isClosed();
    }

    /**
     * 关闭结果集
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        this.rs.close();
    }

    /**
     * 获取原始的ResultSet
     *
     * @return
     */
    public ResultSet unwrap() {
        return this.rs;
    }

    public int getRow() throws SQLException {
        return this.rs.getRow();
    }

    public boolean isFirst() throws SQLException {
        return this.rs.isFirst();
    }

    public boolean isLast() throws SQLException {
        return this.rs.isLast();
    }

    public boolean wasNull() throws SQLException {
        return this.rs.wasNull();
    }

    private void throwErr(Throwable ex) {
        ex.printStackTrace();
        if (!this.ignoreErr) {
            Error err = new Error(ex.getMessage());
            err.setStackTrace(ex.getStackTrace());
            throw err;
        }
    }

    /**
     * 获取字符串
     */
    public String getString(int columnIndex) {
        try {
            return this.rs.getString(columnIndex);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return "";
    }

    /**
     * 获取布尔值
     */

    public boolean getBoolean(int columnIndex) {
        try {
            return this.rs.getBoolean(columnIndex);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return false;
    }

    /**
     * 获取字节
     */
    public byte getByte(int columnIndex) {
        try {
            return this.rs.getByte(columnIndex);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return 0;
    }

    /**
     * 获取int16
     */
    public short getShort(int columnIndex) {
        try {
            return this.rs.getShort(columnIndex);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return 0;
    }

    /**
     * 获取int32
     */
    public int getInt(int columnIndex) {
        try {
            return this.rs.getInt(columnIndex);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return 0;
    }

    /**
     * 获取int64
     */
    public long getLong(int columnIndex) {
        try {
            return this.rs.getLong(columnIndex);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return 0;
    }

    /**
     * 获取float32
     */
    public float getFloat(int columnIndex) {
        try {
            return this.rs.getFloat(columnIndex);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return 0;
    }

    /**
     * 获取float64
     */
    public double getDouble(int columnIndex) {
        try {
            return this.rs.getDouble(columnIndex);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return 0;
    }

    /**
     * 获取字节数组
     */
    public byte[] getBytes(int columnIndex) {
        try {
            return this.rs.getBytes(columnIndex);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return new byte[0];
    }

    /**
     * 获取对象
     */
    public Object getObject(int columnIndex) {
        try {
            return this.rs.getObject(columnIndex);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return "";
    }


    /**
     * 获取字符串
     */
    public String getString(String columnName) {
        try {
            return this.rs.getString(columnName);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return "";
    }

    /**
     * 获取布尔值
     */

    public boolean getBoolean(String columnName) {
        try {
            return this.rs.getBoolean(columnName);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return false;
    }

    /**
     * 获取字节
     */
    public byte getByte(String columnName) {
        try {
            return this.rs.getByte(columnName);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return 0;
    }

    /**
     * 获取int16
     */
    public short getShort(String columnName) {
        try {
            return this.rs.getShort(columnName);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return 0;
    }

    /**
     * 获取int32
     */
    public int getInt(String columnName) {
        try {
            return this.rs.getInt(columnName);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return 0;
    }

    /**
     * 获取int64
     */
    public long getLong(String columnName) {
        try {
            return this.rs.getLong(columnName);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return 0;
    }

    /**
     * 获取float32
     */
    public float getFloat(String columnName) {
        try {
            return this.rs.getFloat(columnName);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return 0;
    }

    /**
     * 获取float64
     */
    public double getDouble(String columnName) {
        try {
            return this.rs.getDouble(columnName);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return 0;
    }

    /**
     * 获取字节数组
     */
    public byte[] getBytes(String columnName) {
        try {
            return this.rs.getBytes(columnName);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return new byte[0];
    }

    /**
     * 获取对象
     */
    public Object getObject(String columnName) {
        try {
            return this.rs.getObject(columnName);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return "";
    }


    /**
     * 查找列的索引
     */
    public int findColumn(String columnLabel) {
        try {
            return this.rs.findColumn(columnLabel);
        } catch (Throwable ex) {
            this.throwErr(ex);
        }
        return -1;
    }

    boolean isStopped() {
        return this._stopped;
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        this._stopped = true;
    }

}
