package com.wherepaid.app.core.designsystem.format

import java.text.NumberFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToLong

/** 시안의 금액·날짜 표기. 화면마다 따로 만들지 말고 여기 함수를 쓴다. */

private val KOREA: ZoneId = ZoneId.of("Asia/Seoul")
private val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

/** 61500 → "61,500원", -4500 → "-4,500원" */
fun formatWon(amount: Long): String = numberFormat.format(amount) + "원"

/**
 * 지도 핀·지역 라벨의 짧은 금액: 62000 → "6.2만", 9800 → "1만", 48000 → "4.8만", 120000 → "12만".
 * 만 단위 소수 첫째 자리에서 반올림하고 ".0"은 뗀다. 만 원 미만(5천 원 미만)은 "0.4만"처럼 쓴다.
 */
fun formatManwon(amount: Long): String {
    val tenths = (amount / 1_000.0).roundToLong() // 0.1만 단위
    val whole = tenths / 10
    val fraction = tenths % 10
    return if (fraction == 0L) "${whole}만" else "$whole.${fraction}만"
}

private val monthDayTime = DateTimeFormatter.ofPattern("MM.dd HH:mm")
private val time = DateTimeFormatter.ofPattern("HH:mm")
private val monthDay = DateTimeFormatter.ofPattern("MM.dd")

private fun Long.toZoned() = Instant.ofEpochMilli(this).atZone(KOREA)

/** 목록 보조줄: "10.09 19:20", 오늘이면 "오늘 08:41" */
fun formatShortDateTime(epochMillis: Long, today: LocalDate = LocalDate.now(KOREA)): String {
    val zoned = epochMillis.toZoned()
    return if (zoned.toLocalDate() == today) "오늘 ${zoned.format(time)}" else zoned.format(monthDayTime)
}

/** 핀 상세의 결제 줄: "10.11 (토) 14:32" */
fun formatDateWithWeekday(epochMillis: Long): String {
    val zoned = epochMillis.toZoned()
    return "${zoned.format(monthDay)} (${zoned.dayOfWeek.korean()}) ${zoned.format(time)}"
}

private fun DayOfWeek.korean(): String = when (this) {
    DayOfWeek.MONDAY -> "월"
    DayOfWeek.TUESDAY -> "화"
    DayOfWeek.WEDNESDAY -> "수"
    DayOfWeek.THURSDAY -> "목"
    DayOfWeek.FRIDAY -> "금"
    DayOfWeek.SATURDAY -> "토"
    DayOfWeek.SUNDAY -> "일"
}

/** 거리: 40 → "40m", 1200 → "1.2km" */
fun formatDistance(meters: Double): String =
    if (meters < 1_000) "${meters.roundToLong()}m" else "%.1fkm".format(Locale.ROOT, meters / 1_000)
