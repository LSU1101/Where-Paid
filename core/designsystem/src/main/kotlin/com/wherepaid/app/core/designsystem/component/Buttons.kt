package com.wherepaid.app.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wherepaid.app.core.designsystem.theme.WherePaidShapes
import com.wherepaid.app.core.designsystem.theme.WherePaidTheme

/** 버튼 크기 (시안: 시작하기 54, 다이얼로그 48, 권한 카드 안 40). */
enum class ButtonSize(val height: Dp) { Large(54.dp), Medium(48.dp), Small(40.dp) }

/** 버튼 색 종류. */
enum class ButtonStyle { Primary, Secondary, Danger }

@Composable
fun WherePaidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.Primary,
    size: ButtonSize = ButtonSize.Large,
    enabled: Boolean = true,
) {
    val colors = WherePaidTheme.colors
    val type = WherePaidTheme.typography
    val (container, content) = when {
        !enabled -> colors.border to colors.textSecondary
        style == ButtonStyle.Primary -> colors.primary to colors.onPrimary
        style == ButtonStyle.Danger -> colors.danger to colors.onPrimary
        else -> colors.surfaceMuted to colors.textPrimary
    }
    val shape = when (size) {
        ButtonSize.Large -> WherePaidShapes.large
        ButtonSize.Medium -> WherePaidShapes.medium
        ButtonSize.Small -> WherePaidShapes.small
    }
    val textStyle = when (size) {
        ButtonSize.Large -> type.button16
        ButtonSize.Medium -> type.body15Strong
        ButtonSize.Small -> type.label14
    }
    Box(
        modifier = modifier
            .height(size.height)
            .clip(shape)
            .background(container)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = textStyle, color = content)
    }
}

/** 칩 버튼 글자색 (시안: 여기예요 = 경고, 찾기 = 기본, 오프라인이에요 = 흐림). */
enum class ChipTone { Default, Warning, Muted }

/** 목록 오른쪽의 작은 회색 버튼: "여기예요", "찾기", "오프라인이에요". */
@Composable
fun ChipButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: ChipTone = ChipTone.Default,
) {
    val colors = WherePaidTheme.colors
    val textColor = when (tone) {
        ChipTone.Default -> colors.textPrimary
        ChipTone.Warning -> colors.onWarningContainer
        ChipTone.Muted -> colors.textSecondary
    }
    Box(
        modifier = modifier
            .clip(WherePaidShapes.small)
            .background(colors.surfaceMuted)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 7.dp),
    ) {
        Text(text, style = WherePaidTheme.typography.label13, color = textColor)
    }
}

/** 흰 바탕 테두리 칩: 온보딩의 "허용하기", "토스 열기". */
@Composable
fun OutlinedChipButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = WherePaidTheme.colors
    Box(
        modifier = modifier
            .clip(WherePaidShapes.small)
            .background(colors.background)
            .border(BorderStroke(1.dp, colors.border), WherePaidShapes.small)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(text, style = WherePaidTheme.typography.label12, color = colors.primary)
    }
}

/** 상태 배지: "허용됨", "거절됨". 누를 수 없다. */
@Composable
fun StatusBadge(text: String, container: Color, content: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(WherePaidShapes.small)
            .background(container)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(text, style = WherePaidTheme.typography.label12, color = content)
    }
}

/** 흰 알약 이름표 (지도 핀 아래 매장 이름, 지역 라벨에 같이 쓴다). */
@Composable
fun PillLabel(
    text: String,
    modifier: Modifier = Modifier,
    container: Color = WherePaidTheme.colors.background,
    content: Color = WherePaidTheme.colors.textPrimary,
    style: TextStyle = WherePaidTheme.typography.label11,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(container)
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(text, style = style, color = content)
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonsPreview() {
    WherePaidTheme {
        val colors = WherePaidTheme.colors
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            WherePaidButton("시작하기", onClick = {}, modifier = Modifier.fillMaxWidth())
            WherePaidButton("이 장소로 등록", onClick = {}, modifier = Modifier.fillMaxWidth(), enabled = false)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WherePaidButton("취소", {}, Modifier.weight(1f), ButtonStyle.Secondary, ButtonSize.Medium)
                WherePaidButton("삭제", {}, Modifier.weight(1f), ButtonStyle.Danger, ButtonSize.Medium)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WherePaidButton("다시 허용", {}, Modifier.weight(1f), size = ButtonSize.Small)
                WherePaidButton("나중에", {}, Modifier.weight(1f), ButtonStyle.Secondary, ButtonSize.Small)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                ChipButton("여기예요", {}, tone = ChipTone.Warning)
                ChipButton("찾기", {})
                ChipButton("오프라인이에요", {}, tone = ChipTone.Muted)
                OutlinedChipButton("허용하기", {})
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusBadge("허용됨", colors.successContainer, colors.success)
                StatusBadge("거절됨", colors.warningContainer, colors.onWarningContainer)
                PillLabel("스타벅스")
                PillLabel("어디였나요?", container = colors.warningContainer, content = colors.onWarningContainer)
            }
        }
    }
}
