package com.wherepaid.app.feature.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wherepaid.app.core.model.TransactionRepository
import com.wherepaid.app.core.model.fake.FakeTransactionRepository

/**
 * 지도 (홈). 아직 빈 화면이다.
 * TODO(③): 카카오맵 SDK로 결제 위치를 표시한다. 네이티브 앱 키는 매니페스트 meta-data로 들어온다.
 */
@Composable
fun MapScreen(repository: TransactionRepository, modifier: Modifier = Modifier) {
    val transactions by repository.observeAll().collectAsStateWithLifecycle(initialValue = emptyList())
    val withLocation = transactions.count { it.location != null }

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("지도", style = MaterialTheme.typography.headlineMedium)
        Text("위치가 있는 거래 ${withLocation}건", style = MaterialTheme.typography.bodyLarge)
        Text("아직 빈 화면입니다", style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun MapScreenPreview() {
    MapScreen(repository = remember { FakeTransactionRepository() })
}
