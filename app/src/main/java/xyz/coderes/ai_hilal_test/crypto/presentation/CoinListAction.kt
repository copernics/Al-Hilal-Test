package xyz.coderes.ai_hilal_test.crypto.presentation

sealed interface CoinListAction
class AddToFavourite(val ids: List<String>) : CoinListAction
sealed interface FavouriteCoinListAction
class RemoveFromFavourite(val ids: List<String>) : FavouriteCoinListAction
class RefreshFavourite() : FavouriteCoinListAction
