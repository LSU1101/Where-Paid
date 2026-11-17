package com.wherepaid.app.feature.map.candidate

import com.wherepaid.app.core.designsystem.format.formatDistance
import com.wherepaid.app.core.designsystem.format.formatShortDateTime
import com.wherepaid.app.core.designsystem.format.formatWon
import com.wherepaid.app.core.model.Place
import com.wherepaid.app.core.model.PlaceStatus
import com.wherepaid.app.core.model.Transaction
import com.wherepaid.app.core.model.distanceMeters
import java.time.LocalDate

/** S-04 느낌표 핀 후보 고르기 시트가 그리는 것. */
sealed interface CandidatePickerUiState {
    data object Loading : CandidatePickerUiState

    /** 결제가 지워졌거나 이미 장소가 정해져 더 고를 것이 없다. 시트를 닫는다. */
    data object Done : CandidatePickerUiState

    data class Picking(
        /** "GS25 · 3,400원 · 오늘 08:41" */
        val summary: String,
        val candidates: List<CandidateItem>,
    ) : CandidatePickerUiState
}

data class CandidateItem(
    val place: Place,
    /** "40m". 결제 위치가 없으면 null. */
    val distance: String?,
)

/** 결제 하나를 시트 상태로 바꾼다. 후보는 결제 위치에서 가까운 순 (분석서 UC-02). */
fun toCandidatePickerUiState(transaction: Transaction?, today: LocalDate): CandidatePickerUiState {
    if (transaction == null || transaction.status != PlaceStatus.AMBIGUOUS) return CandidatePickerUiState.Done
    val location = transaction.location
    val candidates = transaction.candidates
        .map { place -> place to location?.let { distanceMeters(it, place.latLng) } }
        .sortedBy { (_, meters) -> meters ?: 0.0 }
        .map { (place, meters) -> CandidateItem(place, meters?.let(::formatDistance)) }
    val summary = listOf(
        transaction.merchantRaw,
        formatWon(transaction.netAmount),
        formatShortDateTime(transaction.paidAt, today),
    ).joinToString(" · ")
    return CandidatePickerUiState.Picking(summary, candidates)
}
