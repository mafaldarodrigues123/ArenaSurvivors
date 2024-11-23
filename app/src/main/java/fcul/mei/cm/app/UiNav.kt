package fcul.mei.cm.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fcul.mei.cm.app.screens.ChatTemplate
import fcul.mei.cm.app.screens.Home

@Composable
fun UiNav(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable(route = "home") {
            Home(
                modifier,
                navController
            )
        }
        composable(route = "chat") {
            ChatTemplate(
            )
        }
    }
}