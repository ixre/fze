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

    public AbstractReportDataProvider(String cfgPath) {
        if (Strings.isNullOrEmpty(cfgPath)) {
            throw new IllegalArgumentException("cfgPath like \"classpath:query\"");
        }
        this.cfgPath = cfgPath;
    }

    @NotNull
    @Override
    public Connection getConn() {
        throw new RuntimeException("请在实现类中重写数据源获取实现");

        // 实现可参考如下：
        //    @Inject private DataSource ds;
        //        try {
        //            return this.ds.getConnection();
        //        } catch (SQLException e) {
        //            throw new RuntimeException("can't get any connection, message:"+e.getMessage());
        //        }
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
