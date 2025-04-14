package xyz.coderes.ai_hilal_test.crypto.data.mapper

import xyz.coderes.ai_hilal_test.crypto.data.database.CoinEntity
import xyz.coderes.ai_hilal_test.crypto.data.networking.dto.CoinDto
import xyz.coderes.ai_hilal_test.crypto.data.networking.dto.RankCoinDto
import xyz.coderes.ai_hilal_test.crypto.domain.Coin
import xyz.coderes.ai_hilal_test.crypto.domain.CoinRate
import xyz.coderes.ai_hilal_test.crypto.presentation.model.CoinUi

fun CoinDto.toCoin(): Coin {
    return Coin(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr
    )
}

fun Coin.toCoinEntity(): CoinEntity = CoinEntity(
    id = id,
    rank = rank,
    name = name,
    symbol = symbol,
    priceUsd = priceUsd,
    changePercent24Hr = changePercent24Hr
)

fun CoinEntity.toCoin(): Coin =
    Coin(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr
    )

fun CoinUi.toCoin(): Coin =
    Coin(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd.value,
        changePercent24Hr = changePercent24Hr.value
)

fun RankCoinDto.toCoinRate(): CoinRate =
    CoinRate(
        id = id,
        symbol = symbol,
        priceUsd = rateUsd
    )