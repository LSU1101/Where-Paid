package com.wherepaid.app.feature.placecheck

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
 * S-05 장소 확인 (탭 2). 아직 자리표시다.
 * 이 모듈에 들어갈 화면: S-05 장소 확인, S-06 매장 찾기, D-02 등록 전 나가기 확인. 피그마 섹션 ④⑤.
 */
@Composable
fun PlaceCheckScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        ScreenHeader("장소 확인")
        Box(Modifier.weight(1f).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("준비 중", style = WherePaidTheme.typography.body15Medium, color = WherePaidTheme.colors.textSecondary)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun PlaceCheckScreenPreview() {
    WherePaidTheme { PlaceCheckScreen() }
}
