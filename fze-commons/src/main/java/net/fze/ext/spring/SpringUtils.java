/**
 * Copyright (C) 2007-2024 56X.NET,All rights reserved.
 * <p>
 * name : SpringUtils.java
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2024-03-19 17:02
 * description :
 * history :
 */
package net.fze.ext.spring;

import net.fze.util.Types;

/**
 * Spring工具类
 * @author jarrysix
 */
public class SpringUtils {
    /**
     * 获取配置信息名称
     * @return 配置名称,如: dev,prod
     */
    public String getProfile(){
        return Types.orValue( System.getProperty("spring.profiles.active"),"");
    }
}