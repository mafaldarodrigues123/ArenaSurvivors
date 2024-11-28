package fcul.mei.cm.app.ui.theme.arenaMap

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.data.kml.KmlLayer
import fcul.mei.cm.app.R


@Composable
fun ArenaMapUi(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }

    // Manage MapView lifecycle
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                mapView.onCreate(null) // Make sure onCreate is called
            }

            override fun onStart(owner: LifecycleOwner) {
                mapView.onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                mapView.onResume() // Safe to call onResume now
            }

            override fun onPause(owner: LifecycleOwner) {
                mapView.onPause()
            }

            override fun onStop(owner: LifecycleOwner) {
                mapView.onStop()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                mapView.onDestroy()
            }
        }

        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            mapView.onDestroy() // Clean up
        }
    }

    // Configure the MapView
    AndroidView(factory = { mapView }, modifier = modifier.size(400.dp)) { view ->
        view.getMapAsync { googleMap ->
            val initialLocation = LatLng(36.9796541, -25.1176215)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15f))

            try {
                // Load and add the KML layer to the map
                KmlLayer(googleMap, R.raw.arenasurvivormap, context).addLayerToMap()
            } catch (e: Exception) {
                Log.e("MAP", "Error loading KML layer: ${e.message}")
            }
        }
    }
}