package net.fze.ext.jdbc.tx;

public interface ITransactionManager {

    /**
     * 创建开启事务
     *
     */
    ITransaction beginTransaction();
}

