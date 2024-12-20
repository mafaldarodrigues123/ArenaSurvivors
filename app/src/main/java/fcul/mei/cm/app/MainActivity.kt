package fcul.mei.cm.app

import FitnessViewModel
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import fcul.mei.cm.app.screens.fitness.HealthConnectManager
import fcul.mei.cm.app.ui.theme.AppTheme
import fcul.mei.cm.app.ui.theme.arenaMap.ArenaMapUi
import fcul.mei.cm.app.ui.theme.arenaMap.ArenaMapWithSendCoordinates

class MainActivity : ComponentActivity() {


    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null



    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var healthConnectManager = HealthConnectManager(applicationContext)

        // Initialize FitnessViewModel with HealthConnectManager
        val fitnessViewModel = FitnessViewModel(healthConnectManager)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer == null) {
            Log.e("AccelerometerGame", "Accelerometer not available on this device.")
        } else {
            Log.d("AccelerometerGame", "Accelerometer found and ready.")
        }
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navHostController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                            title = {
                                Text(
                                    text = "Arena Survivors",
                                    color = Color.White,
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { navHostController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Menu Icon",
                                        tint = Color(0xFFFFA726)
                                    )
                                }
                            },
                        )
                    },
                ) { innerPadding ->

                    UiNav(
                        modifier = Modifier.padding(innerPadding),
                        navController = navHostController,
                        sensorManager,
                        accelerometer,
                        fitnessViewModel
                    )
                    //ArenaMapWithSendCoordinates()
                }
            }
        }
    }
}