package presintation.navigation

import androidx.navigation.NavController

class AppActions(navController: NavController) {
    val onBack: () -> Unit = {
        navController.navigateUp()
    }

    val onMapScreen: () -> Unit = {
        navController.navigate(AppDestination.MAP_SCREEN)
    }

    val onRestaurantScreen: () -> Unit = {
        navController.navigate(AppDestination.RESTAURANT_SCREEN)
    }

}