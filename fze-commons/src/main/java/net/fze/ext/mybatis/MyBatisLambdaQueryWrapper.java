package net.fze.ext.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import net.fze.domain.query.IQueryWrapper;

import java.util.List;

public class MyBatisLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> implements IQueryWrapper {
    /**
     * 转换为QueryWrapper
     */
    public LambdaQueryWrapper<T> toQueryWrapper() {
        return this;
    }
    /**
     * 判断相等
     * @param column 列名
     * @param val 值
     */
    public MyBatisLambdaQueryWrapper<T> eq(SFunction<T,?> column, Object val) {
        super.eq(column, val);
        return this;
    }

    /**
     * 判断是否相似
     * @param column    列名
     * @param val       值
     */
    public MyBatisLambdaQueryWrapper<T> like(SFunction<T,?> column, Object val) {
        super.like(column, val);
        return this;
    }

    /**
     * 判断不相等
     * @param column 列名
     * @param val 值
     */
    public MyBatisLambdaQueryWrapper<T> ne(SFunction<T,?> column, Object val) {
        super.ne(column, val);
        return this;
    }

    /**
     * 判断小于
     * @param column 列名
     * @param val 值
     */
    public MyBatisLambdaQueryWrapper<T> lt(SFunction<T,?> column, Object val) {
        super.lt(column, val);
        return this;
    }

    /**
     * 判断小于等于
     * @param column 列名
     * @param val 值
     */
    public MyBatisLambdaQueryWrapper<T> gt(SFunction<T,?> column, Object val) {
        super.gt(column, val);
        return this;
    }

    /**
     * 判断大于等于
     * @param column 列名
     * @param val 值
     */
    public MyBatisLambdaQueryWrapper<T> le(SFunction<T,?> column, Object val) {
        super.le(column, val);
        return this;
    }

    /**
     * 判断大于等于
     * @param column 列名
     * @param val 值
     */
    public MyBatisLambdaQueryWrapper<T> ge(SFunction<T,?> column, Object val) {
        super.ge(column, val);
        return this;
    }

    /**
     * 判断是否为空
     * @param column 列名
     */
    public MyBatisLambdaQueryWrapper<T> between(SFunction<T,?> column, Object val1, Object val2) {
        super.between(column, val1, val2);
        return this;
    }

    /**
     * 判断是否为空
     * @param column 列名
     */
    public MyBatisLambdaQueryWrapper<T> notBetween(SFunction<T,?> column, Object val1, Object val2) {
        super.notBetween(column, val1, val2);
        return this;
    }

    /**
     * 判断是否为空
     * @param column 列名
     */
    public MyBatisLambdaQueryWrapper<T> isNull(SFunction<T,?> column) {
        super.isNull(column);
        return this;
    }

    /**
     * 判断是否不为空
     * @param column 列名
     */
    public MyBatisLambdaQueryWrapper<T> isNotNull(SFunction<T,?> column) {
        super.isNotNull(column);
        return this;
    }

    /**
     * 判断是否相似
     * @param column 列名
     * @param val 值
     */
    public MyBatisLambdaQueryWrapper<T> likeLeft(SFunction<T,?> column, String val) {
        super.likeLeft(column, val);
        return this;
    }

    /**
     * 判断是否相似
     * @param column 列名
     * @param val 值
     */
    public MyBatisLambdaQueryWrapper<T> likeRight(SFunction<T,?> column, String val) {
        super.likeRight(column, val);
        return this;
    }
    /**
     * 判断是否不相似
     * @param column 列名
     * @param val 值
     */
    public MyBatisLambdaQueryWrapper<T> notLike(SFunction<T,?> column, String val) {
        super.notLike(column, val);
        return this;
    }

    /**
     * 判断是否在集合中
     * @param column 列名
     * @param coll 集合
     */
    public MyBatisLambdaQueryWrapper<T> in(SFunction<T,?> column, List<?> coll) {
        super.in(column, coll);
        return this;
    }

    /**
     * 判断是否不在集合中
     * @param column 列名
     * @param coll 集合
     */
    public MyBatisLambdaQueryWrapper<T> notIn(SFunction<T,?> column, List<?> coll) {
        super.notIn(column, coll);
        return this;
    }
}
