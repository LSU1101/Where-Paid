package com.wherepaid.app.core.model

import com.wherepaid.app.core.model.fake.SampleData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.YearMonth

class RulesTest {
    private val month = YearMonth.of(2026, 10)
    private val sample = SampleData.transactions(month)

    private fun payment(id: String, amount: Long, paidAt: Long, canceled: Long = 0) = sample.first().copy(
        id = id, merchantRaw = "샘플가게", amount = amount, paidAt = paidAt,
        cancellations = if (canceled > 0) listOf(Cancellation(canceled, paidAt + 1)) else emptyList(),
    )

    @Test
    fun `취소는 금액이 같은 결제를 우선한다`() {
        val payments = listOf(payment("a", 5_000, 3), payment("b", 3_000, 2), payment("c", 9_000, 1))
        assertEquals("b", CancellationRules.findTarget(payments, "샘플가게", 3_000)?.id)
    }

    @Test
    fun `금액이 같은 결제가 없으면 남은 금액이 충분한 가장 최근 결제`() {
        val payments = listOf(payment("old", 9_000, 1), payment("new", 5_000, 2))
        assertEquals("new", CancellationRules.findTarget(payments, "샘플가게", 4_000)?.id)
    }

    @Test
    fun `이미 취소된 금액을 빼고 남은 금액이 모자라면 대상이 아니다`() {
        val payments = listOf(payment("a", 5_000, 1, canceled = 5_000))
        assertNull(CancellationRules.findTarget(payments, "샘플가게", 5_000))
    }

    @Test
    fun `월 합계는 취소를 반영하고 온라인을 따로 센다`() {
        val summary = MonthSummary.of(sample)
        val online = sample.filter { it.status == PlaceStatus.ONLINE }.sumOf { it.amount }
        val offline = sample.filter { it.status != PlaceStatus.ONLINE }.sumOf { it.amount } - 4_500

        assertEquals(online, summary.onlineTotal)
        assertEquals(offline, summary.offlineTotal)
        assertEquals(6, summary.needsPlaceCount)
        assertEquals(SampleData.starbucksSeongsu, summary.places.first().place)
        // 6,800 + 5,300 + (4,500 − 취소 4,500) + 7,200 + 12,000
        assertEquals(31_300L, summary.places.first().total)
        assertEquals(5, summary.places.first().count)
    }

    @Test
    fun `상태 이동은 후보와 이유를 정리한다`() {
        val ambiguous = sample.first { it.status == PlaceStatus.AMBIGUOUS }
        val mapped = PlaceTransitions.choosePlace(ambiguous, ambiguous.candidates.first())

        assertEquals(PlaceStatus.MAPPED, mapped.status)
        assertTrue(mapped.candidates.isEmpty())
        assertNull(mapped.unmatchedReason)
        assertEquals(UnmatchedReason.MARKED_OFFLINE, PlaceTransitions.markOffline(mapped).unmatchedReason)
    }

    @Test
    fun `거리 계산`() {
        val d = distanceMeters(LatLng(37.5665, 126.9780), LatLng(37.5665, 126.9790))
        assertTrue(d in 85.0..95.0)
    }
}
