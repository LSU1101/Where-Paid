package com.wherepaid.app.feature.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wherepaid.app.core.designsystem.theme.WherePaidTheme

/**
 * S-02 지도 홈 (탭 1, 시작 화면). 아직 자리표시다.
 * 이 모듈에 들어갈 화면: S-02 지도 홈, S-03 핀 상세, D-01 삭제 확인, S-04 느낌표 핀 후보 고르기, S-07 온라인 결제.
 * 피그마 섹션 ②③⑥, 지도 줌·지역 색칠 규칙은 섹션 ⑩ "개발 참고".
 * 지도는 상태 바 밑까지 그리고, 위에 뜨는 카드만 상태 바 높이만큼 내린다.
 */
@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().background(WherePaidTheme.colors.mapBackground), contentAlignment = Alignment.Center) {
        Text("지도 홈 (준비 중)", style = WherePaidTheme.typography.body15Medium, color = WherePaidTheme.colors.textSecondary)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun MapScreenPreview() {
    WherePaidTheme { MapScreen() }
}
