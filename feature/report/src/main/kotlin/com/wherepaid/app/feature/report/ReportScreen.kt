package com.wherepaid.app.feature.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wherepaid.app.core.designsystem.component.ScreenHeader
import com.wherepaid.app.core.designsystem.theme.WherePaidTheme

/**
 * S-08 월간 리포트 (탭 3). 아직 자리표시다.
 * 이 모듈에 들어갈 화면: S-08 월간 리포트, S-09 스타일 카드 공유. 피그마 섹션 ⑦⑧.
 */
@Composable
fun ReportScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        ScreenHeader("리포트")
        Box(Modifier.weight(1f).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("준비 중", style = WherePaidTheme.typography.body15Medium, color = WherePaidTheme.colors.textSecondary)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun ReportScreenPreview() {
    WherePaidTheme { ReportScreen() }
}
