/**
 * Copyright (C) 2007-2025 56X.NET,All rights reserved.
 * <p>
 * name : MyBatisUpdateWrapper.java
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2025-12-12 19:13
 * description :
 * history :
 */
package net.fze.ext.mybatis;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import net.fze.domain.query.IUpdateWrapper;

/**
 * @author jarrysix
 */
public class MyBatisLambdaUpdateWrapper<T> extends LambdaUpdateWrapper<T> implements IUpdateWrapper {
}