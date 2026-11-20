package com.wherepaid.app.feature.placecheck

import com.wherepaid.app.core.model.PlaceStatus
import com.wherepaid.app.core.model.fake.SampleData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.YearMonth

class PlaceCheckUiStateTest {
    private val month = YearMonth.of(2026, 10)
    private val sample = SampleData.transactions(month)
    private val today = month.atDay(11)

    @Test
    fun `느낌표 핀과 매칭실패함을 나누고 매칭 대기를 먼저 둔다`() {
        val state = toPlaceCheckUiState(sample, isOnline = true, today = today)

        assertEquals(listOf("GS25", "이디야커피"), state.candidates.map { it.merchant })
        assertEquals(PlaceCheckKind.PendingSearch, state.manual.first().kind)
        assertEquals(listOf("씨유 영등포아크로타", "(주)케이에프씨코리", "홍길동상회", "KCP"), state.manual.map { it.merchant })
        assertEquals(6, state.total)
    }

    @Test
    fun `보조 문구는 시안 형식을 따른다`() {
        val state = toPlaceCheckUiState(sample, isOnline = true, today = today)

        assertEquals("오늘 08:41 · 근처 후보 3곳", state.candidates.first().detail)
        assertEquals("3,400원", state.candidates.first().amount)
        assertEquals("10.09 19:20 · 위치를 못 받았어요", state.manual.first { it.merchant == "(주)케이에프씨코리" }.detail)
        assertEquals("10.07 12:05 · 근처에서 못 찾았어요", state.manual.first { it.merchant == "홍길동상회" }.detail)
        assertEquals("10.04 20:11 · 오프라인 결제로 옮김", state.manual.first { it.merchant == "KCP" }.detail)
    }

    @Test
    fun `오프라인 안내는 오프라인이고 매칭 대기가 있을 때만`() {
        assertTrue(toPlaceCheckUiState(sample, isOnline = false, today = today).showOfflineNote)
        assertFalse(toPlaceCheckUiState(sample, isOnline = true, today = today).showOfflineNote)
        val noPending = sample.filter { it.status != PlaceStatus.PENDING_SEARCH }
        assertFalse(toPlaceCheckUiState(noPending, isOnline = false, today = today).showOfflineNote)
    }

    @Test
    fun `장소가 정해진 결제와 온라인 결제는 나오지 않고, 없으면 빈 상태`() {
        val done = sample.filter { it.status == PlaceStatus.MAPPED || it.status == PlaceStatus.ONLINE }
        val state = toPlaceCheckUiState(done, isOnline = true, today = today)

        assertTrue(state.isEmpty)
        assertFalse(PlaceCheckUiState().isEmpty) // 불러오는 중에는 빈 상태를 보여주지 않는다
    }
}
