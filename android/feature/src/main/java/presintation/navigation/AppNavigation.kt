package presintation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import presintation.homeScreen.HomeScreen
import presintation.mapScreen.MainScreen
import presintation.mapScreen.MainViewModel

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
                uiState = uiState,
                navToBack = actions.onBack,
                send = mainViewModel::send,
                mapView = mapView,
                curLocation = curLocation
            )
        }

    }
}
