package net.fze.libs.jdbc;

import org.junit.jupiter.api.Test;

import java.beans.PropertyVetoException;

public class JdbConnectorTest {
    /**
     * 测试使用连接
     *
     * @throws PropertyVetoException
     * @throws ClassNotFoundException
     */
    @Test
    public void testAcquire() throws PropertyVetoException, ClassNotFoundException {
        String driverClass = "com.mysql.jdbc.Driver";
        String connectionUrl = DataSourceBuilder.Companion.createDriverUrl("mysql", "localhost", 3306, "test");
        // 创建连接器,连接器应是全局的
        IConnectionPool jdb = new DataSourceBuilder().create(driverClass,connectionUrl)
                .credential("root","123456").build();
        // 获取连接，通过连接器获得
        JdbcConnection conn = jdb.acquire();
        // 开启事务
        Transcation tran = conn.transcation();
        try {
            Long id = conn.execNonQuery(
                    "INSERT INTO user(user,host,password)VALUES(?,?,?)",
                    true, // 传true,返回生成的主键，反之返回受影响的行数
                    "test_user",
                    "%",
                    "123456");
            System.out.println(String.format("新增的ID为:%d", id));
            Long rows = conn.execNonQuery("UPDATE user set password='12347890' WHERE user=?", false, "test_user");
            System.out.println(String.format("受影响行数为:%d", rows));
            tran.commit(); // 提交事务
        } catch (Throwable ex) {
            ex.printStackTrace();
            tran.rollback(); // 回滚事务
        } finally {
            conn.close();
        }
    }
}
