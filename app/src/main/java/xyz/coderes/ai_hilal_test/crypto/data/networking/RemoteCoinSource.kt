package xyz.coderes.ai_hilal_test.crypto.data.networking

import xyz.coderes.ai_hilal_test.crypto.data.networking.dto.CoinsResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import xyz.coderes.ai_hilal_test.core.data.constructUrl
import xyz.coderes.ai_hilal_test.core.data.safeCall
import xyz.coderes.ai_hilal_test.core.domain.NetworkError
import xyz.coderes.ai_hilal_test.crypto.domain.Coin
import xyz.coderes.ai_hilal_test.core.domain.Result
import xyz.coderes.ai_hilal_test.core.domain.map
import xyz.coderes.ai_hilal_test.crypto.data.mapper.toCoin
import xyz.coderes.ai_hilal_test.crypto.data.mapper.toCoinRate
import xyz.coderes.ai_hilal_test.crypto.data.networking.dto.RankCoinsResponseDto
import xyz.coderes.ai_hilal_test.crypto.domain.CoinRate


class RemoteCoinSource(
    private val httpClient: HttpClient,
) : RemoteCoinDataSource {

    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return safeCall<CoinsResponseDto> {
            httpClient.get(
                urlString = constructUrl("/assets")
            )
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }

    override suspend fun getRank(coinIds: List<String>): Result<List<CoinRate>, NetworkError> {
        return safeCall<RankCoinsResponseDto> {
            httpClient.get(
                urlString = constructUrl("/rates")
            ) {
                url {
                    parameters.append("ids", coinIds.joinToString(","))
                }
            }
        }.map { response ->
            response.data.map { it.toCoinRate() }
        }
    }
}

interface RemoteCoinDataSource {
    suspend fun getCoins(): Result<List<Coin>, NetworkError>

    suspend fun getRank(coinIds: List<String>): Result<List<CoinRate>, NetworkError>
}