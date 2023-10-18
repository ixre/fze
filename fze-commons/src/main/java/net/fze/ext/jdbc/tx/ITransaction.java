package net.fze.ext.jdbc.tx;

public interface ITransaction {
    /**
     * 提交事务
     */
    void commit();

    /**
     * 回滚事务
     */
    void rollback();
}
