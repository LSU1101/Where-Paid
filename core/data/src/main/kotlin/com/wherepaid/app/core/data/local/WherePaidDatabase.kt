package com.wherepaid.app.core.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY paidAt DESC")
    fun observeAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun get(id: String): TransactionEntity?

    @Upsert
    suspend fun upsert(entity: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun delete(id: String)
}

@Dao
internal interface PlaceDao {
    @Query("SELECT * FROM places WHERE id = :id")
    suspend fun get(id: String): PlaceEntity?

    @Upsert
    suspend fun upsert(entity: PlaceEntity)
}

/** 구조를 바꾸면 version을 올리고 마이그레이션을 추가한다. */
@Database(
    entities = [TransactionEntity::class, PlaceEntity::class],
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
