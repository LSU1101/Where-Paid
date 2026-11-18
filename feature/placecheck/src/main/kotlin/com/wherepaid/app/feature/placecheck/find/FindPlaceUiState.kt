package com.wherepaid.app.feature.placecheck.find

import com.wherepaid.app.core.designsystem.format.formatDistance
import com.wherepaid.app.core.designsystem.format.formatShortDateTime
import com.wherepaid.app.core.designsystem.format.formatWon
import com.wherepaid.app.core.model.LatLng
import com.wherepaid.app.core.model.Place
import com.wherepaid.app.core.model.Transaction
import com.wherepaid.app.core.model.distanceMeters
import java.time.LocalDate

/** S-06 매장 찾기 화면이 그리는 것. */
data class FindPlaceUiState(
    /** "(주)케이에프씨코리 · 9,800원 · 10.09 19:20" */
    val summary: String = "",
    val query: String = "",
    val results: SearchResults = SearchResults.Idle,
    val selectedId: String? = null,
    /** D-02 등록 전 나가기 확인 */
    val showLeaveDialog: Boolean = false,
    /** 등록했거나 나가기로 했다. 화면을 닫는다. */
    val finished: Boolean = false,
) {
    val selected: FoundPlace? get() = (results as? SearchResults.Found)?.places?.firstOrNull { it.place.id == selectedId }
    val canRegister: Boolean get() = selected != null
}

sealed interface SearchResults {
    /** 검색어가 비어 있다. */
    data object Idle : SearchResults

    /** 시안 61:2 */
    data object Loading : SearchResults

    /** 시안 61:66. 네트워크 없음 또는 검색 요청 실패 (분석서 UC-02 1f). */
    data object Offline : SearchResults

    /** 시안 61:125 */
    data class Empty(val query: String) : SearchResults

    /** 시안 5:71 */
    data class Found(val places: List<FoundPlace>) : SearchResults
}

data class FoundPlace(
    val place: Place,
    /** 목록과 지도 핀의 번호 (1부터). */
    val number: Int,
    /** "성북구 동소문로 · 350m". 결제 위치가 없으면 거리 없이. */
    val detail: String,
)

fun summaryOf(transaction: Transaction, today: LocalDate): String =
    listOf(transaction.merchantRaw, formatWon(transaction.netAmount), formatShortDateTime(transaction.paidAt, today))
        .joinToString(" · ")

/** 검색 결과를 번호와 보조줄이 붙은 목록으로. 순서는 검색 결과 그대로(가까운 순). */
fun toFoundPlaces(places: List<Place>, paymentLocation: LatLng?): List<FoundPlace> =
    places.mapIndexed { index, place ->
        val distance = paymentLocation?.let { formatDistance(distanceMeters(it, place.latLng)) }
        FoundPlace(place, index + 1, listOfNotNull(shortAddress(place.address), distance).joinToString(" · "))
    }

/** "서울 성북구 동소문로 123" → "성북구 동소문로" (시안처럼 시·도와 번지를 뺀다). */
internal fun shortAddress(address: String?): String? {
    val tokens = address?.trim()?.split(Regex("\\s+"))?.filter { it.isNotEmpty() } ?: return null
    if (tokens.size <= 1) return tokens.firstOrNull()
    val withoutCity = tokens.drop(1)
    val withoutNumber = if (withoutCity.size > 1 && withoutCity.last().matches(Regex("[\\d-]+"))) withoutCity.dropLast(1) else withoutCity
    return withoutNumber.joinToString(" ")
}
