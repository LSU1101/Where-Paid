package com.wherepaid.app.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wherepaid.app.core.designsystem.component.WherePaidButton
import com.wherepaid.app.core.designsystem.theme.WherePaidTheme

/**
 * S-01 온보딩 (첫 실행에만). 아직 자리표시다: 제목과 [시작하기]만 있다.
 * 피그마 섹션 ①: 권한 4가지(알림 접근 → 위치 항상 허용 → 알림 표시 → 토스 카드 알림)와 위치 거절 상태.
 *
 * @param onStart [시작하기]. 호출한 쪽이 "온보딩 끝남"을 저장하고 지도 홈으로 보낸다.
 */
@Composable
fun OnboardingScreen(onStart: () -> Unit, modifier: Modifier = Modifier) {
    val colors = WherePaidTheme.colors
    val type = WherePaidTheme.typography
    Column(modifier.fillMaxSize().systemBarsPadding()) {
        Column(
            Modifier.padding(start = 24.dp, end = 24.dp, top = 56.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("결제하면 알아서\n지도에 찍어드려요", style = type.headline26, color = colors.textPrimary)
            Text("네 가지만 켜 두면 끝이에요", style = type.body15, color = colors.textSecondary)
        }
        Spacer(Modifier.weight(1f))
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("결제 내역과 위치는 내 폰에만 저장돼요.", style = type.caption12, color = colors.textSecondary)
            WherePaidButton("시작하기", onClick = onStart, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun OnboardingScreenPreview() {
    WherePaidTheme { OnboardingScreen(onStart = {}) }
}
