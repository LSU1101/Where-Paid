package com.wherepaid.app.feature.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wherepaid.app.core.model.Transaction
import com.wherepaid.app.core.model.TransactionRepository
import com.wherepaid.app.core.model.fake.FakeTransactionRepository
import java.text.NumberFormat
import java.time.YearMonth
import java.util.Locale

/**
 * 결제 내역. 저장소 연결을 확인하는 최소한의 목록만 있다.
 * TODO(③): 날짜별 묶음, 상세·수정 화면, 온라인 결제와 취소·실패 표시 방식 (인계 문서 §7).
 */
@Composable
fun HistoryScreen(repository: TransactionRepository, modifier: Modifier = Modifier) {
    val transactions by remember(repository) { repository.observeMonth(YearMonth.now()) }.collectAsStateWithLifecycle(initialValue = emptyList())

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "내역 ${transactions.size}건",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        )
        if (transactions.isEmpty()) {
            Text(
                text = "아직 기록된 결제가 없습니다",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            items(transactions, key = { it.id }) { transaction ->
                TransactionRow(transaction)
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun TransactionRow(transaction: Transaction) {
    val label = NumberFormat.getNumberInstance(Locale.KOREA).format(transaction.netAmount) + "원"
    ListItem(
        headlineContent = { Text(transaction.place?.name ?: transaction.merchantRaw) },
        supportingContent = {
            Text(listOfNotNull(transaction.cardName, transaction.status.name).joinToString(" · "))
        },
        trailingContent = { Text(label, style = MaterialTheme.typography.bodyLarge) },
    )
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    HistoryScreen(repository = remember { FakeTransactionRepository() })
}
