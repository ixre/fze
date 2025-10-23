package net.fze.ext.report.v2;

import java.util.List;
import java.util.Map;

/**
 * 导出策略
 */
public interface IExportFileStrategy {
    /**
     * 获取导出字节
     *
     * @param mapList 数据
     * @param props   属性
     * @param headers 头部
     * @return 字节
     */
    byte[] getExportBytes(List<Map<String, Object>> mapList, List<String> props, List<String> headers);
}
