package net.fze.ext.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.fze.domain.query.IQueryWrapper;

import java.util.List;

public class MyBatisQueryWrapper<T> extends QueryWrapper<T> implements IQueryWrapper {
    /**
     * 转换为QueryWrapper
     */
    public QueryWrapper<T> toQueryWrapper() {
        return this;
    }

    /**
     * 排序
     * @param column 列名
     */
    public MyBatisQueryWrapper<T> orderByAsc(String column) {
        super.orderByAsc(column);
        return this;
    }

    /**
     * 排序
     * @param column 列名
     */
    public MyBatisQueryWrapper<T> orderByDesc(String column) {
        super.orderByDesc(column);
        return this;
    }

    /**
     * 或
     */
    public MyBatisQueryWrapper<T> or(){
        super.or();
        return this;
    }

    /**
     * 判断相等
     * @param column 列名
     * @param val 值
     */
    public MyBatisQueryWrapper<T> eq(String column, Object val) {
        super.eq(column, val);
        return this;
    }

    /**
     * 判断相等，为空则不加入条件
     * @param column 列名
     * @param val 值
     */
    public MyBatisQueryWrapper<T> eqIfPresent(String column, Object val) {
        if(val == null || val.equals(""))return this;
        return this.eq(column, val);
    }

    /**
     * 判断是否相似
     * @param column    列名
     * @param val       值
     */
    public MyBatisQueryWrapper<T> like(String column, Object val) {
        super.like(column, val);
        return this;
    }

    public MyBatisQueryWrapper<T> likeIfPresent(String column, Object val) {
        if(val == null || val.equals(""))return this;
        return this.like(column, val);
    }

    /**
     * 判断不相等
     * @param column 列名
     * @param val 值
     */
    public MyBatisQueryWrapper<T> ne(String column, Object val) {
        super.ne(column, val);
        return this;
    }

    /**
     * 判断小于
     * @param column 列名
     * @param val 值
     */
    public MyBatisQueryWrapper<T> lt(String column, Object val) {
        super.lt(column, val);
        return this;
    }

    /**
     * 判断小于等于
     * @param column 列名
     * @param val 值
     */
    public MyBatisQueryWrapper<T> gt(String column, Object val) {
        super.gt(column, val);
        return this;
    }

    /**
     * 判断大于等于
     * @param column 列名
     * @param val 值
     */
    public MyBatisQueryWrapper<T> le(String column, Object val) {
        super.le(column, val);
        return this;
    }

    /**
     * 判断大于等于
     * @param column 列名
     * @param val 值
     */
    public MyBatisQueryWrapper<T> ge(String column, Object val) {
        super.ge(column, val);
        return this;
    }

    /**
     * 判断是否为空
     * @param column 列名
     */
    public MyBatisQueryWrapper<T> between(String column, Object val1, Object val2) {
        super.between(column, val1, val2);
        return this;
    }

    /**
     * 判断是否为空
     * @param column 列名
     */
    public MyBatisQueryWrapper<T> notBetween(String column, Object val1, Object val2) {
        super.notBetween(column, val1, val2);
        return this;
    }

    /**
     * 判断是否为空
     * @param column 列名
     */
    public MyBatisQueryWrapper<T> isNull(String column) {
        super.isNull(column);
        return this;
    }

    /**
     * 判断是否不为空
     * @param column 列名
     */
    public MyBatisQueryWrapper<T> isNotNull(String column) {
        super.isNotNull(column);
        return this;
    }

    /**
     * 判断是否相似
     * @param column 列名
     * @param val 值
     */
    public MyBatisQueryWrapper<T> likeLeft(String column, String val) {
        super.likeLeft(column, val);
        return this;
    }

    /**
     * 判断是否相似
     * @param column 列名
     * @param val 值
     */
    public MyBatisQueryWrapper<T> likeRight(String column, String val) {
        super.likeRight(column, val);
        return this;
    }
    /**
     * 判断是否不相似
     * @param column 列名
     * @param val 值
     */
    public MyBatisQueryWrapper<T> notLike(String column, String val) {
        super.notLike(column, val);
        return this;
    }

    /**
     * 判断是否在集合中
     * @param column 列名
     * @param coll 集合
     */
    public MyBatisQueryWrapper<T> in(String column, List<?> coll) {
        super.in(column, coll);
        return this;
    }

    /**
     * 判断是否在集合中，为空则不加入条件
     * @param column 列名
     * @param coll 集合
     */
    public MyBatisQueryWrapper<T> inIfPresent(String column, List<?> coll) {
        if(coll == null || coll.isEmpty())return this;
        return this.in(column, coll);
    }

    /**
     * 判断是否不在集合中
     * @param column 列名
     * @param coll 集合
     */
    public MyBatisQueryWrapper<T> notIn(String column, List<?> coll) {
        super.notIn(column, coll);
        return this;
    }
}
