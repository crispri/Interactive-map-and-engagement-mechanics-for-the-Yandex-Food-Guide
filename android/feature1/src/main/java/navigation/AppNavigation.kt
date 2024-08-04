package navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val actions = remember(navController) { AppActions(navController) }
    NavHost(
        navController = navController,
        startDestination = AppDestination.HOME_SCREEN
    ) {
        composable(AppDestination.HOME_SCREEN) {
        }

        composable(AppDestination.MAP_SCREEN) {
            // MapScreen
        }

    }
}
