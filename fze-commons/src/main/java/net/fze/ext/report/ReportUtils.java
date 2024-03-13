package net.fze.ext.report;

import net.fze.common.data.SqlUtil;
import net.fze.util.Strings;
import net.fze.util.Times;
import net.fze.util.TypeConv;
import net.fze.util.Types;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 报表工具类
 */
public class ReportUtils {
    /**
     * 获取列映射数组
     */
    public static ItemConfig readItemConfigFromXml(String xmlFilePath) {
        try {
            if (xmlFilePath.startsWith("classpath:")) {
                return readItemConfigFromResources(xmlFilePath);
            }
            FileInputStream fs = new FileInputStream(xmlFilePath);
            return parseItemConfig(fs);
          //  String xmlContent = IoUtils.readToEnd(fs, "UTF-8");
           // return XmlUtils.deserializeObject(xmlContent);
        } catch (Throwable ex) {
            throw new RuntimeException("resolve xml file failed! filepath="+xmlFilePath,ex);
        }
    }

    private static ItemConfig parseItemConfig(InputStream fs) throws Exception {
        return SAXConfigItemParser.parse(fs);
        // 不能使用XmlDecoder因为Xml中的字段为大写,无法映射到Java类的小写开头属性
//        String xmlContent = IoUtils.readToEnd(fs, "UTF-8");
//        return XmlUtils.deserializeObject(xmlContent);
        // 需要引用其他的库
//        JAXBContext ctx = JAXBContext.newInstance(ItemConfig.class);
//        ItemConfig cfg = (ItemConfig) ctx.createUnmarshaller().unmarshal(fs);
//        fs.close();
//        return cfg;
    }

    /**
     * 从资源中读取配置文件内容
     *
     * @param resourcePath 资源路径
     * @return 配置项
     */
    private static ItemConfig readItemConfigFromResources(String resourcePath) throws Exception {
        String resPath = resourcePath.replace("classpath:", "");
        if (resPath.startsWith("/")) {
            resPath = resPath.substring(1);
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream fs = loader.getResourceAsStream(resPath);
        if (fs == null) {
            throw new RuntimeException("not found query item in classpath; path: " + resPath);
        }
        return parseItemConfig(fs);
    }

    /**
     * 转换列与字段的映射
     */
    public static ColumnMapping[] parseColumnMapping(String str) {
        throw new Error("not implement");
        /*
         * re, err := regexp.Compile("([^:]+):([^;]*);*\\s*")
         * if err != nil {
         * return nil
         * }
         * var matches = re.FindAllStringSubmatch(str, -1)
         * if matches == nil {
         * return nil
         * }
         * columnsMapping := make([]ColumnMapping, len(matches))
         * for i, v := range matches {
         * columnsMapping[i] = ColumnMapping{Field: v[1], Name: v[2]}
         * }
         * return columnsMapping
         */
    }

    /**
     * 转换参数
     */
    public static Params parseParams(String paramMappings) {
        Params params = new Params(new HashMap<>());
        if (paramMappings != null && paramMappings.length() > 1) {
            try {
                String query = URLDecoder.decode(paramMappings, "UTF-8");
                if (query.charAt(0) == '{') {
                    Map<String, Object> mp = Types.fromJson(query, Map.class);
                    mp.forEach(params::set);
                } else {
                    String mapping = query.replace("%3d", "=");
                    String[] paramsArr = mapping.split(";");
                    String[] splitArr;
                    // 添加传入的参数
                    for (String s : paramsArr) {
                        splitArr = s.split(":");
                        int l = splitArr[0].length() + 1;
                        params.set(splitArr[0], s.substring(l));
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return params;
    }

    /**
     * 判断是否存在危险的注入操作
     */
    static boolean checkInject(String str) {
        return SqlUtil.checkInject(str);
    }

    // 格式化sql语句
    public static String sqlFormat(String sql, Map<String, Object> ht) {
        String formatted = SqlBuilder.resolve(sql, ht);
        for (Map.Entry<String, Object> e : ht.entrySet()) {
            formatted = formatted.replace(
                    "{" + e.getKey() + "}",
                    TypeConv.toString(e.getValue()));
        }
        return formatted.trim();
    }

    /**
     * parse time range like
     * [2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z]
     * 2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z
     * [1661335074674,1661939874674]
     * 1661335074674,1661939874674
     */
    public static List<Long> parseTimeRange(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return new ArrayList<>();
        }
        String src = s;
        if (src.charAt(0) == '[') {
            src = src.substring(1);
        }
        int len = src.length();
        if (src.charAt(len - 1) == ']') {
            src = src.substring(0, len - 1);
        }
        if (src.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(src.split(","))
                .map(it -> {
                    if (Pattern.matches("^\\d+$", it)) {
                        Long l = TypeConv.toLong(it);
                        return l > 1E12 ? l / 1000 : l;
                    }
                    return Times.unix(Objects.requireNonNull(Times.parseISOTime(it.trim())));
                })
                .collect(Collectors.toList());
    }

    public static String getTimeRangeSQL(List<Long> range, String field, boolean timestamp) {
        if (range.isEmpty()) {
            return "";
        }
        if (range.size() == 1) {
            return timestamp ? String.format("%s >= %d", field, range.get(0))
                    : String.format("%s >= '%s'", field, Times.formatUnix(range.get(0), "yyyy-MM-dd HH:mm:ss"));
        }
        if (range.get(1) % 3600L == 0L) {
            // 添加结束时间
            range.set(1, range.get(1) + 3600L * 24 - 1);
        }
        if (!timestamp) {
            return String.format("%s BETWEEN '%s' AND '%s'", field,
                    Times.formatUnix(range.get(0), "yyyy-MM-dd HH:mm:ss"),
                    Times.formatUnix(range.get(1), "yyyy-MM-dd HH:mm:ss"));
        }
        return String.format("%s BETWEEN %d AND %d", field, range.get(0), range.get(1));
    }

    /**
     * 生成时间范围SQL,使用时间字符串
     *
     * @param range : [2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z]
     */
    public static String timeSQLByJSONTime(Object range, String field) {
        return timeSQLByJSONTime(range, field, false);
    }

    /**
     * 生成时间范围SQL,使用时间戳
     *
     * @param range : [1661335074674,1661939874674]
     */
    public static String timestampSQLByJSONTime(Object range, String field) {
        return timeSQLByJSONTime(range, field, true);
    }

    /**
     * 生成时间范围SQL
     *
     * @param range : [2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z]
     */
    private static String timeSQLByJSONTime(Object range, String field, boolean timestamp) {
        if (range instanceof String) {
            List<Long> arr = parseTimeRange((String) range);
            return getTimeRangeSQL(arr, field, timestamp);
        }
        if (range instanceof List) {
            List<Object> r = (List) range;
            if (r.isEmpty()) {
                return "";
            }
            if (r.get(0) instanceof String) {
                List<Long> arr = r.stream().map(a -> Times.unix(Objects.requireNonNull(Times.parseISOTime(((String) a).trim()))))
                        .collect(Collectors.toList());
                return getTimeRangeSQL(arr, field, timestamp);
            }
            if (r.get(0) instanceof Number) {
                List<Long> arr = r.stream().map(a -> {
                    Long l = TypeConv.toLong(a);
                    return l > 1E12 ? l / 1000 : l; // 1E12 为13位数字
                }).collect(Collectors.toList());
                return getTimeRangeSQL(arr, field, timestamp);
            }
        }
        //throw new Error("range only support List<Long> or List<String>");
        return null;
    }
}
