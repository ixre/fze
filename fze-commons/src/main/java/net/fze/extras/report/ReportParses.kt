package net.fze.extras.report

import net.fze.util.Times

class ReportParses {
    companion object {
        /** parse time range like [2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z] */
        @JvmStatic
        fun parseTimeRange(s: String): MutableList<Long> {
            if (s.isEmpty()) return mutableListOf()
            var src = s
            if (src[0] == '[') {
                src = src.substring(1)
            }
            var len = src.length
            if (src[len - 1] == ']') {
                src = src.substring(0, len - 1)
            }
            return src.split(",")
                .map { Times.unix(Times.parseISOTime(it.trim())) }
                .toMutableList()
        }
    }
}