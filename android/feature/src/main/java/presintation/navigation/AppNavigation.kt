package presintation.navigation

import Utils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import presintation.homeScreen.HomeScreen
import presintation.mapScreen.CustomMapView
import presintation.mapScreen.MainScreen
import presintation.mapScreen.MainViewModel
import presintation.restaurantScreen.RestaurantScreen
import presintation.restaurantScreen.RestaurantViewModel

@Composable
fun AppNavigation(mapView: CustomMapView, curLocation: MutableState<Point?>) {
    val navController = rememberNavController()
    val actions = remember(navController) { AppActions(navController) }
    val mainViewModel: MainViewModel = hiltViewModel()
    val mainUiState by mainViewModel.uiState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = AppDestination.HOME_SCREEN
    ) {
        composable(AppDestination.HOME_SCREEN) {
            HomeScreen(
                mainViewModel::send,
                mainUiState,
                actions.onMapScreen
            )
        }

        composable(AppDestination.MAP_SCREEN) {
            MainScreen(
                navToRestaurant = actions.onRestaurantScreen,
                uiState = mainUiState,
                navToBack = actions.onBack,
                send = mainViewModel::send,
                mapView = mapView,
                curLocation = curLocation
            )
        }


        composable("${AppDestination.RESTAURANT_SCREEN}/{itemId}") {backStackEntry ->
            val restaurantViewModel: RestaurantViewModel = hiltViewModel()
            val itemId = backStackEntry.arguments?.getString("itemId")
            val uiState by restaurantViewModel.uiState.collectAsState()
            RestaurantScreen(uiState = uiState, restaurantId = itemId, send = restaurantViewModel::send, navToBack = actions.onBack)
        }
    }
}
