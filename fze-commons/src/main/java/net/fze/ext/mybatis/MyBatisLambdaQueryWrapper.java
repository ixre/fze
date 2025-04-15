package net.fze.ext.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.fze.domain.query.IQueryWrapper;

public class MyBatisLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> implements IQueryWrapper {

}
