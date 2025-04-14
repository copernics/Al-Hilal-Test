package xyz.coderes.ai_hilal_test.crypto.presentation

import androidx.compose.runtime.Immutable
import xyz.coderes.ai_hilal_test.crypto.presentation.model.CoinUi

@Immutable
data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<CoinUi> = emptyList(),
    val selected: List<String> = emptyList(),
    val lastUpdate: Long? = null,
)
