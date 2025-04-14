package xyz.coderes.ai_hilal_test.crypto.domain

import kotlinx.coroutines.flow.Flow
import xyz.coderes.ai_hilal_test.core.domain.LocalError
import xyz.coderes.ai_hilal_test.core.domain.NetworkError
import xyz.coderes.ai_hilal_test.core.domain.Result


interface CoinRepository {
    suspend fun getCoins(): Result<List<Coin>, NetworkError>
    suspend fun updateRankCoins(coinIds: List<String>): Result<List<CoinRate>, NetworkError>
    fun getFavoriteCoins(): Flow<List<Coin>>
    fun isCoinFavorite(coinId: String): Flow<Boolean>
    suspend fun markAsFavorite(coins: List<Coin>): Result<Unit, LocalError>
    suspend fun deleteFavorite(coinIds: List<String>): Result<Unit, LocalError>
    suspend fun updateCoinPrice(coinId: String, priceUsd: String): Result<Unit, LocalError>
}