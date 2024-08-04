package navigation

import androidx.navigation.NavController

class AppActions(navController: NavController) {
    val onBack: () -> Unit = {
        navController.navigateUp()
    }

    val onMapScreen: () -> Unit = {
        navController.navigate(AppDestination.MAP_SCREEN)
    }

}