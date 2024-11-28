package fcul.mei.cm.app.screens.fitness

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

private fun requestSensorPermissions(activity: Activity) {
    val requiredPermissions = listOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.BODY_SENSORS
    )

    val permissionsToRequest = requiredPermissions.filter {
        ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
    }

    if (permissionsToRequest.isNotEmpty()) {
        ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), 1001)
    }
}