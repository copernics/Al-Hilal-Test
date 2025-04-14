package xyz.coderes.ai_hilal_test.crypto.presentation

import androidx.core.util.TimeUtils
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

class FavouriteCoinListViewModel(
    private val coinDataSource: CoinRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListState())
    val state = _state

    init {
        viewModelScope.launch {
            coinDataSource
                .getFavoriteCoins().collect { coins ->
                    _state.update {
                        it.copy(
                            coins = coins.map { it.toCoinUi() }
                        )
                    }
                }
        }
    }

    fun onAction(action: FavouriteCoinListAction) {
        when (action) {
            is RemoveFromFavourite -> {
                deleteCoin(
                    action.ids
                )
            }
            is RefreshFavourite -> {
                refreshCoins()
            }
        }
    }

    private fun refreshCoins() {
        state.value.coins.map { it.id }.let { coinIds ->
            if (coinIds.isNotEmpty())
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    coinDataSource
                        .updateRankCoins(coinIds)
                        .onSuccess { coins ->
                            coins.forEach {
                                coinDataSource.updateCoinPrice(it.id, it.priceUsd.toString())
                            }
                            _state.update {
                                it.copy(
                                    lastUpdate = System.currentTimeMillis(),
                                )
                            }
                        }
                        .onError { error ->
                            _state.update { it.copy(isLoading = false) }
                        }
                }
        }
    }

    private fun deleteCoin(ids: List<String>) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            coinDataSource.deleteFavorite(ids)
                .onSuccess { coins ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }
}