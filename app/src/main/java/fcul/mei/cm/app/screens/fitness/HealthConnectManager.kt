package fcul.mei.cm.app.screens.fitness

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord

public class HealthConnectManager(private val context: Context) : ComponentActivity() {
    companion object {
    val PERMISSIONS =
        setOf(
            HealthPermission
                .getReadPermission(StepsRecord::class),
            HealthPermission
                .getWritePermission(StepsRecord::class),
        )
}
    private val healthConnectClient by lazy {
        HealthConnectClient.getOrCreate(context)
    }

    suspend fun hasAllPermissions(): Boolean =
        healthConnectClient
            .permissionController
            .getGrantedPermissions()
            .containsAll(PERMISSIONS)

    fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController
            .createRequestPermissionResultContract()
    }



}