package fcul.mei.cm.app.ui.theme.arenaMap

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.MapView
import com.mapbox.geojson.Point
import com.mapbox.maps.debugoptions.MapViewDebugOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState


@Composable
fun ArenaMapUi(
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val zoom by remember { mutableFloatStateOf(15f) }
    val latitude = 36.9796541
    val longitude = -25.1176215
    MapboxMap(
        Modifier.fillMaxSize(),
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(2.0)
                center(Point.fromLngLat(latitude, longitude))
                pitch(0.0)
                bearing(0.0)
            }
        },
    ){
        MapEffect(Unit) { mapView ->
            mapView.debugOptions = setOf(
                MapViewDebugOptions.TILE_BORDERS,
                MapViewDebugOptions.PARSE_STATUS,
                MapViewDebugOptions.TIMESTAMPS,
                MapViewDebugOptions.COLLISION,
                MapViewDebugOptions.STENCIL_CLIP,
                MapViewDebugOptions.DEPTH_BUFFER,
                MapViewDebugOptions.MODEL_BOUNDS,
                MapViewDebugOptions.TERRAIN_WIREFRAME,
            )
        }
    }
}
