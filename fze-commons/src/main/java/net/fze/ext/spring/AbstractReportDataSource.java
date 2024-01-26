package net.fze.ext.spring;

import net.fze.common.data.PagingResult;
import net.fze.ext.report.IConnProvider;
import net.fze.ext.report.Params;
import net.fze.ext.report.ReportHub;
import net.fze.util.Strings;
import net.fze.util.Systems;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 报表数据源
 * @author jarrysix
 */
public abstract class AbstractReportDataSource implements IConnProvider {
    private final HashMap<String, ReportHub> ReportHubMap = new HashMap<>();
    private final String cfgPath;

    public AbstractReportDataSource(String cfgPath){
        if(Strings.isNullOrEmpty(cfgPath)){
            throw new IllegalArgumentException("cfgPath like \"classpath:query\"");
        }
        this.cfgPath = cfgPath;
    }
    @Inject
    private DataSource ds;

    @NotNull
    @Override
    public Connection getConn() {
        try {
            return this.ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("can't get any connection, message:"+e.getMessage());
        }
    }

    private void lazyInit() {
        Systems.resolveEnvironment(AbstractReportDataSource.class);
        boolean cache = !Systems.dev();
        ReportHubMap.put("default", new ReportHub(this, this.cfgPath, cache));
    }

    private ReportHub getHub(String key) {
        if (this.ReportHubMap.isEmpty()) {
            this.lazyInit();
        }
        if (ReportHubMap.containsKey(key)) {
            return ReportHubMap.get(key);
        }
        return ReportHubMap.get("default");
    }

    public PagingResult<Map<String, Object>> fetchData(String key, String portal, Params params, int page, int rows) {
        ReportHub hub = this.getHub(key);
        if (hub == null) {
            throw new Error("datasource not exists");
        }
        return hub.fetchData(portal, params, page, rows);
    }

}
