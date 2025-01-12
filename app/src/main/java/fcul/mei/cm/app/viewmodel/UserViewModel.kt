package fcul.mei.cm.app.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import fcul.mei.cm.app.database.UserRepository
import fcul.mei.cm.app.domain.Coordinates
import fcul.mei.cm.app.domain.User

@SuppressLint("StaticFieldLeak")
class UserViewModel(
    private val context: Context,
) : ViewModel() {

    private val userRepository = UserRepository()

    fun addUser(district: Int, name: String) {
        val userId = getUserId()
        if (userId != null) {
            Log.d(TAG, "User already added: $userId")
            return
        }

        val user = User(
            district = district,
            name = name,
            coordinates = Coordinates(),
        )

        userRepository.addUser(user) { userAdded ->
            if (userAdded) {
                saveUserId(user.id)
                Log.d(TAG, "UserAlreadyAdded")
            }
        }
    }

    fun displaySameDistrict(district: Int): User? {
        var user: User? = null
        userRepository.getAllUser { usersList ->
            user = usersList.find { it.district == district }
        }
        return user
    }

    private fun saveUserId(userId: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(KEY_USER_ID, userId)
            apply()
        }
    }

    private fun getUserId() =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                                 .getString(KEY_USER_ID, null)

    companion object {
        private const val TAG = "--User_ViewModel"
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_USER_ID = "user_id"
    }
}
