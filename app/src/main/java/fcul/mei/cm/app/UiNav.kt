package fcul.mei.cm.app

import FitnessViewModel
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fcul.mei.cm.app.screens.alliance.Alliances
import fcul.mei.cm.app.screens.chat.ChatTemplate
import fcul.mei.cm.app.screens.Home
import fcul.mei.cm.app.screens.alliance.CreateAlliance
import fcul.mei.cm.app.screens.fitness.AccelerometerGame
import fcul.mei.cm.app.utils.Routes
import fcul.mei.cm.app.screens.arenaMap.ArenaMapWithSendCoordinates

@Composable
fun UiNav(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sensorManager: SensorManager,
    accelerometer: Sensor?,
    fitnessViewModel: FitnessViewModel
) {


    NavHost(navController = navController, startDestination = Routes.HOME.name) {
        composable(route = Routes.HOME.name) {
                ArenaMapWithSendCoordinates()
            Home(
                modifier = modifier,
                onClickChatButton = {
                    navController.navigate(Routes.ALLIANCES.name)
                },
                onClickHealthButton = {
                    navController.navigate(Routes.FITNESS.name)
                }
            )
        }
        composable(route = Routes.ALLIANCES.name) {
            Alliances(
                modifier = modifier,
                onClick = {
                    navController.navigate(Routes.CREATE_ALLIANCE.name)
                }
            )
        }
        composable(route = Routes.CREATE_ALLIANCE.name) {
            CreateAlliance(
                modifier = modifier,
                onComplete = {
                    if(it) navController.navigate(Routes.HOME)
                }
            )
        }
        composable(route = Routes.CHAT.name) {
            ChatTemplate()
        }
//        composable(route = Routes.FITNESS.name) {
//            Fitness(modifier = modifier,
//                onClick = {
//                navController.navigate(Routes.FITNESS.name)
//            })
        composable(route = Routes.FITNESS.name) {
            AccelerometerGame(sensorManager, accelerometer,fitnessViewModel)
            }


    }
}