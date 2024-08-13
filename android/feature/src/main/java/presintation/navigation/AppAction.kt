package presintation.navigation

import androidx.navigation.NavController

class AppActions(navController: NavController) {
    val onBack: () -> Unit = {
        navController.navigateUp()
    }

    val onMapScreen: () -> Unit = {
        navController.navigate(AppDestination.MAP_SCREEN)
    }

    val onRestaurantScreen: (String?) -> Unit = { itemId ->
        // navController.navigate("${AppDestination.ADD_TODO_LIST}/${itemId}")() -> Unit = {
        navController.navigate("${AppDestination.RESTAURANT_SCREEN}/${itemId}")
    }

}