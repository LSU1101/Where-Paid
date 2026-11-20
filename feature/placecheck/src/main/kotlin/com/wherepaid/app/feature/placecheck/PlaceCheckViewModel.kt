package com.wherepaid.app.feature.placecheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wherepaid.app.core.model.KOREA_ZONE
import com.wherepaid.app.core.model.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

/** S-05 장소 확인. 저장소의 장소 확인 대상과 네트워크 상태를 화면 상태로 합친다. 기기 DB만 쓰므로 오프라인에서도 동작한다. */
class PlaceCheckViewModel(
    repository: TransactionRepository,
    isOnline: Flow<Boolean>,
    today: () -> LocalDate = { LocalDate.now(KOREA_ZONE) },
) : ViewModel() {
    val state: StateFlow<PlaceCheckUiState> =
        combine(repository.observeNeedsPlace(), isOnline) { transactions, online ->
            toPlaceCheckUiState(transactions, online, today())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PlaceCheckUiState())
}
