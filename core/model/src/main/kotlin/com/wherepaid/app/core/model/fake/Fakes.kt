package com.wherepaid.app.core.model.fake

import com.wherepaid.app.core.model.LatLng
import com.wherepaid.app.core.model.LocationProvider
import com.wherepaid.app.core.model.LocationResult
import com.wherepaid.app.core.model.ParsedPayment
import com.wherepaid.app.core.model.PaymentSource
import com.wherepaid.app.core.model.Place
import com.wherepaid.app.core.model.PlaceResolver
import com.wherepaid.app.core.model.Transaction
import com.wherepaid.app.core.model.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * 가짜 구현. 실제 구현이 없어도 화면과 테스트를 만들 수 있게 한다.
 * 데이터는 [SampleData]에 있고 모두 메모리에만 있다 (앱을 다시 켜면 초기화).
 */

/** [emit]으로 결제를 직접 흘려보낸다. */
class FakePaymentSource : PaymentSource {
    private val flow = MutableSharedFlow<ParsedPayment>(extraBufferCapacity = 16)
    override val payments: Flow<ParsedPayment> = flow

    suspend fun emit(payment: ParsedPayment) = flow.emit(payment)
}

/** 항상 [result]를 돌려준다. */
class FakeLocationProvider(
    var result: LocationResult = LocationResult.Success(SampleData.CITY_HALL, accuracyM = 12.5f),
) : LocationProvider {
    override suspend fun capture(): LocationResult = result
}

/** 가맹점명에 장소 이름이 들어 있으면 그 장소를 돌려준다. */
class FakePlaceResolver(
    private val places: List<Place> = SampleData.places,
) : PlaceResolver {
    override suspend fun resolve(merchant: String, near: LatLng?): Place? =
        places.firstOrNull { it.name in merchant || merchant in it.name }
}

class FakeTransactionRepository(
    transactions: List<Transaction> = SampleData.transactions,
    places: List<Place> = SampleData.places,
) : TransactionRepository {
    private val transactions = MutableStateFlow(transactions.associateBy { it.id })
    private val places = MutableStateFlow(places.associateBy { it.id })

    override fun observeAll(): Flow<List<Transaction>> =
        transactions.map { all -> all.values.sortedByDescending { it.paidAt } }

    override suspend fun get(id: String): Transaction? = transactions.value[id]

    override suspend fun upsert(transaction: Transaction) {
        transactions.update { it + (transaction.id to transaction) }
    }

    override suspend fun delete(id: String) {
        transactions.update { it - id }
    }

    override suspend fun getPlace(id: String): Place? = places.value[id]

    override suspend fun upsertPlace(place: Place) {
        places.update { it + (place.id to place) }
    }

    override suspend fun sync() = Unit
}
