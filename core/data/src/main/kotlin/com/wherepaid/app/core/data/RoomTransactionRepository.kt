package com.wherepaid.app.core.data

import android.content.Context
import com.wherepaid.app.core.data.local.WherePaidDatabase
import com.wherepaid.app.core.data.local.toEntity
import com.wherepaid.app.core.data.local.toModel
import com.wherepaid.app.core.model.Place
import com.wherepaid.app.core.model.Transaction
import com.wherepaid.app.core.model.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 기기 DB(Room)에 저장하는 [TransactionRepository]. 이 모듈이 밖으로 내놓는 것은 이 함수 하나다.
 * 프로세스에서 한 번만 호출한다 (:app의 AppContainer).
 */
fun createTransactionRepository(context: Context): TransactionRepository =
    RoomTransactionRepository(WherePaidDatabase.create(context))

internal class RoomTransactionRepository(db: WherePaidDatabase) : TransactionRepository {
    private val transactions = db.transactionDao()
    private val places = db.placeDao()

    override fun observeAll(): Flow<List<Transaction>> =
        transactions.observeAll().map { list -> list.map { it.toModel() } }

    override suspend fun get(id: String): Transaction? = transactions.get(id)?.toModel()

    override suspend fun upsert(transaction: Transaction) = transactions.upsert(transaction.toEntity())

    override suspend fun delete(id: String) = transactions.delete(id)

    override suspend fun getPlace(id: String): Place? = places.get(id)?.toModel()

    override suspend fun upsertPlace(place: Place) = places.upsert(place.toEntity())

    override suspend fun sync() {
        // TODO(④): 원격 저장 방식(Firebase / 구글 드라이브)이 정해지면 구현한다. 인계 문서 §6, §7.
        //  기기 DB가 원본이고 원격은 동기화 대상이다.
    }
}
