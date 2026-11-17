package com.wherepaid.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wherepaid.app.core.designsystem.theme.WherePaidShapes
import com.wherepaid.app.core.designsystem.theme.WherePaidTheme

/** 목록 앞 40dp 아이콘 칸의 종류. 기호는 시안 그대로 글자로 그린다. */
enum class TileKind(val symbol: String) {
    /** 느낌표 핀 (후보 중에 골라 주세요) */
    Ambiguous("!"),

    /** 매칭실패함 (직접 찾아 주세요) */
    Unmatched("?"),

    /** 매칭 대기 (검색 대기) */
    Pending("⏱"),

    /** 온라인 결제 */
    Online("◎"),
}

@Composable
fun LeadingTile(kind: TileKind, modifier: Modifier = Modifier) {
    val colors = WherePaidTheme.colors
    val (container, content) = when (kind) {
        TileKind.Ambiguous -> colors.warningContainer to colors.onWarningContainer
        TileKind.Unmatched, TileKind.Pending -> colors.surfaceMuted to colors.textSecondary
        TileKind.Online -> colors.onlineContainer to colors.online
    }
    Box(
        modifier = modifier.size(40.dp).clip(WherePaidShapes.tile).background(container),
        contentAlignment = Alignment.Center,
    ) {
        Text(kind.symbol, style = WherePaidTheme.typography.title18.copy(fontSize = 16.8.sp), color = content)
    }
}

/**
 * 결제 한 줄: [앞 아이콘] 가맹점 금액 / 보조줄 [오른쪽 버튼].
 * 장소 확인(S-05), 온라인 결제(S-07) 목록에 쓴다.
 */
@Composable
fun PaymentRow(
    title: String,
    amount: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    subtitleColor: Color = WherePaidTheme.colors.textSecondary,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val colors = WherePaidTheme.colors
    val type = WherePaidTheme.typography
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leading?.invoke()
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    title, style = type.body15Strong, color = colors.textPrimary,
                    maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false),
                )
                Text(amount, style = type.body15, color = colors.textSecondary, maxLines = 1)
            }
            Text(subtitle, style = type.caption12, color = subtitleColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        trailing?.invoke()
    }
}

/** 목록 구역 제목과 개수: "후보 중에 골라 주세요 2". */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    count: Int? = null,
    countColor: Color = WherePaidTheme.colors.textSecondary,
) {
    Row(
        modifier = modifier.padding(top = 12.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(title, style = WherePaidTheme.typography.label14, color = WherePaidTheme.colors.textPrimary)
        if (count != null) Text(count.toString(), style = WherePaidTheme.typography.label14, color = countColor)
    }
}

/** 구역 사이 8dp 회색 띠. */
@Composable
fun SectionDivider(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().height(8.dp).background(WherePaidTheme.colors.surfaceMuted))
}

/** 1dp 선. */
@Composable
fun HairlineDivider(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().height(1.dp).background(WherePaidTheme.colors.border))
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun ListsPreview() {
    WherePaidTheme {
        Column {
            Column(Modifier.padding(horizontal = 20.dp)) {
                SectionHeader("후보 중에 골라 주세요", count = 2, countColor = WherePaidTheme.colors.onWarningContainer)
                PaymentRow(
                    "GS25", "3,400원", "오늘 08:41 · 근처 후보 3곳",
                    leading = { LeadingTile(TileKind.Ambiguous) },
                    trailing = { ChipButton("여기예요", {}, tone = ChipTone.Warning) },
                )
            }
            SectionDivider()
            Column(Modifier.padding(horizontal = 20.dp)) {
                SectionHeader("직접 찾아 주세요", count = 2)
                PaymentRow(
                    "씨유 영등포아크로타", "3,400원", "검색 대기 · 인터넷이 연결되면 자동으로 다시 찾아요",
                    leading = { LeadingTile(TileKind.Pending) },
                    subtitleColor = WherePaidTheme.colors.textTertiary,
                )
                PaymentRow(
                    "(주)케이에프씨코리", "9,800원", "10.09 19:20 · 위치를 못 받았어요",
                    leading = { LeadingTile(TileKind.Unmatched) },
                    trailing = { ChipButton("찾기", {}) },
                )
                PaymentRow(
                    "쿠팡", "32,400원", "10.10 22:14 · 하나체크",
                    leading = { LeadingTile(TileKind.Online) },
                    trailing = { ChipButton("오프라인이에요", {}, tone = ChipTone.Muted) },
                )
            }
        }
    }
}
