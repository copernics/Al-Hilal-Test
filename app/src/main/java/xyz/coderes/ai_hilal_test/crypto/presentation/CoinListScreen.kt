package xyz.coderes.ai_hilal_test.crypto.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import xyz.coderes.ai_hilal_test.R
import xyz.coderes.ai_hilal_test.crypto.presentation.component.CoinListItem
import xyz.coderes.ai_hilal_test.crypto.presentation.component.testCoin
import xyz.coderes.ai_hilal_test.crypto.presentation.model.CoinUi
import xyz.coderes.ai_hilal_test.crypto.presentation.theme.AiHilalTestTheme


@Composable
fun CoinListScreen(
    state: CoinListState,
    modifier: Modifier = Modifier,
    addCoinToFavourite: (List<String>) -> Unit,
) {
    val selected = remember { mutableStateListOf<String>() }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            enabled = selected.isNotEmpty(),
            onClick = {
                addCoinToFavourite(selected)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding( 16.dp)
        ) {
            Text(stringResource(R.string.add))
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (state.isLoading) {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val coins = remember(state.coins, state.selected) {
                println("Selected : ${state.selected}")
                mutableStateOf<List<CoinUi>>(
                    state.coins
                        .filter { it.id !in state.selected }
                )
            }
            LazyColumn(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = rememberLazyListState()
            ) {
                items(items = coins.value) { coinUi ->
                    CoinListItem(
                        modifier = Modifier.fillMaxWidth(),
                        coin = coinUi,
                        selected = selected.contains(coinUi.id),
                        onCheckedChange = { checked ->
                            if (checked) {
                                selected.add(coinUi.id)
                            } else {
                                selected.remove(coinUi.id)
                            }
                        }
                    )
                }

            }
        }
    }
}


@PreviewLightDark
@Composable
private fun CoinListScreenPreview() {
    AiHilalTestTheme {
        CoinListScreen(
            state = CoinListState(
                coins = (1..100).map {
                    testCoin.copy(id = it.toString())
                }
            ),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            addCoinToFavourite = { },
        )
    }
}