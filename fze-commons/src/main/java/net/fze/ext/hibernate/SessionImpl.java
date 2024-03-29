package net.fze.ext.hibernate;

import net.fze.util.TypeConv;
import net.fze.util.Types;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 会话适配器
 */
public class SessionImpl implements TinySession {
    private boolean _cacheable = false;
    private Session _session;
    private Transaction _trans;

    public SessionImpl() {
        //this._cacheable = Hibernate.cacheable();
        this._session = Hibernate.getSession();
    }

    public SessionImpl(Session s,boolean cacheable) {
        this._session = s;
        this._cacheable = cacheable;
    }

    /**
     * 获取会话
     *
     * @return 会话
     */
    @Override
    public Session session() {
        return this._session;
    }

    /**
     * 开始事务
     *
     * @return 返回事物
     */
    @Override
    public Transaction beginTrans() {
        this._trans = this._session.beginTransaction();
        return this._trans;
    }

    /**
     * 通过HQL语句查询
     *
     * @param hql HQL语句
     * @return 结果集
     */
    @Override
    public <T> List<T> select(String hql, Object... params) {
        Session s = this._session;
        Query query = s.createQuery(hql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        List list = this.cacheQuery(query).list();
        s.close();
        return list;
    }

    /**
     * 通过HQL语句查询
     *
     * @param hql  HQL语句
     * @param data 数据
     * @return 列表
     */
    @Override
    public <T> List<T> select(String hql, Map<String, Object> data) {
        Session s = this._session;
        Query query = s.createQuery(hql);
        for (Map.Entry<String, Object> e : data.entrySet()) {
            query.setParameter(e.getKey(), e.getValue());
        }
        List list = this.cacheQuery(query).list();
        s.close();
        return list;
    }

    /**
     * 根据主键获取对象
     *
     * @param c   对象类型
     * @param id  编号
     * @param <T> 对象
     * @return 返回对象, 如果不存在，将返回null
     */
    @Override
    public <T> T get(Class<T> c, Serializable id) {
        Session s = this._session;
        T t = s.get(c, id);
        s.close();
        return t;
    }

    /**
     * 通过HQL语句查询并返回单个结果
     *
     * @param where  条件语句
     * @param params 数据
     * @return 对象，需拆箱
     */
    @Override
    public <T> T get(Class<T> c, String where, Object... params) {
        String[] arr = new String[]{"FROM ", c.getName(), " WHERE ", where};
        String hql = String.join("", arr);

        Session s = this._session;
        Query query = s.createQuery(hql);
        query.setMaxResults(1);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        List<T> list = this.cacheQuery(query).list();
        s.close();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 通过HQL语句查询并返回单个结果
     *
     * @param where  条件语句
     * @param params 数据
     * @return 对象，需拆箱
     */
    @Override
    public <T> T get(Class<T> c, String where, Map<String, Object> params) {
        String[] arr = new String[]{"FROM ", c.getSimpleName(), " WHERE ", where};
        String hql = String.join("", arr);
        Session s = this._session;
        Query query = s.createQuery(hql);
        query.setMaxResults(1);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        List<T> list = this.cacheQuery(query).list();
        s.close();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    private Query cacheQuery(Query query) {
        return this._cacheable?query.setCacheable(true):query;
    }

    /**
     * 分页查询, Hibernate调用如下
     *
     * @param hql   HQL语句
     * @param begin 开始条数
     * @param end   结束条数
     * @param data  数据
     * @return 返回对象列表
     */
    @Override
    public <T> List<T> pagingSelect(String hql, int begin, int end, Map<String, Object> data) {
        Session s = this._session;
        Query query = s.createQuery(hql).setProperties(data);
        query.setFirstResult(begin);
        query.setMaxResults(end);
        List l = query.list();
        s.close();
        return l;
    }

    /**
     * 创建原生查询
     *
     * @param sql  SQL
     * @param data 参数
     * @return 查询
     */
    private Query createQuery(Session session, String sql, Map<String, Object> data) {
        Query query = session.createQuery(sql);
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query;
    }

    @Override
    public int executeHql(String hql, Map<String, Object> data) {
        Session s = this._session;
        Query query = this.createQuery(s, hql, data);
        Transaction trans = s.beginTransaction();
        try {
            int i = query.executeUpdate();
            trans.commit();
            return i;
        } catch (Exception ex) {
            ex.printStackTrace();
            trans.rollback();
        } finally {
            s.close();
        }
        return 0;
    }

    /**
     * 删除
     *
     * @param t 对象
     */
    @Override
    public void delete(Object t) {
        Session s = this._session;
        Transaction trans = s.beginTransaction();
        try {
            s.delete(t);
            trans.commit();
            // this._session.flush();
        } catch (Exception ex) {
            trans.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            s.close();
        }
    }

    /**
     * 创建对象
     *
     * @param t 对象
     */
    @Override
    public void saveOrUpdate(Object t) {
        Session s = this._session;
        Transaction trans = s.beginTransaction();
        try {
            // unsaved-value
            // https://blog.csdn.net/daryl715/article/details/1756325

            // Caused by: javax.persistence.OptimisticLockException: Batch update returned unexpected row count from update [0]; actual row count: 0; expected: 1; statement executed
            // 使用的是hibernate的saveOrUpdate方法保存实例。saveOrUpdate方法要求ID为null时才执行SAVE，在其它情况下执行UPDATE。在保存实例的时候是新增，但你的ID不为null，所以使用的是UPDATE，但是数据库里没有主键相关的值，所以出现异常。
            s.saveOrUpdate(t);
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            s.close();
        }
    }

    @Override
    public void saveOrUpdateById(Object t, Object id) {
        if (id == null || "".equals(id) || TypeConv.toFloat(id) <= 0) {
            this.add(t);
        } else {
            this.update(t);
        }
    }

    @Override
    public Serializable add(Object t) {
        Session s = this._session;
        Transaction trans = s.beginTransaction();
        try {
            Serializable save = s.save(t);
            trans.commit();
            return save;
        } catch (Exception ex) {
            trans.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            s.close();
        }
    }

    @Override
    public void update(Object t) {
        Session s = this._session;
        Transaction trans = s.beginTransaction();
        try {
            s.update(t);
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            s.close();
        }
    }

    /**
     * 统计记录数
     *
     * @param hql HQL语句，必须包括大写COUNT
     * @return 返回记录总数
     */
    public int count(String hql) {
        if (hql.indexOf("COUNT") <= 0) {
            System.out.println("语句不符合查询总条数的语法");
            return -1;
        }
        Session s = this._session;
        Object r = s.createQuery(hql).uniqueResult();
        s.close();
        return (Integer) r;
    }

    /**
     * 创建原生查询
     *
     * @param sql  SQL
     * @param data 参数
     * @return 查询
     */
    private NativeQuery createNativeQuery(Session session, String sql, Map<String, Object> data) {
        NativeQuery query = session.createNativeQuery(sql);
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query;
    }

    /**
     * 创建原生查询
     *
     * @param sql  SQL
     * @param data 参数
     * @return 查询
     */
    private NativeQuery createNativeQuery(Session session, String sql, Object... data) {
        NativeQuery query = session.createNativeQuery(sql);
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                query.setParameter(i, data[i]);
            }
        }
        return query;
    }

    /**
     * 执行原生SQL查询
     *
     * @param sql  T-SQL语句
     * @param data 数据参数
     * @return 返回受影响的行数
     */
    @Override
    public int execute(String sql, Map<String, Object> data) {
        Session s = this._session;
        NativeQuery query = this.createNativeQuery(s, sql, data);
        Transaction trans = s.beginTransaction();
        try {
            int i = query.executeUpdate();
            trans.commit();
            return i;
        } catch (Exception ex) {
            ex.printStackTrace();
            trans.rollback();
            throw new RuntimeException(ex);
        } finally {
            s.close();
        }
    }

    /**
     * 执行查询，并返回单个结果
     *
     * @param sql  SQL语句
     * @param data 参数
     * @return 结果
     */
    @Override
    public Object executeScalar(String sql, Map<String, Object> data) {
        Session s = this._session;
        NativeQuery query = this.createNativeQuery(s, sql, data);
        Object r = query.uniqueResult();
        s.close();
        return r;
    }

    /**
     * 执行查询，并返回单个结果
     *
     * @param sql  SQL语句
     * @param data 参数
     * @return 结果
     */
    @Override
    public Object executeScalar(String sql, Object... data) {
        Session s = this._session;
        NativeQuery query = this.createNativeQuery(s, sql, data);
        Object r = query.uniqueResult();
        this.close();
        return r;
    }

    @Override
    public <T> List<T> select(Class<T> c, String sql, Map<String, Object> data) {
        Session s = this._session;
        NativeQuery query = this.createNativeQuery(s, sql, data).addEntity(c);
        List<T> l = query.list();
        s.close();
        return l;
    }

    @Override
    public Map<String, Object> get2Map(String sql, Map<String, Object> data) {
        Session s = this._session;
        List list =
                this.createNativeQuery(s, sql, data)
                        .unwrap(Query.class)
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .getResultList();
        s.close();
        if (list.size() > 0) {
            return (Map<String, Object>) list.get(0);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> select2Map(String sql, Map<String, Object> data) {
        Session s = this._session;
        List list =
                this.createNativeQuery(s, sql, data)
                        .unwrap(Query.class)
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .getResultList();
        this.close();
        return (List<Map<String, Object>>) list;
    }

    /**
     * 关闭数据库连接
     */
    @Override
    public void close() {
        if (this._session != null) {
            this._session.close();
            this._session = null;
        }
    }

    /**
     * 测试连接
     */
    @Override
    public boolean ping() {
        Object obj = this.executeScalar("SELECT 1");
        return obj != null && "1".equals(obj.toString());
    }
}
