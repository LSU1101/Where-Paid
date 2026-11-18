package com.wherepaid.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wherepaid.app.core.designsystem.theme.WherePaidShapes
import com.wherepaid.app.core.designsystem.theme.WherePaidTheme

/**
 * 확인 다이얼로그 (D-01 삭제 확인, D-02 등록 전 나가기).
 * 제목, 회색 요약 칸, 설명, 버튼 두 개. 오른쪽 버튼이 확인.
 */
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    summary: String? = null,
    confirmStyle: ButtonStyle = ButtonStyle.Primary,
) {
    Dialog(onDismissRequest = onDismiss) {
        ConfirmDialogContent(title, message, confirmText, dismissText, onConfirm, onDismiss, summary, confirmStyle)
    }
}

@Composable
private fun ConfirmDialogContent(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    summary: String?,
    confirmStyle: ButtonStyle,
) {
    val colors = WherePaidTheme.colors
    val type = WherePaidTheme.typography
    Column(
        Modifier
            .width(312.dp)
            .shadow(32.dp, WherePaidShapes.dialog)
            .clip(WherePaidShapes.dialog)
            .background(colors.background)
            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(title, style = type.title18, color = colors.textPrimary)
        if (summary != null) {
            Text(
                summary,
                style = type.caption13Medium,
                color = colors.textTertiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(WherePaidShapes.small)
                    .background(colors.surfaceMuted)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            )
        }
        Text(message, style = type.paragraph15, color = colors.textTertiary)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WherePaidButton(dismissText, onDismiss, Modifier.weight(1f), ButtonStyle.Secondary, ButtonSize.Medium)
            WherePaidButton(confirmText, onConfirm, Modifier.weight(1f), confirmStyle, ButtonSize.Medium)
        }
    }
}

@Preview
@Composable
private fun ConfirmDialogPreview() {
    WherePaidTheme {
        ConfirmDialogContent(
            title = "이 결제를 삭제할까요?",
            message = "지도와 총액에서 빠져요\n고른 매장은 그대로 기억해요",
            confirmText = "삭제",
            dismissText = "취소",
            onConfirm = {},
            onDismiss = {},
            summary = "스타벅스 성수역점 · 6,800원 · 10.11 14:32",
            confirmStyle = ButtonStyle.Danger,
        )
    }
}
