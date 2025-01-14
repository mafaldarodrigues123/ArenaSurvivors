package fcul.mei.cm.app.database

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import fcul.mei.cm.app.domain.Coordinates
import fcul.mei.cm.app.utils.FirebaseConfig
import fcul.mei.cm.app.utils.ReferencePath

class CoordinatesRepository {

    private val db = Firebase.database(FirebaseConfig.DB_URL)

    private val myRef = db.getReference(ReferencePath.COORDINATES)

    private val userRepository = AlliancesRepository()

    fun saveCoordinates(userId: String, coordinates: Coordinates){
        Log.d(TAG, "Value is: $coordinates")

        myRef.child(userId).child(ReferencePath.COORDINATES).setValue(coordinates)

    }

    fun getCoordinates(userId: String, callback: (Coordinates?) -> Unit) {
        myRef.child(userId).child(ReferencePath.COORDINATES).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val coordinates = snapshot.getValue(Coordinates::class.java)
                callback(coordinates)
                Log.d(TAG, "Coordinates for $userId: $coordinates")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read coordinates for $userId.", error.toException())
                callback(null)
            }
        })
    }

    companion object {
        private const val TAG = "Coordinates"
    }
}