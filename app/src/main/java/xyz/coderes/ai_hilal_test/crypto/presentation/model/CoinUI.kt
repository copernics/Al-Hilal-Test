package xyz.coderes.ai_hilal_test.crypto.presentation.model

import android.icu.text.NumberFormat
import androidx.annotation.DrawableRes
import xyz.coderes.ai_hilal_test.core.presentation.getDrawableIdForCoin
import xyz.coderes.ai_hilal_test.crypto.domain.Coin
import xyz.coderes.ai_hilal_test.crypto.domain.DisplayableNumber
import java.util.Locale

data class CoinUi(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val priceUsd: DisplayableNumber,
    val changePercent24Hr: DisplayableNumber,
    @DrawableRes val iconRes: Int,
)

fun Coin.toCoinUi(): CoinUi = CoinUi(
    id = id,
    rank = rank,
    name = name,
    symbol = symbol,
    priceUsd = priceUsd.toDisplayableNumber(),
    changePercent24Hr = changePercent24Hr.toDisplayableNumber(),
    iconRes = getDrawableIdForCoin(symbol)
)

fun Double.toDisplayableNumber(): DisplayableNumber {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return DisplayableNumber(
        value = this,
        formatted = formatter.format(this)
    )
}