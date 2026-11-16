package com.wherepaid.app.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 설정. 아직 빈 화면이다.
 * TODO(③): 권한 상태와 설정 화면 이동(알림 접근 → 위치 → 백그라운드 위치 → 배터리 최적화 제외), 계정·동기화.
 *
 * @param dataSourceLabel 지금 어떤 저장소를 쓰고 있는지 (개발 중 확인용).
 */
@Composable
fun SettingsScreen(dataSourceLabel: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("설정", style = MaterialTheme.typography.headlineMedium)
        Text("데이터: $dataSourceLabel", style = MaterialTheme.typography.bodyLarge)
        Text("아직 빈 화면입니다", style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(dataSourceLabel = "가짜 데이터")
}
