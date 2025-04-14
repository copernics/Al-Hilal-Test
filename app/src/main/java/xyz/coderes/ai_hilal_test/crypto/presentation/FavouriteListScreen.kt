package xyz.coderes.ai_hilal_test.crypto.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.util.TimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.coderes.ai_hilal_test.R
import xyz.coderes.ai_hilal_test.crypto.presentation.component.FavouriteCoinListItem
import xyz.coderes.ai_hilal_test.crypto.presentation.component.testCoin
import xyz.coderes.ai_hilal_test.crypto.presentation.theme.AiHilalTestTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


private const val RequestSeconds = 5_000L

@Composable
fun FavouriteListScreen(
    state: CoinListState,
    onRemoveFromFavourite: (List<String>) -> Unit,
    onRefresh: () -> Unit,
    onOpenAssets: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    println("FavouriteListScreen :${state.coins}")
    if (state.coins.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.padding(16.dp).background(
                Color.Gray.copy(0.3f),
                shape = MaterialTheme.shapes.medium
            )) {
                Text(
                    stringResource(R.string.no_favourite_coins_you_should_add_some),
                    modifier = Modifier
                        .padding(32.dp)
                        .clickable {
                            onOpenAssets()
                        },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Blue.copy(alpha = 0.8f)
                )
            }

        }
    } else {
        DisposableEffect(Unit) {
            val job = scope.launch {
                while (true) {
                    if (!state.isLoading)
                        onRefresh()
                    delay(RequestSeconds)
                }
            }
            onDispose {
                job.cancel()
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            state.lastUpdate?.let {
                Text(
                    text = "Last updated:${millisToDateString(it)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = rememberLazyListState()
            ) {
                items(
                    items = state.coins,
                    key = { it.id }
                ) { coinUi ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                scope.launch {
                                    onRemoveFromFavourite(listOf(coinUi.id))
                                }
                                true
                            } else {
                                false
                            }
                        },
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                )
                            }
                        },
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        FavouriteCoinListItem(
                            modifier = Modifier.fillMaxWidth(),
                            coin = coinUi,
                        )
                    }
                }
            }
        }
    }
}

private fun millisToDateString(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        .withZone(ZoneId.systemDefault())

    return formatter.format(Instant.ofEpochMilli(millis))
}

@PreviewLightDark
@Composable
private fun FavouriteListScreenPreview() {
    AiHilalTestTheme {
        FavouriteListScreen(
            state = CoinListState(
                coins = (1..100).map {
                    testCoin.copy(id = it.toString())
                }),
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            onRemoveFromFavourite = { },
            onRefresh = { },
            onOpenAssets = { }
        )
    }
}