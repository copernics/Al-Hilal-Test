package xyz.coderes.ai_hilal_test.crypto.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import xyz.coderes.ai_hilal_test.R

sealed class Screen(val route: String, val icon: ImageVector, @StringRes label: Int) {
    object Home : Screen("favorites", Icons.Default.Favorite, R.string.favorites)
    object Search : Screen("assets", Icons.Default.Search, R.string.assets)
}