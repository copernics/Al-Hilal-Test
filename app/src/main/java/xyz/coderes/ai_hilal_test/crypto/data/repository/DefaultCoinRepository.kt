package xyz.coderes.ai_hilal_test.crypto.data.repository

import androidx.sqlite.SQLiteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.coderes.ai_hilal_test.core.domain.LocalError
import xyz.coderes.ai_hilal_test.core.domain.NetworkError
import xyz.coderes.ai_hilal_test.core.domain.Result
import xyz.coderes.ai_hilal_test.crypto.data.database.CoinFavoriteDao
import xyz.coderes.ai_hilal_test.crypto.data.mapper.toCoin
import xyz.coderes.ai_hilal_test.crypto.data.mapper.toCoinEntity
import xyz.coderes.ai_hilal_test.crypto.data.networking.RemoteCoinSource
import xyz.coderes.ai_hilal_test.crypto.domain.Coin
import xyz.coderes.ai_hilal_test.crypto.domain.CoinRate
import xyz.coderes.ai_hilal_test.crypto.domain.CoinRepository

class DefaultCoinRepository(
    private val remoteCoinDataSource: RemoteCoinSource,
    private val favoriteCoinDao: CoinFavoriteDao,
) : CoinRepository {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return remoteCoinDataSource.getCoins()
    }

    override suspend fun updateRankCoins(coinIds: List<String>): Result<List<CoinRate>, NetworkError> {
        return remoteCoinDataSource.getRank(coinIds)
    }

    override fun getFavoriteCoins(): Flow<List<Coin>> {
       return  favoriteCoinDao.getAllFavoriteCoins().map { it.map { entity -> entity.toCoin() } }
    }

    override fun isCoinFavorite(coinId: String): Flow<Boolean> {
        return favoriteCoinDao.getAllFavoriteCoins().map {
            it.any { entuty -> entuty.id == coinId }
        }
    }

    override suspend fun markAsFavorite(coins: List<Coin>): Result<Unit, LocalError> =
        safeDbCall {
            println("add to favorite: ${coins.joinToString { it.name }}")
            favoriteCoinDao.upsertCoin(coins.map { it.toCoinEntity()})
        }


    override suspend fun deleteFavorite(coinIds: List<String>): Result<Unit, LocalError> =
        safeDbCall { favoriteCoinDao.deleteCoinById(coinIds) }

    override suspend fun updateCoinPrice(coinId: String, priceUsd: String): Result<Unit, LocalError> {
        return safeDbCall {
            favoriteCoinDao.updateCoinPrice(coinId, priceUsd)
        }
    }

}

private inline fun <T> safeDbCall(action: () -> T): Result<T, LocalError> {
    return try {
        Result.Success(action())
    } catch (e: SQLiteException) {
        Result.Error(LocalError.SQL_ERROR)
    }
}