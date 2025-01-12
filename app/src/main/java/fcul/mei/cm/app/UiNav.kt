package fcul.mei.cm.app

import fcul.mei.cm.app.viewmodel.FitnessViewModel
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fcul.mei.cm.app.screens.alliances.ChatTemplate
import fcul.mei.cm.app.screens.Home
import fcul.mei.cm.app.screens.alliances.CreateAllianceTemplate
import fcul.mei.cm.app.screens.alliances.AlliancesList
import fcul.mei.cm.app.screens.health.AccelerometerGame
import fcul.mei.cm.app.screens.AddUserScreen
import fcul.mei.cm.app.utils.Routes
import fcul.mei.cm.app.screens.map.ArenaMapWithSendCoordinates
import fcul.mei.cm.app.viewmodel.AlliancesViewModel
import fcul.mei.cm.app.viewmodel.UserViewModel

@Composable
fun UiNav(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sensorManager: SensorManager,
    accelerometer: Sensor?,
    fitnessViewModel: FitnessViewModel
) {

    val chatViewModel = AlliancesViewModel()

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
                },
                onClickUserButton = {
                    navController.navigate(Routes.CREATE_USER.name)
                }
            )
        }
        composable(route = Routes.ALLIANCES_LIST.name) {
            AlliancesList(
                modifier = modifier,
                viewModel = chatViewModel,
                onCreateAllianceClick = { navController.navigate(Routes.CREATE_ALLIANCE.name) }
            )
        }

        composable(route = Routes.CREATE_ALLIANCE.name) {
            CreateAllianceTemplate(
                viewModel = AlliancesViewModel(),
                modifier = modifier,
                onClickCreateAlliance = {
                    navController.navigate(Routes.CHAT.name)
                }
            )
        }

        composable(route = Routes.CHAT.name) {
            ChatTemplate(
                viewModel = AlliancesViewModel(),
                modifier = modifier
            )
        }

        composable(route = Routes.FITNESS.name) {
            AccelerometerGame(sensorManager, accelerometer,fitnessViewModel)
            }

        composable(route = Routes.CREATE_USER.name) {
            AddUserScreen(
                userViewModel = UserViewModel(
                    LocalContext.current
                )
            )
        }
    }
}