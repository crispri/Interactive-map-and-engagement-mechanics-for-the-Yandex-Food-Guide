package presintation.navigation

import androidx.navigation.NavController
/**
 * Provides a set of navigation actions for the application.
 *
 * This class encapsulates navigation-related actions for various screens within the app,
 * using the provided [NavController] to handle navigation commands.
 */
class AppActions(navController: NavController) {
    val onBack: () -> Unit = {
        navController.navigateUp()
    }

    val onMapScreen: () -> Unit = {
        navController.navigate(AppDestination.MAP_SCREEN)
    }

    val onRestaurantScreen: (String) -> Unit = { itemId ->
        navController.navigate("${AppDestination.RESTAURANT_SCREEN}/${itemId}")
    }

}