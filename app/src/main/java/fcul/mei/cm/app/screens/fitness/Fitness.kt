package fcul.mei.cm.app.screens.fitness

import FitnessViewModel
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.units.Mass
import androidx.health.platform.client.permission.Permission
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

import java.lang.Thread.sleep
import java.time.ZonedDateTime
import kotlin.math.log

import kotlin.math.sqrt

@Composable
fun Fitness(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    var stepCount by remember { mutableFloatStateOf(0f) }
    var restTime by remember { mutableStateOf(0f) }
    var humidity by remember { mutableStateOf(0f) }
    var temperature by remember { mutableStateOf(0f) }

    // Check and request permissions
    LaunchedEffect(Unit) {
        activity?.let {
            requestSensorPermissions(it)
        }
    }

    LaunchedEffect(Unit) {
        val stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        val temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_STEP_COUNTER -> stepCount = event.values[0]
                    Sensor.TYPE_ACCELEROMETER -> restTime = calculateRestTime(event.values)
                    Sensor.TYPE_RELATIVE_HUMIDITY -> humidity = event.values[0]
                    Sensor.TYPE_AMBIENT_TEMPERATURE -> temperature = event.values[0]
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        stepCounter?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL) }
        accelerometer?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL) }
        humiditySensor?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL) }
        temperatureSensor?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Step Count: $stepCount")
            Text(text = "Rest Time: $restTime s")
            Text(text = "Humidity: $humidity %")
            Text(text = "Temperature: $temperature °C")
        }
    }
}

private fun calculateRestTime(values: FloatArray): Float {
    // Implement logic to detect resting state (low acceleration values).
    return 0f // Replace with actual calculation.
}
val healthPermissions = setOf(
    HealthPermission.getReadPermission(StepsRecord::class),
    HealthPermission.getWritePermission(StepsRecord::class)
)



suspend fun requestHealthConnectPermissions(context: Context) {

    val healthConnectClient = HealthConnectClient.getOrCreate(context)
    val grantedPermissions = healthConnectClient.permissionController.getGrantedPermissions()

    val requiredPermissions = healthPermissions - grantedPermissions


}

fun signInAnonymously() {
    val auth = Firebase.auth
    FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
        DebugAppCheckProviderFactory.getInstance()
    )
    auth.signInAnonymously()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign-in succeeded
                println("Anonymous sign-in successful")
            } else {
                // Handle sign-in failure
                println("Anonymous sign-in failed: ${task.exception?.message}")
            }
        }
}

@Composable
fun AccelerometerGame(sensorManager: SensorManager, accelerometer: Sensor?, fitnessViewModel: FitnessViewModel) {
    var sprintDetected by remember { mutableStateOf(false) }
    var dodgeDetected by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val viewModel = fitnessViewModel
    var listeningEnabled by remember { mutableStateOf(true) } // Track if listening is enabled

    val WEB_CLIENT_ID = "1"
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(WEB_CLIENT_ID)
        .setAutoSelectEnabled(true)
        .setNonce(12645.toString())
    .build()



    // Listener to detect sensor events
    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {


                event?.let {
                    // Calculate movement intensity
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]

                    // Calculate the magnitude of acceleration
                    val accelerationMagnitude = sqrt(x * x + y * y + z * z)


                    // Detect sprint (shaking rapidly)
                    if (accelerationMagnitude > 15) { // Adjust threshold as needed
                        sprintDetected = true
                        listeningEnabled = false // Disable listening temporarily



                    } else {
                       // if (!listeningEnabled) return
                        sprintDetected = false
                    }

                    // Detect dodge (tilt phone left/right)
                    if (x > 5 || x < -5) { // Adjust tilt threshold as needed
                        dodgeDetected = true
                    } else {
                        dodgeDetected = false
                    }
                    viewModel.onSensorChanged(accelerationMagnitude, x)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    // Register the sensor listener
    DisposableEffect(Unit) {

        val success = sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        if (!success) {
            Log.e("AccelerometerGame", "Failed to register accelerometer listener.")
        } else {
            Log.d("AccelerometerGame", "Success registering the accelerometer listener.")
        }

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }






    // UI
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = if (sprintDetected) "Sprinting!" else "Not Sprinting",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = if (dodgeDetected) "Dodged!" else "Not Dodging",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Time spent sprinting: ${"%.2f".format(viewModel.sprintTime)} seconds",
                style = MaterialTheme.typography.bodyLarge
            )

            //Log.d("AccelerometerGame", "UI recomposed. Sprint: $sprintDetected, Dodge: $dodgeDetected")
        }
    }
}


suspend fun manageStepsData(context: Context, launcher: ActivityResultLauncher<Set<String>>) {
    // Get the HealthConnectClient
    val healthConnectClient = HealthConnectClient.getOrCreate(context)

    // Define permissions for steps data
    val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class)
    )

    // Check if Health Connect is supported
    val availability = HealthConnectClient.getSdkStatus(context)
    if (availability == HealthConnectClient.SDK_UNAVAILABLE) {
        Log.e("HealthConnect", "Health Connect is not supported on this device")
        return
    }

    // Check for already-granted permissions
    val granted = healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)
    if (granted) {
        Log.d("HealthConnect", "Permissions already granted")
        return
    }

    // Use the ActivityResultLauncher to request permissions
    launcher.launch(permissions)


}

suspend fun writeWeightInput(weightInput: Double, context: Context, healthConnectClient: HealthConnectClient) {
    val time = ZonedDateTime.now().withNano(0)
    val weightRecord = WeightRecord(
        weight = Mass.kilograms(weightInput),
        time = time.toInstant(),
        zoneOffset = time.offset
    )
    val records = listOf(weightRecord)
    try {
        healthConnectClient.insertRecords(records)
        Toast.makeText(context, "Successfully insert records", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
    }
}


private fun requestSensorPermissions(activity: Activity) {
    val requiredPermissions = listOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.BODY_SENSORS
    )

    val permissionsToRequest = requiredPermissions.filter {
        ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
    }
    Log.d("tryingpermissions","whttttttttttttffffffffffff")
print(requiredPermissions)

     var res =   ActivityCompat.requestPermissions(activity, requiredPermissions.toTypedArray(),1001)
print(res)
}