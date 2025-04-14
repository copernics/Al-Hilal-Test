package xyz.coderes.ai_hilal_test

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.plcoding.cryptotracker.core.presentation.util.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import xyz.coderes.ai_hilal_test.crypto.navigation.Screen
import xyz.coderes.ai_hilal_test.crypto.presentation.AddToFavourite
import xyz.coderes.ai_hilal_test.crypto.presentation.CoinListEvent
import xyz.coderes.ai_hilal_test.crypto.presentation.CoinListScreen
import xyz.coderes.ai_hilal_test.crypto.presentation.CoinListViewModel
import xyz.coderes.ai_hilal_test.crypto.presentation.FavouriteCoinListViewModel
import xyz.coderes.ai_hilal_test.crypto.presentation.FavouriteListScreen
import xyz.coderes.ai_hilal_test.crypto.presentation.RefreshFavourite
import xyz.coderes.ai_hilal_test.crypto.presentation.RemoveFromFavourite
import xyz.coderes.ai_hilal_test.crypto.presentation.theme.AiHilalTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val screens = listOf(
                Screen.Home,
                Screen.Search
            )

            val viewModel = koinViewModel<CoinListViewModel>()
            val viewModelFavourite = koinViewModel<FavouriteCoinListViewModel>()

            val state by viewModel.state.collectAsStateWithLifecycle()
            val stateFavourite by viewModelFavourite.state.collectAsStateWithLifecycle()

            AiHilalTestTheme {
                ObserveAsEvents(events = viewModel.events) { event ->
                    when (event) {
                        is CoinListEvent.Error -> {
                            Toast.makeText(
                                applicationContext,
                                event.error.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry = navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry.value?.destination?.route

                            screens.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = screen.route) },
                                    label = { Text(screen.route) },
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        if (currentRoute != screen.route) {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Search.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            FavouriteListScreen(
                                state = stateFavourite,
                                onRemoveFromFavourite = { ids ->
                                    viewModelFavourite.onAction(RemoveFromFavourite(ids))
                                },
                                onRefresh = {
                                    viewModelFavourite.onAction(RefreshFavourite())
                                },
                                onOpenAssets = {
                                    navController.navigate(Screen.Search.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                        composable(Screen.Search.route) {
                            CoinListScreen(
                                state = state,
                                addCoinToFavourite = {
                                    viewModel.onAction(AddToFavourite(it))
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}