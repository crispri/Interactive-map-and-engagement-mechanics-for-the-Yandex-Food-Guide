package presintation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.mapkit.mapview.MapView
import presintation.homeScreen.HomeScreen
import presintation.mapScreen.MainScreen
import presintation.mapScreen.MainViewModel
import presintation.restaurantScreen.RestaurantScreen

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
            val mainViewModel: MainViewModel = hiltViewModel()
            val uiState by mainViewModel.uiState.collectAsState()

            MainScreen(
                navToRestaurant = actions.onRestaurantScreen,
                uiState = uiState,
                navToBack = actions.onBack,
                send = mainViewModel::send,
                mapView = mapView
            )
        }

        composable(AppDestination.RESTAURANT_SCREEN) {
            RestaurantScreen(navToBack = actions.onBack)
        }

    }
}



// [4, 7, 9, 2, 6, 1, 3]
// [1->4, 7, 9]

