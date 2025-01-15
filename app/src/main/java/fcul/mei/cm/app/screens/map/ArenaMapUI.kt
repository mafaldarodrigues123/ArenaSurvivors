package fcul.mei.cm.app.screens.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.data.kml.KmlLayer
import fcul.mei.cm.app.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import fcul.mei.cm.app.database.CoordinatesRepository
import fcul.mei.cm.app.domain.Coordinates
import fcul.mei.cm.app.viewmodel.ArenaMapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.SphericalUtil
import com.google.maps.android.data.kml.KmlPlacemark
import com.google.maps.android.data.kml.KmlPoint

private val LOCATION_PERMISSION_REQUEST_CODE = 1000
private lateinit var fusedLocationClient: FusedLocationProviderClient


@Composable
fun ArenaMapUi(
    modifier: Modifier = Modifier,
    onSendCoordinates: (Double, Double) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // State for user's location
    var userLatitude by remember { mutableDoubleStateOf(Double.NaN) }
    var userLongitude by remember { mutableDoubleStateOf(Double.NaN) }
    var isLocationAvailable by remember { mutableStateOf(false) } // Track if location is available
    val initialZoom = 13f
    var googleMap: GoogleMap? = null
    var hasZoomedToInitialLocation by remember { mutableStateOf(false) } // Track initial zoom
    var userCircle by remember { mutableStateOf<Circle?>(null) }
    var userMarker by remember { mutableStateOf<Marker?>(null) }
    // Lifecycle handling
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

    // Check and request location permissions
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            (context as ComponentActivity),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    } else {
        // Fetch the last known location
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                userLatitude = location.latitude
                userLongitude = location.longitude
                isLocationAvailable = true // Mark location as available
            }
        }

        // Continuous location updates
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 2000L
        ).setMinUpdateIntervalMillis(1000L)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            context.mainExecutor,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        userLatitude = location.latitude
                        userLongitude = location.longitude
                        isLocationAvailable = true
                        googleMap?.let { map ->
                            updateUserMarker(map, userLatitude, userLongitude, userMarker) { newMarker ->
                                userMarker = newMarker
                            }
                            // Zoom to user's location only the first time
                            if (!hasZoomedToInitialLocation) {
                                map.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(userLatitude, userLongitude),
                                        initialZoom
                                    )
                                )
                                hasZoomedToInitialLocation = true
                            }
                        }
                    }
                }
            }
        )
    }

    // Map UI
    Box(modifier = modifier) {
        AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize()) { view ->
            view.getMapAsync { map ->
                googleMap = map

                try {
                    KmlLayer(googleMap, R.raw.arenasurvivorlisbon, context).addLayerToMap()
                } catch (e: Exception) {
                    Log.e("MAP", "Error loading KML layer: ${e.message}")
                }

                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(userLatitude, userLongitude),
                        initialZoom
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(end= 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Zoom In Button
            Button(
                onClick = { googleMap?.animateCamera(CameraUpdateFactory.zoomIn()) },
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("+", fontSize = 24.sp)
            }
            // Zoom Out Button
            Button(
                onClick = { googleMap?.animateCamera(CameraUpdateFactory.zoomOut()) },
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("-", fontSize = 24.sp)
            }
        }
    }
}


//eventally delete this
fun updateUserCircle(
    googleMap: GoogleMap,
    latitude: Double,
    longitude: Double,
    existingCircle: Circle?,
    onCircleUpdated: (Circle) -> Unit
) {
    val newPosition = LatLng(latitude, longitude)

    if (existingCircle == null) {
        // Create a new circle if it doesn't exist
        val newCircle = googleMap.addCircle(
            CircleOptions()
                .center(newPosition)
                .radius(10.0) // 10 meters
                .fillColor(0x5500ffff) // Semi-transparent green
                .strokeWidth(2f)
        )
        onCircleUpdated(newCircle)
    } else {
        // Update the existing circle's position
        existingCircle.center = newPosition
    }
}

fun updateUserMarker(googleMap: GoogleMap, latitude: Double, longitude: Double,
                     userMarker: Marker?,  onMarkerUpdated: (Marker) -> Unit) {
    val newPosition = LatLng(latitude, longitude)

    if (userMarker == null) {
        // Create a new marker if it doesn't exist
        val newMarker = googleMap.addMarker(
            MarkerOptions()
                .position(newPosition)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_dot)) // Use your custom dot image
                .anchor(0.5f, 0.5f) // Center the dot on the location
                .zIndex(10f) // Keep the marker on top of other elements
        )
        if (newMarker != null) {
            onMarkerUpdated(newMarker)
        }
    } else {
        // Update the existing marker's position
        userMarker.position = newPosition
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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ArenaMapWithSendCoordinates(
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var userLatitude by remember { mutableDoubleStateOf(0.0) }
    var userLongitude by remember { mutableDoubleStateOf(0.0) }
    val coordinates = CoordinatesRepository()

    Box(Modifier.fillMaxWidth()) {
        ArenaMapUi(
//            pointLatitude = userLatitude,
//            pointLongitude = userLongitude,
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
