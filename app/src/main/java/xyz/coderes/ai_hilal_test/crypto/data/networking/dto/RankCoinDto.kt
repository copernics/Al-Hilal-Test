package xyz.coderes.ai_hilal_test.crypto.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class RankCoinDto(
    val id: String,
    val symbol: String,
    val rateUsd: Double,
)