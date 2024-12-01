package fcul.mei.cm.app.ui.theme.arenaMap

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.google.android.gms.maps.GoogleMap

@Composable
fun ArenaMapUi(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }

    val latitude = 36.9796541
    val longitude = -25.1176215
    val initialZoom = 13f
    var zoomLevelMap: GoogleMap? = null

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                mapView.onCreate(null)
            }

            override fun onStart(owner: LifecycleOwner) {
                mapView.onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                mapView.onResume()
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
            mapView.onDestroy()
        }
    }

    Box(modifier = modifier) {
        AndroidView(factory = { mapView }, modifier = Modifier.size(400.dp)) { view ->
            view.getMapAsync { googleMap ->
                zoomLevelMap = googleMap

                val initialLocation = LatLng(latitude, longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, initialZoom))

                try {
                    KmlLayer(googleMap, R.raw.arenasurvivormap, context).addLayerToMap()
                } catch (e: Exception) {
                    Log.e("MAP", "Error loading KML layer: ${e.message}")
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                zoomLevelMap?.animateCamera(CameraUpdateFactory.zoomIn())
            }) {
                Text("Zoom In")
            }
            Button(onClick = {
                zoomLevelMap?.animateCamera(CameraUpdateFactory.zoomOut())
            }) {
                Text("Zoom Out")
            }
        }
    }
}