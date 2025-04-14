package xyz.coderes.ai_hilal_test.crypto.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class RankCoinsResponseDto(
    val data: List<RankCoinDto>,
    val timestamp: Long
)