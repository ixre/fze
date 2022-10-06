package net.fze.common.infrastructure;
/*
  created for mzl-server [ Formatter.java ]
  user: liuming (jarrysix@gmail.com)
  date: 12/11/2017 10:00
  description: 
 */

import java.util.Set;

public class Formatter {

    /**
     * 将键集合转为SQL
     *
     * @param set 键集合
     * @return 字符串
     */
    public String setToSql(Set<String> set) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String s : set) {
            if (i++ > 0) {
                sb.append(" AND ");
            }
            sb.append(s).append("=:").append(s);
        }
        return sb.toString();
    }

    /**
     * 将分页信息添加到SQL查询中
     *
     * @param sw         字符串构造器
     * @param orderField 排序字段
     * @param orderDesc  是否倒序
     * @param begin      开始条数
     * @param over       结束条数
     */
    public void attachSqlQuery(StringBuilder sw, String orderField, boolean orderDesc, int begin, int over) {
        if (orderField != null && !orderField.isEmpty()) {
            sw.append(" ORDER BY ").append(orderField).append(orderDesc ? " DESC" : " ASC");
        }
        if (over > begin) {
            if (begin < 0)
                begin = 0;
            // todo mysql limit 按分页数分页
            int connt = over - begin;
            sw.append(" LIMIT ").append(String.valueOf(begin))
                    .append(",").append(String.valueOf(connt));
        }
    }

    /**
     * 将INT数组转换为SQL字符串
     *
     * @param arr 数组
     * @return 以","分割的字符串
     */
    public String sqlArrayString(int[] arr) {
        int i = 0;
        String[] sa = new String[arr.length];
        for (int a : arr) {
            sa[i++] = String.valueOf(a);
        }
        return String.join(",", sa);
    }

    /**
     * 获取订单类型键
     *
     * @param orderKind 订单种类
     * @param orderType 订单类型
     * @return 键
     */
    public String getOrderTypeKey(int orderKind, int orderType) {
        if (orderKind <= 0 || orderType <= 0 || orderType > 99 || orderKind > 99) {
            return "unknown order kind";
        }
        StringBuilder sb = new StringBuilder();
        if (orderKind < 10) {
            sb.append("0").append(orderKind);
        }
        if (orderType < 10) {
            sb.append("0").append(orderType);
        }
        return sb.toString();
    }
}
