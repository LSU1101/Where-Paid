package com.wherepaid.app.core.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TransactionDao {
    @Transaction
    @Query("SELECT * FROM transactions WHERE paidAt >= :from AND paidAt < :until ORDER BY paidAt DESC")
    fun observeBetween(from: Long, until: Long): Flow<List<TransactionWithRelations>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE status IN (:statuses) ORDER BY paidAt DESC")
    fun observeByStatus(statuses: List<String>): Flow<List<TransactionWithRelations>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE status IN (:statuses) ORDER BY paidAt DESC")
    suspend fun getByStatus(statuses: List<String>): List<TransactionWithRelations>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    fun observe(id: String): Flow<TransactionWithRelations?>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun get(id: String): TransactionWithRelations?

    @Transaction
    @Query("SELECT * FROM transactions WHERE merchantRaw = :merchant")
    suspend fun getByMerchant(merchant: String): List<TransactionWithRelations>

    @Query("SELECT EXISTS(SELECT 1 FROM transactions WHERE dedupeKey = :dedupeKey)")
    suspend fun existsDedupeKey(dedupeKey: String): Boolean

    @Upsert
    suspend fun upsert(entity: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun delete(id: String)

    @Insert
    suspend fun insertCancellation(entity: CancellationEntity)

    @Query("DELETE FROM candidates WHERE transactionId = :transactionId")
    suspend fun clearCandidates(transactionId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCandidates(entities: List<CandidateEntity>)
}

@Dao
internal interface PlaceDao {
    @Query("SELECT * FROM places WHERE id = :id")
    suspend fun get(id: String): PlaceEntity?

    @Upsert
    suspend fun upsert(entities: List<PlaceEntity>)

    @Query("SELECT p.* FROM learned_places l JOIN places p ON p.id = l.placeId WHERE l.merchant = :merchant")
    suspend fun learnedPlace(merchant: String): PlaceEntity?

    @Upsert
    suspend fun upsertLearned(entity: LearnedPlaceEntity)

    @Query("SELECT designation FROM designations WHERE merchant = :merchant")
    suspend fun designation(merchant: String): String?

    @Upsert
    suspend fun upsertDesignation(entity: DesignationEntity)
}

/**
 * 첫 릴리스 전까지는 version 1을 그대로 고쳐 쓴다 (기존 데이터 없음).
 * 첫 릴리스 뒤에 구조를 바꾸면 version을 올리고 마이그레이션을 추가한다.
 */
@Database(
    entities = [
        TransactionEntity::class,
        PlaceEntity::class,
        CancellationEntity::class,
        CandidateEntity::class,
        LearnedPlaceEntity::class,
        DesignationEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
internal abstract class WherePaidDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun placeDao(): PlaceDao

    companion object {
        fun create(context: Context): WherePaidDatabase =
            Room.databaseBuilder(context.applicationContext, WherePaidDatabase::class.java, "wherepaid.db")
                .build()
    }
}
