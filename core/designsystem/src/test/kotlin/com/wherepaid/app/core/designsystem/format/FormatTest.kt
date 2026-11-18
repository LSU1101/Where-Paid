package com.wherepaid.app.core.designsystem.format

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class FormatTest {
    private fun millis(y: Int, m: Int, d: Int, h: Int, min: Int) =
        LocalDate.of(y, m, d).atTime(h, min).atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli()

    @Test
    fun `원 단위 금액`() {
        assertEquals("61,500원", formatWon(61_500))
        assertEquals("-4,500원", formatWon(-4_500))
        assertEquals("0원", formatWon(0))
    }

    @Test
    fun `지도 핀의 만 단위 금액 (시안 예시)`() {
        assertEquals("6.2만", formatManwon(61_500))
        assertEquals("4.8만", formatManwon(48_200))
        assertEquals("1만", formatManwon(9_800))
        assertEquals("2.4만", formatManwon(24_000))
        assertEquals("27만", formatManwon(269_900))
    }

    @Test
    fun `날짜 표기`() {
        val at = millis(2026, 10, 10, 14, 32) // 토요일
        assertEquals("10.10 (토) 14:32", formatDateWithWeekday(at))
        assertEquals("10.10 14:32", formatShortDateTime(at, today = LocalDate.of(2026, 10, 12)))
        assertEquals("오늘 14:32", formatShortDateTime(at, today = LocalDate.of(2026, 10, 10)))
    }

    @Test
    fun `거리 표기`() {
        assertEquals("40m", formatDistance(40.2))
        assertEquals("1.2km", formatDistance(1_200.0))
    }
}
