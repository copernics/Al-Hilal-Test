package xyz.coderes.ai_hilal_test.crypto.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.coderes.ai_hilal_test.core.domain.onError
import xyz.coderes.ai_hilal_test.core.domain.onSuccess
import xyz.coderes.ai_hilal_test.crypto.data.mapper.toCoin
import xyz.coderes.ai_hilal_test.crypto.domain.Coin
import xyz.coderes.ai_hilal_test.crypto.domain.CoinRepository
import xyz.coderes.ai_hilal_test.crypto.presentation.model.toCoinUi
import kotlin.collections.map

class CoinListViewModel(
    private val coinDataSource: CoinRepository,
) : ViewModel() {
    init {
        viewModelScope.launch {
            coinDataSource
                .getFavoriteCoins().collect { coins ->
                    _state.update {
                        it.copy(
                            selected = coins.map { it.id }
                        )
                    }
                }
        }
    }

    private val _state = MutableStateFlow(CoinListState())
    val state = _state
        .onStart { loadCoins() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CoinListState()
        )


    private val _events = Channel<CoinListEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CoinListAction) {
        when (action) {
            is AddToFavourite -> {
                insertCoin(state.value.coins.filter { it.id in action.ids }.map { it.toCoin() })
            }
        }
    }

    private fun insertCoin(coins: List<Coin>) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            coinDataSource.markAsFavorite(coins)
                .onSuccess { coins ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(CoinListEvent.Error(error))
                }
        }
    }

    private fun loadCoins() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            coinDataSource
                .getCoins()
                .onSuccess { coins ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            coins = coins.map { it.toCoinUi() }
                        )
                    }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(CoinListEvent.Error(error))
                }
        }
    }
}