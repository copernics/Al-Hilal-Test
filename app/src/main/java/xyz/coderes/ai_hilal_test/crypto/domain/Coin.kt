package xyz.coderes.ai_hilal_test.crypto.domain


data class Coin(
    val id: String,
    val name: String,
    val rank: Int,
    val symbol: String,
    val priceUsd: Double,
    val changePercent24Hr: Double,
)

