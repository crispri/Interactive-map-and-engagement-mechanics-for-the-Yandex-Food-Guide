package presintation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import presintation.homeScreen.HomeScreen
import presintation.mapScreen.CustomMapView
import presintation.mapScreen.MainScreen
import presintation.mapScreen.MainViewModel
import presintation.restaurantScreen.RestaurantScreen
import presintation.restaurantScreen.RestaurantViewModel

@Composable
fun AppNavigation(mapView: MapView, curLocation: MutableState<Point?>) {
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
                mapView = mapView,
                curLocation = curLocation
            )
        }


        composable("${AppDestination.RESTAURANT_SCREEN}/{itemId}") {backStackEntry ->
            val restaurantViewModel: RestaurantViewModel = hiltViewModel()
            val itemId = backStackEntry.arguments?.getString("itemId")
            val uiState by restaurantViewModel.uiState.collectAsState()
            RestaurantScreen(uiState = uiState, navToBack = actions.onBack)
        }

//        composable(route = "${AppDestination.RESTAURANT_SCREEN}/{itemId}",
//            arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
//         )
//         { backStackEntry ->
//             val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
//             val restaurantViewModel: RestaurantViewModel = hiltViewModel()
//             val uiState by restaurantViewModel.uiState.collectAsState()
//             RestaurantScreen(navToBack = actions.onBack, restaurantId = itemId, send = restaurantViewModel::send, uiState = uiState)
//         }
    }
}



// [4, 7, 9, 2, 6, 1, 3]
// [1->4, 7, 9]

