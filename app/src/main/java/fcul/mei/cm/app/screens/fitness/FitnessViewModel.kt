import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fcul.mei.cm.app.screens.fitness.HealthConnectManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FitnessViewModel(healthConnectManager: HealthConnectManager) : ViewModel() {

    val database = Firebase.database("https://arena-survivors-e1316-default-rtdb.europe-west1.firebasedatabase.app/")
    val userId = "someUserId"

    var sprintDetected by mutableStateOf(false)
    var sprintTime by mutableStateOf(0f)
    private var lastTime = System.nanoTime()

    private var sprintStartTime: Long? = null // Track when the sprint starts

    val userRef = database.getReference("sprints")
    init {
        signInAnonymously()
        fetchSprintTime(userId) {time -> sprintTime = time}
        // Start a coroutine to track time
        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                if (sprintDetected) {
                    if (sprintStartTime == null) {
                        // Start tracking time when sprinting begins
                        sprintStartTime = System.nanoTime()
                    }
                } else {
                    if (sprintStartTime != null) {
                        // Stop tracking time when sprinting ends
                        val sprintEndTime = System.nanoTime()
                        sprintTime += (sprintEndTime - sprintStartTime!!) / 1_000_000_000f // Convert to seconds
                        sprintStartTime = null
                        saveSprintTimeToFirebase(sprintTime)
                    }
                }
                delay(1000) // Update every 100ms
            }
        }
    }

    // Call this method to update sprintDetected state from sensor
    fun onSensorChanged(accelerationMagnitude: Float, x: Float) {
        if (accelerationMagnitude > 15) {
            sprintDetected = true
        } else {
            sprintDetected = false
        }
    }

    var permissionsGranted by mutableStateOf(false)

    val permissionsLauncher =
        healthConnectManager.requestPermissionsActivityContract()

    private fun checkPermissions(healthConnectManager: HealthConnectManager) {
        viewModelScope.launch {
            permissionsGranted = healthConnectManager.hasAllPermissions()
        }
    }
    private fun saveSprintTimeToFirebase(sprintTime: Float) {
        // Assuming that you want to store sprintTime in a path based on a user ID (if needed).
      // Replace with actual user ID if needed
        val sprintData = mapOf(
            "sprintTime" to sprintTime,
            "timestamp" to System.currentTimeMillis()
        )

        userRef.child(userId).setValue(sprintData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Handle success
                    println("Sprint time saved successfully!")
                } else {
                    // Handle failure
                    println("Failed to save sprint time: ${task.exception?.message}")
                }
            }
    }

    fun fetchSprintTime(userId: String, onSprintTimeFetched: (Float) -> Unit) {
        userRef.child(userId).get() // Correct the path to match `saveSprintTimeToFirebase`
            .addOnSuccessListener { snapshot ->
                val sprintTime = snapshot.child("sprintTime").getValue(Float::class.java) ?: 0f
                onSprintTimeFetched(sprintTime)
            }
            .addOnFailureListener { exception ->
                println("Error fetching sprint time: ${exception.message}")
                onSprintTimeFetched(0f) // Default to 0 if there's an error
            }
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

}
