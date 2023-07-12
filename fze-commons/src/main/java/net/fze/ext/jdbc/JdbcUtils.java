package net.fze.ext.jdbc;

import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 通用工具类
 */
public class JdbcUtils {
    /**
     * 判断是否为空
     *
     * @param object
     * @return
     */
    public static boolean isNullForObject(Object object) {
        if (null == object || "".equals(object) || "null".equals(object)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * * 将ResultSet结果集转化为Map
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static Map<String, Object> toMapFromResultSet(ResultSetWrapper rs) throws SQLException {
        Map<String, Object> map = null;
        if (null != rs) {
            ResultSetMetaData resultSetMetaData = rs.unwrap().getMetaData();
            if (null != resultSetMetaData) {
                int columnSize = resultSetMetaData.getColumnCount();
                map = new HashMap<>();
                for (int i = 1; i <= columnSize; i++) {
                    String columnLabel = resultSetMetaData.getColumnLabel(i);
                    map.put(columnLabel, rs.getObject(columnLabel));
                }
            }
        }
        return map;
    }

    /**
     * 将ResultSet结果集转化为object
     *
     * @param rs
     * @param clazz
     * @return
     * @throws SQLException
     */
    public static Object toClassFromResultSet(ResultSetWrapper rs, Class clazz) throws SQLException {
        Object beans = null;
        if (null != rs) {
            ResultSetMetaData resultSetMetaData = rs.unwrap().getMetaData();
            if (null != resultSetMetaData) {
                /*
                 * //获取Person类的所有方法信息
                 * Method[] method=clazz.getDeclaredMethods();
                 */
                // 获取类的所有成员属性信息
                Field[] fields = clazz.getDeclaredFields();
                int columnSize = resultSetMetaData.getColumnCount();
                try {
                    beans = clazz.newInstance();
                    for (int i = 1; i <= columnSize; i++) {
                        String columnLabel = resultSetMetaData.getColumnLabel(i);
                        for (Field field : fields) {
                            field.setAccessible(true);
                            if (field.getName().equals(columnLabel)) {
                                field.set(beans, rs.unwrap().getObject(columnLabel, field.getType()));
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        return beans;
    }

    /**
     * 二次封装执行查询集合操作，减少重复代码 返回MAP集合
     *
     * @param connection
     * @param sql
     * @param isBreak
     * @param params
     * @return
     */
    public static List<Map<String, Object>> executeQueryRowForListMap(
            JdbcConnection connection, String sql, boolean isBreak, Object... params) throws SQLException {
        List<Map<String, Object>> list;
        list = new ArrayList<>();
        connection.queryRow(
                sql,
                rs -> {
                    try {
                        Map<String, Object> map = toMapFromResultSet(rs);
                        list.add(map);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (isBreak)
                        rs.stopScan();
                },
                params);
        return list;
    }

    /**
     * 二次封装执行查询单条结果操作，减少重复代码 返回值MAP
     *
     * @param connection
     * @param sql
     * @param isBreak
     * @param params
     * @return
     */
    public static Map<String, Object> executeQueryRowForMap(
            JdbcConnection connection, String sql, boolean isBreak, Object... params) throws SQLException {
        AtomicReference<Map<String, Object>> row = new AtomicReference<>();
        connection.queryRow(
                sql,
                rs -> {
                    try {
                        row.set(toMapFromResultSet(rs));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (isBreak)
                        rs.stopScan();
                },
                params);
        return row.get();
    }

    /**
     * 二次封装执行查询实体结果集合操作，减少重复代码 返回值MAP集合
     *
     * @param connection
     * @param sql
     * @param isBreak
     * @param clazz
     * @param params
     * @return
     */
    public static List<Object> executeQueryRowForListClass(
            JdbcConnection connection, String sql, boolean isBreak, Class clazz, Object... params)
            throws SQLException {
        List<Object> list = new ArrayList<>();
        connection.queryRow(
                sql,
                rs -> {
                    try {
                        Object map = toClassFromResultSet(rs, clazz);
                        list.add(map);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (isBreak)
                        rs.stopScan();
                },
                params);
        return list;
    }

    /**
     * 二次封装执行查询实体结果集合操作，减少重复代码 返回值MAP集合
     *
     * @param connection
     * @param sql
     * @param isBreak
     * @param clazz
     * @param params
     * @return
     */
    public static Object executeQueryRowForClass(
            JdbcConnection connection, String sql, boolean isBreak, Class clazz, Object... params)
            throws SQLException {
        AtomicReference<Object> row = new AtomicReference<>();
        connection.queryRow(
                sql,
                rs -> {
                    try {
                        row.set(toClassFromResultSet(rs, clazz));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (isBreak)
                        rs.stopScan();
                },
                params);
        return row.get();
    }

    public static long executeNonQuery(
            JdbcConnection connection, String sql, boolean autoKey, Class clazz, Object... params)
            throws Throwable {
        long key = -1;
        try {
            key = connection.execNonQuery(sql, autoKey, params);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return key;
    }
}
