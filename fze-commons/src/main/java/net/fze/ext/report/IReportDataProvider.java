/**
 * Copyright (C) 2007-2024 56X.NET,All rights reserved.
 * <p>
 * name : IReportDataProvider.java
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2024-03-06 17:22
 * description :
 * history :
 */
package net.fze.ext.report;

import net.fze.common.data.PagingResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 导出数据提供者
 * @author jarrysix
 */
public interface IReportDataProvider {
    /**
     * 绑定报表查询容器
     * @param container 容器字典
     * @param cfgPath 配置目录路径
     */
     void bind(HashMap<String, ReportHub> container, String cfgPath);
    /**
     * 获取数据
     * @param key 数据源key
     * @param portal 查询文件路径
     * @param params 参数
     * @param page 页码
     * @param rows 页行
     * @return 数据行
     */
    PagingResult<Map<String, Object>> fetchData(String key, String portal, Params params, int page, int rows);
}
