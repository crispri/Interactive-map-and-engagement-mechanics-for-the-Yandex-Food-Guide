package presintation.navigation

import presintation.homeScreen.HomeScreen
import presintation.mapScreen.MainScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.mapkit.mapview.MapView

@Composable
fun AppNavigation(mapView: MapView) {
    val navController = rememberNavController()
    val actions = remember(navController) { AppActions(navController) }
    NavHost(
        navController = navController,
        startDestination = AppDestination.HOME_SCREEN
    ) {
        composable(AppDestination.HOME_SCREEN) {
            HomeScreen(
                actions.onMapScreen
            )
        }

        composable(AppDestination.MAP_SCREEN) {
            MainScreen(actions.onBack, mapView)
        }

    }
}
