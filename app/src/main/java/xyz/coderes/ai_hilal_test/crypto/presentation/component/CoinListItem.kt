package xyz.coderes.ai_hilal_test.crypto.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.coderes.ai_hilal_test.crypto.domain.Coin
import xyz.coderes.ai_hilal_test.crypto.presentation.model.CoinUi
import xyz.coderes.ai_hilal_test.crypto.presentation.model.toCoinUi

@Composable
fun CoinListItem(
    modifier: Modifier = Modifier,
    coin: CoinUi = coins.first(),
    selected: Boolean = false,
    onCheckedChange: ((Boolean) -> Unit)? = { },
) {
    val contentColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
    Card(
        modifier = modifier
            .padding(8.dp)
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary,
            )
            .border(
                shape = RoundedCornerShape(10.dp),
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )
            .clip(RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = contentColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = coin.iconRes),
                contentDescription = coin.name,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp),
                    text = coin.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = contentColor
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp),
                    text = coin.symbol,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    color = contentColor.copy(alpha = 0.5f)
                )
            }
            IconCircleCheckbox(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(CircleShape),
                checked = selected,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
fun IconCircleCheckbox(modifier: Modifier,checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?) {

    IconToggleButton(
        modifier = modifier,
        checked = checked,
        onCheckedChange = { onCheckedChange?.invoke(it)}
    ) {
        val icon = if (checked) Icons.Default.CheckCircle else Icons.Default.AddCircle
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (checked) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}

internal val coins = List<CoinUi>(3) {
    Coin(
        id = it.toString(),
        name = "Coin#$it",
        symbol = "C$it",
        priceUsd = 100.0,
        rank = it,
        changePercent24Hr = 2.0
    ).toCoinUi()
}
internal val testCoin = Coin(
    id = "bitcoin",
    rank = 1,
    name = "Bitcoin",
    symbol = "BTC",
    priceUsd = 62828.15,
    changePercent24Hr = 0.1
).toCoinUi()


@Preview()
@Composable
fun CoinListItemPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        CoinListItem(coin = testCoin)
    }
}