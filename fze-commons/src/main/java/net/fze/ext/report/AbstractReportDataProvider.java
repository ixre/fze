package net.fze.ext.report;

import net.fze.common.data.PagingResult;
import net.fze.util.Strings;
import net.fze.util.Systems;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 报表数据源
 *
 * @author jarrysix
 */
public abstract class AbstractReportDataProvider implements IConnProvider, IReportDataProvider {
    private final HashMap<String, ReportHub> ReportHubMap = new HashMap<>();
    private final String cfgPath;

    protected AbstractReportDataProvider(){
        this.cfgPath = "classpath:query";
    }

    protected AbstractReportDataProvider(String cfgPath) {
        if (Strings.isNullOrEmpty(cfgPath)) {
            throw new IllegalArgumentException("cfgPath like \"super(\"classpath:query\");\"");
        }
        this.cfgPath = cfgPath;
    }

    /**
     * 绑定报表查询容器
     *
     * @param container 容器字典
     * @param cfgPath   配置目录路径
     */
    @Override
    public void bind(HashMap<String, ReportHub> container, String cfgPath) {
        boolean cache = !Systems.dev();
        container.put("default", new ReportHub(this, cfgPath, cache));
    }

    private ReportHub getHub(String key) {
        if (this.ReportHubMap.isEmpty()) {
            this.bind(this.ReportHubMap, this.cfgPath);
        }
        if (ReportHubMap.containsKey(key)) {
            return ReportHubMap.get(key);
        }
        return ReportHubMap.get("default");
    }

    @Override
    public PagingResult<Map<String, Object>> fetchData(String key, String portal, Params params, int page, int rows) {
        ReportHub hub = this.getHub(key);
        if (hub == null) {
            throw new Error("datasource not exists");
        }
        return hub.fetchData(portal, params, page, rows);
    }

}
