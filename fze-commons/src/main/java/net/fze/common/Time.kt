package net.fze.common

/** 时间单位 */
enum class TimeUnit(var value: Int) {
    /** 秒 */
    Second(1),
    /** 分 */
    Minute(60),
    /** 时 */
    Hour(3600),
    /** 天 */
    Day(86400),
}