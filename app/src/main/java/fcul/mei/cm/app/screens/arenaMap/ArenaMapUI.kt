package fcul.mei.cm.app.screens.arenaMap

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.data.kml.KmlLayer
import fcul.mei.cm.app.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import fcul.mei.cm.app.database.CoordinatesDatabase
import fcul.mei.cm.app.domain.Coordinates

@Composable
fun ArenaMapUi(
    modifier: Modifier = Modifier,
    pointLatitude: Double = 0.0,
    pointLongitude: Double = 0.0,
    onSendCoordinates: (Double, Double) -> Unit = { _, _ -> },
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coordinatesViewModel = viewModel<ArenaMapViewModel>()
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
        AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize()) { view ->
            view.getMapAsync { googleMap ->
                zoomLevelMap = googleMap

                val initialLocation = LatLng(latitude, longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, initialZoom))

                try {
                    KmlLayer(googleMap, R.raw.arenasurvivormap, context).addLayerToMap()
                } catch (e: Exception) {
                    Log.e("MAP", "Error loading KML layer: ${e.message}")
                }

                if (pointLatitude != 0.0 && pointLongitude != 0.0) {
                    val position = LatLng(pointLatitude, pointLongitude)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(
                                "Marker added at (${pointLatitude}, ${pointLongitude})"
                            )
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Zoom In Button
            Button(
                onClick = { zoomLevelMap?.animateCamera(CameraUpdateFactory.zoomIn()) },
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("+", fontSize = 24.sp)
            }
            // Zoom Out Button
            Button(
                onClick = { zoomLevelMap?.animateCamera(CameraUpdateFactory.zoomOut()) },
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("-", fontSize = 24.sp)
            }
            Button(
                onClick = { onSendCoordinates(pointLatitude, pointLongitude) },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text("Send Your Coordinates")
            }
        }
    }
}

@Composable
fun SendCoordinatesDialog(
    onDismiss: () -> Unit,
    onSubmit: (Double, Double) -> Unit
) {
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Enter Coordinates") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Latitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Longitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val lat = latitude.toDoubleOrNull() ?: 0.0
                val lng = longitude.toDoubleOrNull() ?: 0.0
                onSubmit(lat, lng)
                onDismiss()
            }) {

                Text("Submit")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ArenaMapWithSendCoordinates(
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var userLatitude by remember { mutableDoubleStateOf(0.0) }
    var userLongitude by remember { mutableDoubleStateOf(0.0) }
    val coordinates = CoordinatesDatabase()

    Box(Modifier.fillMaxWidth()) {
        ArenaMapUi(
            pointLatitude = userLatitude,
            pointLongitude = userLongitude,
            onSendCoordinates = { _, _ -> showDialog = true }
        )

        if (showDialog) {
            SendCoordinatesDialog(
                onDismiss = { showDialog = false },
                onSubmit = { lat, lng ->
                    userLatitude = lat
                    userLongitude = lng

                    coordinates.saveCoordinates(
                        "2",
                        Coordinates(userLongitude,userLatitude)
                    )
                }
            )
        }
    }
 }
