package net.fze.extras.report;

import net.fze.util.Strings;
import net.fze.util.Times;
import net.fze.util.TypeConv;
import net.fze.util.Types;

import javax.xml.bind.JAXBContext;
import java.io.File;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReportUtils {
    private static final Pattern injectRegexp = Pattern.compile(
            "\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|DELETE.+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)");

    /**
     * 获取列映射数组
     */
    public static ItemConfig readItemConfigFromXml(String xmlFilePath) {
        try {
            File f = new File(xmlFilePath);
            JAXBContext ctx = JAXBContext.newInstance(ItemConfig.class);
            return (ItemConfig) ctx.createUnmarshaller().unmarshal(f);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
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
            String query = URLDecoder.decode(paramMappings);
            if (query.charAt(0) == '{') {
                Map<String, Object> mp = Types.fromJson(query, Map.class);
                mp.forEach((k, v) -> {
                    params.set(k, v);
                });
            } else {
                String mapping = query.replace("%3d", "=");
                String[] paramsArr = mapping.split(";");
                String[] splitArr;
                // 添加传入的参数
                for (int i = 0; i < paramsArr.length; i++) {
                    splitArr = paramsArr[i].split(":");
                    int l = splitArr[0].length() + 1;
                    params.set(splitArr[0], paramsArr[i].substring(l));
                }
            }
        }
        return params;
    }

    /**
     * 判断是否存在危险的注入操作
     */
    static boolean checkInject(String str) {
        return !injectRegexp.matcher(str).find();
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
     * parse time range like [2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z]
     */
    public static List<Long> parseTimeRange(String s) {
        if (Strings.isNullOrEmpty(s))
            return new ArrayList<>();
        String src = s;
        if (src.charAt(0) == '[') {
            src = src.substring(1);
        }
        int len = src.length();
        if (src.charAt(len - 1) == ']') {
            src = src.substring(0, len - 1);
        }
        if (src.isEmpty())
            return new ArrayList<>();
        return Arrays.stream(src.split(","))
                .map(it -> {
                    if (Pattern.matches("^\\d+$", it)) {
                        Long l = TypeConv.toLong(it);
                        return l > 1E12 ? l / 1000 : l;
                    }
                    return Times.unix(Times.parseISOTime(it.trim()));
                })
                .collect(Collectors.toList());
    }

    public static String getTimeRangeSQL(List<Long> range, String field, boolean timestamp) {
        if (range.isEmpty())
            return "";
        if (range.size() == 1) {
            return timestamp ? String.format("%s >= %d", field, range.get(0))
                    : String.format("%s >= '%s'", Times.formatUnix(range.get(0), "yyyy-MM-dd HH:mm:ss"));
        }
        if (range.get(1) % 3600L == 0L)
            range.set(1, range.get(1) + 3600L * 24 - 1); // 添加结束时间
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
     * @range : [2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z]
     */
    public static String timeSQLByJSONTime(Object range, String field) {
        return timeSQLByJSONTime(range, field, false);
    }

    /**
     * 生成时间范围SQL,使用时间戳
     *
     * @range : [2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z]
     */
    public static String timestampSQLByJSONTime(Object range, String field) {
        return timeSQLByJSONTime(range, field, true);
    }

    /**
     * 生成时间范围SQL
     *
     * @range : [2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z]
     */
    private static String timeSQLByJSONTime(Object range, String field, boolean timestamp) {
        if (range instanceof String) {
            List<Long> arr = parseTimeRange((String) range);
            return getTimeRangeSQL(arr, field, timestamp);
        }
        if (range instanceof List) {
            List<Object> r = (List) range;
            if (r.size() == 0)
                return "";
            if (r.get(0) instanceof String) {
                List<Long> arr = r.stream().map(a -> Times.unix(Times.parseISOTime(((String) a).trim())))
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
