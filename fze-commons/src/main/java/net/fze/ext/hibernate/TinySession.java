package net.fze.ext.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 会话
 */
public interface TinySession {
    /**
     * 获取会话
     *
     * @return 会话
     */
    Session session();

    /**
     * 开始事务
     *
     * @return 返回事务
     */
    Transaction beginTrans();

    /**
     * 通过HQL语句查询
     *
     * @param hql HQL语句
     * @return 列表
     */
    <T> List<T> select(String hql, Object... params);

    /**
     * 通过HQL语句查询
     *
     * @param hql  HQL语句
     * @param data 数据
     * @return 列表
     */
    <T> List<T> select(String hql, Map<String, Object> data);

    /**
     * 根据主键获取对象
     *
     * @param c   对象类型
     * @param id  编号
     * @param <T> 对象
     * @return 返回对象, 如果不存在，将返回null
     */
    <T> T get(Class<T> c, Serializable id);

    /**
     * 通过HQL语句查询并返回单个结果
     *
     * @param where  条件语句
     * @param params 数据
     * @return 对象，需拆箱
     */
    <T> T get(Class<T> c, String where, Object... params);

    /**
     * 通过HQL语句查询并返回单个结果
     *
     * @param where  条件语句
     * @param params 数据
     * @return 对象，需拆箱
     */
    <T> T get(Class<T> c, String where, Map<String, Object> params);

    /**
     * 分页查询, Hibernate调用如下
     *
     * @param hql   HQL语句
     * @param begin 开始条数
     * @param end   结束条数
     * @param data  数据
     * @return 返回对象列表
     */
    <T> List<T> pagingSelect(String hql, int begin, int end, Map<String, Object> data);

    /**
     * 执行HQL查询
     *
     * @param hql  语句
     * @param data 数据
     * @return 影响的行数
     */
    int executeHql(String hql, Map<String, Object> data);

    /**
     * 删除
     *
     * @param t 对象
     */
    void delete(Object t);

    /**
     * 保存
     *
     * @param t 对象
     */
    void saveOrUpdate(Object t);

    /**
     * create
     *
     * @param t 对象
     */
    Serializable add(Object t);

    /**
     * 保存
     *
     * @param t 对象
     */
    void update(Object t);

    /**
     * 执行SQL语句查询
     *
     * <pre>
     *      如：select * FROM mysql.user WHERE user=:user AND host=:host
     *      Map data = new HashMap();
     *      data.add("user","jarrysix");
     * </pre>
     *
     * @param sql  SQL语句
     * @param data 数据参数
     * @return 返回受影响的行数
     */
    int execute(String sql, Map<String, Object> data);

    /**
     * 执行查询，并返回单个结果
     *
     * @param sql  SQL语句
     * @param data 参数
     * @return 结果
     */
    Object executeScalar(String sql, Map<String, Object> data);

    /**
     * 执行查询，并返回单个结果
     *
     * @param sql  SQL语句
     * @param data 参数
     * @return 结果
     */
    Object executeScalar(String sql, Object... data);

    /**
     * 使用SQL查询，并返回列表
     *
     * @param sql  SQL语句
     * @param data 参数
     * @param <T>  类型
     * @return 列表
     */
    <T> List<T> select(Class<T> c, String sql, Map<String, Object> data);

    /**
     * 使用SQL查询，并返回字段与值的映射
     *
     * @param sql  SQL语句
     * @param data 参数
     * @return 字段与值
     */
    Map<String, Object> get2Map(String sql, Map<String, Object> data);

    /**
     * 使用SQL查询，并返回字段与值映射的列表
     * 对于连接了多个表的查询，这就可能造成问题，因为可能在多个表中出现同样名字的字段。
     * 下面的方法就可以避免字段名重复的问题:
     * List cats = sess.createSQLQuery( " select {cat.*} from cats cat " )
     * .addEntity( " cat " , Cat. class ).list();
     *
     * @param sql  SQL语句
     * @param data 参数
     * @return 列表
     */
    List<Map<String, Object>> select2Map(String sql, Map<String, Object> data);

    /**
     * 关闭数据库连接
     */
    void close();

    /**
     * 测试连接
     */
    boolean ping();
}