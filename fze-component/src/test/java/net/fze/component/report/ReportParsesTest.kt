package net.fze.component.report

import net.fze.commons.std.Types
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*

internal class ReportParsesTest{
    @Test
    fun testParseDateRange(){
        val s = "[2020-05-06T16:00:00.000Z, 2020-05-08T16:00:00.000Z]"
        val e = ReportParses.parseTimeRange(s)
        print(e==null)
    }
}