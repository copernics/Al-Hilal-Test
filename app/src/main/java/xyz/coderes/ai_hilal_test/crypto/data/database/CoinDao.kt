package xyz.coderes.ai_hilal_test.crypto.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinFavoriteDao {
    @Upsert
    suspend fun upsertCoin(coins: List<CoinEntity>)

    @Query("SELECT * FROM CoinEntity")
    fun getAllFavoriteCoins(): Flow<List<CoinEntity>>

    @Query("SELECT * FROM CoinEntity WHERE id = :id")
    suspend fun getCoinById(id: String): CoinEntity?

    @Query("DELETE FROM CoinEntity WHERE id in (:ids)")
    suspend fun deleteCoinById(ids: List<String>)

    @Query("UPDATE CoinEntity SET priceUsd = :priceUsd WHERE id = :id")
    suspend fun updateCoinPrice(id: String, priceUsd: String)

}