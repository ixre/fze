package net.fze.ext.report;

import net.fze.common.data.PagingResult;

import java.util.Map;

/**
 * 报表项仓库
 * @author jarrysix
 */
public class ReportHub {
    private final ItemManager _manager;

    /**
     * 创建仓库实例
     *
     * @param provider   数据源提供者
     * @param rootPath   跟路径,默认为: classpath:
     * @param cacheFiles 是否缓存文件, 识别本地文件有效
     */
    public ReportHub(IConnProvider provider, String rootPath, boolean cacheFiles) {
        _manager = new ItemManager(provider, rootPath.isEmpty() ? "classpath:" : rootPath, cacheFiles);
    }

    public PagingResult<Map<String, Object>> fetchData(String portal, Params p, int page, int size) {
        IReportPortal item = _manager.getItem(portal);
        if (page > 0) {
            p.set("page_index", page);
        }
        if (size > 0) {
            p.set("page_size", size);
        }
        return item.getSchemaAndData(p);
    }
}
