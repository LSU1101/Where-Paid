package com.wherepaid.app.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wherepaid.app.core.designsystem.R
import com.wherepaid.app.core.designsystem.theme.WherePaidShapes
import com.wherepaid.app.core.designsystem.theme.WherePaidTheme

/** 탭 화면 머리글: "장소 확인" (높이 52). */
@Composable
fun ScreenHeader(title: String, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().height(52.dp).padding(start = 16.dp, end = 20.dp), contentAlignment = Alignment.CenterStart) {
        Text(title, style = WherePaidTheme.typography.title18, color = WherePaidTheme.colors.textPrimary)
    }
}

/** 뒤로 가기가 있는 머리글: "‹ 온라인 결제" (높이 52). */
@Composable
fun BackHeader(title: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().height(52.dp).padding(start = 12.dp, end = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable(role = Role.Button, onClick = onBack)
                .semantics { contentDescription = "뒤로" },
            contentAlignment = Alignment.Center,
        ) {
            Text("‹", style = WherePaidTheme.typography.body15Medium.copy(fontSize = 26.sp), color = WherePaidTheme.colors.textPrimary)
        }
        Text(title, style = WherePaidTheme.typography.header17, color = WherePaidTheme.colors.textPrimary)
    }
}

/** 하단 탭 하나. 아이콘은 [WherePaidIcons]. */
data class BottomTab(val label: String, @param:DrawableRes val icon: Int)

object WherePaidIcons {
    val tabMap = R.drawable.ic_tab_map
    val tabPlace = R.drawable.ic_tab_place
    val tabReport = R.drawable.ic_tab_report
}

/** 하단 탭 바 (높이 72, 위쪽 1dp 선). 시스템 내비게이션 바 높이만큼 아래 여백을 더한다. */
@Composable
fun WherePaidBottomBar(
    tabs: List<BottomTab>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = WherePaidTheme.colors
    Row(
        modifier
            .fillMaxWidth()
            .background(colors.background)
            .drawBehind { drawLine(colors.border, Offset.Zero, Offset(size.width, 0f), strokeWidth = 1.dp.toPx()) }
            .navigationBarsPadding()
            .height(72.dp)
            .padding(top = 10.dp, bottom = 14.dp),
    ) {
        tabs.forEachIndexed { index, tab ->
            val selected = index == selectedIndex
            val tint = if (selected) colors.primary else colors.textSecondary
            Column(
                Modifier
                    .weight(1f)
                    .clickable(role = Role.Tab) { onSelect(index) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(painterResource(tab.icon), contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
                Text(
                    tab.label,
                    style = if (selected) WherePaidTheme.typography.label12 else WherePaidTheme.typography.caption12Medium,
                    color = tint,
                )
            }
        }
    }
}

/** 하단 시트 위쪽 손잡이 (36×4). */
@Composable
fun SheetHandle(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(Modifier.width(36.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(WherePaidTheme.colors.border))
    }
}

/** 빈 상태·결과 없음 블록: 원형 기호, 제목, 설명, (버튼). 화면 가운데에 둔다. */
@Composable
fun StateBlock(
    symbol: String,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    symbolColor: Color = WherePaidTheme.colors.success,
    symbolContainer: Color = WherePaidTheme.colors.successContainer,
    actionText: String? = null,
    onAction: () -> Unit = {},
) {
    val type = WherePaidTheme.typography
    Column(
        modifier.width(296.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(Modifier.size(56.dp).clip(CircleShape).background(symbolContainer), contentAlignment = Alignment.Center) {
            Text(symbol, style = type.title18.copy(fontSize = 22.sp), color = symbolColor)
        }
        Spacer(Modifier.height(4.dp))
        Text(title, style = type.title18, color = WherePaidTheme.colors.textPrimary, textAlign = TextAlign.Center)
        Text(message, style = type.paragraph15, color = WherePaidTheme.colors.textTertiary, textAlign = TextAlign.Center)
        if (actionText != null) {
            Spacer(Modifier.height(8.dp))
            WherePaidButton(actionText, onAction, style = ButtonStyle.Secondary, size = ButtonSize.Medium)
        }
    }
}

/** 어두운 안내 토스트: "오프라인 · 연결되면 대기 중인 결제를 다시 찾아요". */
@Composable
fun InfoToast(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier
            .clip(WherePaidShapes.small)
            .background(WherePaidTheme.colors.toast)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(text, style = WherePaidTheme.typography.caption12Medium, color = Color.White)
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun ScaffoldingPreview() {
    WherePaidTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ScreenHeader("장소 확인")
            BackHeader("온라인 결제", onBack = {})
            SheetHandle()
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                StateBlock("✓", "장소를 기다리는 결제가 없어요", "새 결제는 자동으로 지도에 찍혀요", actionText = "지도 보기")
            }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                InfoToast("오프라인 · 연결되면 대기 중인 결제를 다시 찾아요")
            }
            WherePaidBottomBar(
                tabs = listOf(
                    BottomTab("지도", WherePaidIcons.tabMap),
                    BottomTab("장소 확인", WherePaidIcons.tabPlace),
                    BottomTab("리포트", WherePaidIcons.tabReport),
                ),
                selectedIndex = 1,
                onSelect = {},
            )
        }
    }
}
