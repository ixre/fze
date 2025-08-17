package net.fze.domain.query;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询工具
 */
public class QueryUtils {

    /**
     * 格式化数据字段名，将下划线后的字母转换为大写
     */
    public static void formatRowFields(Map<String, Object> mp) {
        Map<String, Object> cpMp = new HashMap<>(mp);
        // 使用 Matcher 和 Pattern 将下划线后的字母转换为大写
        Pattern pattern = Pattern.compile("_(.)");
        for (String key : cpMp.keySet()) {
            Matcher matcher = pattern.matcher(key.toLowerCase());
            StringBuffer buffer = new StringBuffer();
            boolean hasMatch = false;
            while (matcher.find()) {
                hasMatch = true;
                matcher.appendReplacement(buffer, matcher.group(1).toUpperCase());
            }
            String newKey;
            if (hasMatch) {
                matcher.appendTail(buffer);
                // 使用正则表达式将下划线后的字母转换为大写
                newKey = buffer.toString();
            } else {
                // 如果首字母大写，则小写
                newKey = Character.isUpperCase(key.charAt(0)) ? key.toLowerCase() : key;
            }
            if (!newKey.equals(key)) {
                mp.put(newKey, cpMp.get(key));
                mp.remove(key);
            }
        }
    }
}
