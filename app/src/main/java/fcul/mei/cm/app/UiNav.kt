package fcul.mei.cm.app

import fcul.mei.cm.app.viewmodel.FitnessViewModel
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fcul.mei.cm.app.screens.CreateAlliances
import fcul.mei.cm.app.screens.ChatTemplate
import fcul.mei.cm.app.screens.Home
import fcul.mei.cm.app.screens.CreateAllianceTemplate
import fcul.mei.cm.app.screens.AlliancesList
import fcul.mei.cm.app.screens.AccelerometerGame
import fcul.mei.cm.app.utils.Routes
import fcul.mei.cm.app.screens.ArenaMapWithSendCoordinates

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
                },
                onClickAliacesList = {
                    navController.navigate(Routes.ALLIANCES_LIST.name)
                }
            )
        }
        composable(route = Routes.ALLIANCES_LIST.name) {
            AlliancesList()
        }
        composable(route = Routes.ALLIANCES.name) {
            CreateAlliances(
                modifier = modifier,
                onClick = {
                    navController.navigate(Routes.CREATE_ALLIANCE.name)
                }
            )
        }
        composable(route = Routes.CREATE_ALLIANCE.name) {
            CreateAllianceTemplate(
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