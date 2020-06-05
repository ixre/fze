package net.fze.component.report

import net.fze.commons.std.Types

class ReportParses {
    companion object{
        /** parse time range like [2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z] */
        fun parseTimeRange(s:String):Array<Int> {
            if (s.isEmpty()) return arrayOf()
            var src = s
            if (src[0] == '[') {
                src = src.substring(1)
            }
            var len = src.length
            if (src[len - 1] == ']'){
                src = src.substring(0, len - 1)
            }
            return src.split(",")
                    .map { Types.time.unix(Types.time.parseISOTime(it.trim())) }
                    .toTypedArray()
        }
    }
}