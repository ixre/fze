package net.fze.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Times {
    public static final SimpleDateFormat DefaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 格式化如：2020-05-06T16:00:00.000Z的时间
    private static final SimpleDateFormat ISO_RFC_3339_24H_FULL_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    public static final TimeZone ZoneAsiaShangHai = TimeZone.getTimeZone("GMT+08:00");

    /**
     * 设置时区
     *
     * @param zone 时区,GMT+08:00 / GMT+8上海时间
     */
    public static void setTimeZone(TimeZone zone) {
        if (zone == null) zone = ZoneAsiaShangHai;
        System.setProperty("user.timezone", zone.getDisplayName());
        TimeZone.setDefault(zone);
    }

    /**
     * 字符串转换为时间
     *
     * @param s 字符串
     * @return 时间，如果格式不对，则返回null
     */
    private static Date time(String s) {
        return parseTime(s, null);
    }


    /**
     * 字符串转换为时间
     *
     * @param s      字符串
     * @param format 格式,如:yyyy-MM-dd HH:mm:ss
     * @return 时间，如果格式不对，则返回null
     */
    public static Date time(String s, String format) {
        return parseTime(s, format);
    }

    /**
     * 转换时间
     */
    private static Date parseTime(String s, String format) {
        try {
            return (Strings.isNullOrEmpty(format) ? DefaultDateFormat : new SimpleDateFormat(format)).parse(s);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 转换时间
     */
    public static Date parseISOTime(String s) {
        try {
            return ISO_RFC_3339_24H_FULL_FORMAT.parse(s);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化为时间字符串
     *
     * @param d      时间
     * @param format 格式
     * @return 字符串
     */
    public static String format(Date d, String format) {
        SimpleDateFormat fmt = Strings.isNullOrEmpty(format) ?
                DefaultDateFormat :
                new SimpleDateFormat(format);
        return fmt.format(d);
    }

    /**
     * 格式化为时间字符串
     *
     * @param unix   时间戳
     * @param format 格式
     * @return 字符串
     */
    public static String formatUnix(int unix, String format) {
        return format(unixTime(unix, 0), format);
    }

    /*
     * 将时间转换为时间戳
     */
    public static int unix(Date d) {
        return (int) (d.getTime() / 1000);
    }

    /**
     * 将unix时间转为日期对象
     *
     * @param unix 时间戳
     * @return 日期
     */
    public static Date unixTime(long unix, int millisecond) {
        return new Date(unix * 1000 + millisecond);
    }

    /**
     * 将字符串转为时间戳
     */
    public static int unix(String time) {
        Date d = parseTime(time, null);
        return d == null ? 0 : unix(d);
    }

    /**
     * 获取一个时间的开始时间戳
     */
    public static int unixOfDate(Date d) {
        Calendar c = new Calendar.Builder().setInstant(d).build();
        truncateTime(c);
        return intUnix(c.getTimeInMillis());
    }

    /**
     * 获取一个时间的结束时间戳
     */
    public static int unixOfDateOver(Date d) {
        return unixOfDate(d) + 3600 * 24 - 1;
    }

    /**
     * 将毫秒的unix时间转换为int
     */
    private static int intUnix(long time) {
        return (int) (time / 1000);
    }


    // 清除时间
    private static void truncateTime(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 计算一个月的开始日期时间戳
     */
    public static int unixOfMonth(Date d) {
        Calendar c = getCalendar(d);
        truncateTime(c);
        // 计算一个月第一天的开始时间
        c.set(Calendar.DAY_OF_MONTH, 1);
        return intUnix(c.getTimeInMillis());
    }

    /**
     * 计算一个月的结束时间戳
     */
    public static int unixOfMonthOver(Date d) {
        Calendar c = getCalendar(d);
        truncateTime(c);
        // 计算一个月第一天的开始时间
        c.set(Calendar.DAY_OF_MONTH, 1);
        int month = c.get(Calendar.MONTH);
        if (month + 1 < 12) {
            c.set(Calendar.MONTH, month + 1);
        } else {
            int year = c.get(Calendar.YEAR);
            c.set(Calendar.YEAR, year + 1);
            c.set(Calendar.MONTH, month + 1 - 12);
        }
        return intUnix(c.getTimeInMillis()) - 1;
    }

    /**
     * 添加时间,unit如： Calendar.YEAR
     */
    public static Date addTime(Date d, int unit, int value) {
        Calendar c = getCalendar(d);
        switch (unit) {
            case Calendar.YEAR:
                c.set(Calendar.YEAR, c.get(Calendar.YEAR) + value);
                break;
            case Calendar.MONTH:
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) + value);
                break;
            case Calendar.DAY_OF_MONTH:
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + value);
                break;
            case Calendar.HOUR:
                c.set(Calendar.HOUR, c.get(Calendar.HOUR) + value);
                break;
            case Calendar.MINUTE:
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + value);
                break;
            case Calendar.SECOND:
                c.set(Calendar.SECOND, c.get(Calendar.SECOND) + value);
                break;
            case Calendar.MILLISECOND:
                c.set(Calendar.MILLISECOND, c.get(Calendar.MILLISECOND) + value);
                break;
            default:
                throw new Error("不支持的Calendar单位");
        }
        return c.getTime();
    }

    public static Calendar getCalendar(Date d) {
        return new Calendar.Builder().setInstant(d).build();
    }

    /**
     * 转换为1970年的时间戳，只包含日期和月份
     *
     * @param d 时间
     * @return 时间戳
     */
    public static int getMonthDayUnix(Date d) {
        Calendar c = new Calendar.Builder().setInstant(d).build();
        truncateTime(c);
        int year = c.get(Calendar.YEAR);
        c.add(Calendar.YEAR, 1970 - year);
        return intUnix(c.getTimeInMillis());
    }

    /**
     * 返回时间戳
     *
     * @return 时间戳
     */
    public static int unix() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 在当前时间上添加秒
     *
     * @param second 秒
     * @return 时间戳
     */
    public static int unixAdd(int second) {
        return unix() + second;
    }

    // 计算多久以前
    public static String evalAgoTime(int unix) {
        int duration = unix() - unix;
        if (duration < 60) return agoTimeText(duration, "秒");
        if (duration < 3600) return agoTimeText(duration / 60, "分钟");
        if (duration < 86400) return agoTimeText(duration / 3600, "小时");
        if (duration < 86400 * 7) return agoTimeText(duration / 86400, "天");
        if (duration < 86400 * 30) return agoTimeText(duration / (86400 * 7), "周");
        if (duration < 86400 * 30 * 365) return agoTimeText(duration / (86400 * 30), "月");
        return agoTimeText(duration / (86400 * 365), "年");
    }

    private static String agoTimeText(int d, String unit) {
        return String.format("%d%s以前", d, unit);
    }
}
