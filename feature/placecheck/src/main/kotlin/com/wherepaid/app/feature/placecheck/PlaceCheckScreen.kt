package com.wherepaid.app.feature.placecheck

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wherepaid.app.core.designsystem.component.ChipButton
import com.wherepaid.app.core.designsystem.component.ChipTone
import com.wherepaid.app.core.designsystem.component.InfoToast
import com.wherepaid.app.core.designsystem.component.LeadingTile
import com.wherepaid.app.core.designsystem.component.PaymentRow
import com.wherepaid.app.core.designsystem.component.ScreenHeader
import com.wherepaid.app.core.designsystem.component.SectionDivider
import com.wherepaid.app.core.designsystem.component.SectionHeader
import com.wherepaid.app.core.designsystem.component.StateBlock
import com.wherepaid.app.core.designsystem.component.TileKind
import com.wherepaid.app.core.designsystem.theme.WherePaidTheme
import com.wherepaid.app.core.model.PlaceStatus
import com.wherepaid.app.core.model.TransactionRepository
import com.wherepaid.app.core.model.fake.SampleData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

/**
 * S-05 장소 확인 (탭 2). 저장소와 연결된 진입점.
 *
 * @param isOnline 네트워크 연결 상태 (오프라인 안내용)
 * @param onChooseCandidate [여기예요] → S-04 후보 고르기 (결제 id)
 * @param onFindPlace [찾기] → S-06 매장 찾기 (결제 id)
 * @param onOpenMap 비어 있을 때 [지도 보기]
 */
@Composable
fun PlaceCheckRoute(
    repository: TransactionRepository,
    isOnline: Flow<Boolean>,
    onChooseCandidate: (String) -> Unit,
    onFindPlace: (String) -> Unit,
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: PlaceCheckViewModel = viewModel { PlaceCheckViewModel(repository, isOnline) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    PlaceCheckScreen(state, onChooseCandidate, onFindPlace, onOpenMap, modifier)
}

/** S-05 장소 확인. 시안 프레임: 기본 5:2, 비어 있음 61:180, 부분(검색 대기) 61:205. */
@Composable
fun PlaceCheckScreen(
    state: PlaceCheckUiState,
    onChooseCandidate: (String) -> Unit,
    onFindPlace: (String) -> Unit,
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = WherePaidTheme.colors
    val type = WherePaidTheme.typography
    Column(modifier.fillMaxSize()) {
        ScreenHeader("장소 확인")
        Box(Modifier.weight(1f).fillMaxSize()) {
            when {
                state.loading -> Unit
                state.isEmpty -> StateBlock(
                    symbol = "✓",
                    title = "장소를 기다리는 결제가 없어요",
                    message = "새 결제는 자동으로 지도에 찍혀요",
                    actionText = "지도 보기",
                    onAction = onOpenMap,
                    modifier = Modifier.align(Alignment.Center),
                )
                else -> LazyColumn(contentPadding = PaddingValues(bottom = 72.dp)) {
                    item {
                        Column(
                            Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text("${state.total}건의 결제가 장소를 기다려요", style = type.title20, color = colors.textPrimary)
                            Text("정하면 지도에 찍히고, 다음부터는 자동으로 찾아요", style = type.caption13, color = colors.textSecondary)
                        }
                    }
                    if (state.candidates.isNotEmpty()) {
                        section("후보 중에 골라 주세요", state.candidates, isCandidates = true) { item ->
                            ChipButton("여기예요", onClick = { onChooseCandidate(item.id) }, tone = ChipTone.Warning)
                        }
                    }
                    if (state.candidates.isNotEmpty() && state.manual.isNotEmpty()) {
                        item { SectionDivider() }
                    }
                    if (state.manual.isNotEmpty()) {
                        section("직접 찾아 주세요", state.manual, isCandidates = false) { item ->
                            // 매칭 대기는 연결되면 자동으로 다시 찾으므로 버튼이 없다.
                            if (item.kind == PlaceCheckKind.Unmatched) ChipButton("찾기", onClick = { onFindPlace(item.id) })
                        }
                    }
                }
            }
            if (state.showOfflineNote) {
                InfoToast(
                    "오프라인 · 연결되면 대기 중인 결제를 다시 찾아요",
                    Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp),
                )
            }
        }
    }
}

private fun LazyListScope.section(
    title: String,
    items: List<PlaceCheckItem>,
    isCandidates: Boolean,
    action: @Composable (PlaceCheckItem) -> Unit,
) {
    item(key = "header-$title") {
        SectionHeader(
            title,
            count = items.size,
            countColor = if (isCandidates) WherePaidTheme.colors.onWarningContainer else WherePaidTheme.colors.textSecondary,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
    }
    items(items, key = { it.id }) { item ->
        PaymentRow(
            title = item.merchant,
            amount = item.amount,
            subtitle = item.detail,
            subtitleColor = if (item.kind == PlaceCheckKind.PendingSearch) {
                WherePaidTheme.colors.textTertiary
            } else {
                WherePaidTheme.colors.textSecondary
            },
            leading = {
                LeadingTile(
                    when (item.kind) {
                        PlaceCheckKind.Candidates -> TileKind.Ambiguous
                        PlaceCheckKind.Unmatched -> TileKind.Unmatched
                        PlaceCheckKind.PendingSearch -> TileKind.Pending
                    },
                )
            },
            trailing = { action(item) },
            modifier = Modifier.padding(horizontal = 20.dp),
        )
    }
}

// --- 시안 상태별 미리보기 ---

private val previewMonth = YearMonth.now()
private val previewToday = previewMonth.atDay(11)

@Composable
private fun PreviewScreen(state: PlaceCheckUiState) {
    WherePaidTheme { PlaceCheckScreen(state, {}, {}, {}) }
}

@Preview(name = "기본 (5:2)", showBackground = true, widthDp = 360, heightDp = 728)
@Composable
private fun PlaceCheckDefaultPreview() {
    val state = remember {
        val sample = SampleData.transactions(previewMonth).filter { it.status != PlaceStatus.PENDING_SEARCH }
        toPlaceCheckUiState(sample, isOnline = true, today = previewToday)
    }
    PreviewScreen(state)
}

@Preview(name = "비어 있음 (61:180)", showBackground = true, widthDp = 360, heightDp = 728)
@Composable
private fun PlaceCheckEmptyPreview() {
    PreviewScreen(toPlaceCheckUiState(emptyList(), isOnline = true, today = LocalDate.now()))
}

@Preview(name = "부분: 검색 대기 + 오프라인 (61:205)", showBackground = true, widthDp = 360, heightDp = 728)
@Composable
private fun PlaceCheckOfflinePreview() {
    val state = remember { toPlaceCheckUiState(SampleData.transactions(previewMonth), isOnline = false, today = previewToday) }
    PreviewScreen(state)
}
