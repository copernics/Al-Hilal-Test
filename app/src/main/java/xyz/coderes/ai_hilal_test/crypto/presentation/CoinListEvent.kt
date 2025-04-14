package xyz.coderes.ai_hilal_test.crypto.presentation

import xyz.coderes.ai_hilal_test.core.domain.Error


sealed interface CoinListEvent {
    data class Error(val error: xyz.coderes.ai_hilal_test.core.domain.Error): CoinListEvent
}