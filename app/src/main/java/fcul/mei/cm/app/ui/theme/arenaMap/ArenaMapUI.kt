package fcul.mei.cm.app.ui.theme.arenaMap

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.data.kml.KmlLayer
import fcul.mei.cm.app.R


@Composable
fun ArenaMapUi(
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val zoom by remember { mutableFloatStateOf(15f) }
    val latitude = 36.9796541
    val longitude = -25.1176215
    val mapView = remember{ MapView(context) }

    LaunchedEffect(Unit) {
        mapView.onResume()
    }

    AndroidView(factory = { mapView },
        modifier = Modifier
            .size(400.dp)
    ) { view ->
        view.getMapAsync { googleMap ->
            val initialLocation = LatLng(latitude , longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15f))

            try {
                KmlLayer(googleMap, R.raw.arenasurvivormap, context)
                    .addLayerToMap()
            } catch(e: Exception) {
                Log.e("MAP", e.toString())
            }
        }
    }
}
