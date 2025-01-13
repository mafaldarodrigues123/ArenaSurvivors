package fcul.mei.cm.app.database

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fcul.mei.cm.app.domain.User
import fcul.mei.cm.app.utils.CollectionPath

class UserRepository {
    private val db = Firebase.firestore
    private val chatRepository = AlliancesRepository()


    private fun addParticipantToChat(
        chatName: String,
        participantId: String,
        onComplete: (Boolean) -> Unit
    ) {
        val participantData = hashMapOf(
            "id" to participantId,
            "role" to "member",
            "joinedAt" to System.currentTimeMillis()
        )

        db.collection(CollectionPath.CHATS).document(chatName)
            .collection(CollectionPath.PARTICIPANTS).document(participantId)
            .set(participantData)
            .addOnSuccessListener {
                println("Participant added successfully")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                println("Error adding participant: ${e.message}")
                onComplete(false)
            }
    }

    fun removeParticipant(
        chatName: String,
        removerId: String,
        participantId: String,
        onComplete: (Boolean) -> Unit
    ) {
        val participantRef = db.collection(CollectionPath.CHATS).document(chatName)
            .collection(CollectionPath.PARTICIPANTS).document(removerId)

        participantRef.get()
            .addOnSuccessListener { document ->
                if (document.exists() && document.getString("role") == "admin") {
                    removeParticipantFromChat(chatName, participantId, onComplete)
                } else {
                    println("Only admins can remove participants")
                    onComplete(false)
                }
            }
            .addOnFailureListener { e ->
                println("Error checking admin status: ${e.message}")
                onComplete(false)
            }
    }

    private fun removeParticipantFromChat(
        chatName: String,
        participantId: String,
        onComplete: (Boolean) -> Unit
    ) {
        db.collection(CollectionPath.CHATS).document(chatName)
            .collection(CollectionPath.PARTICIPANTS).document(participantId)
            .delete()
            .addOnSuccessListener {
                println("Participant removed successfully")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                println("Error removing participant: ${e.message}")
                onComplete(false)
            }
    }

    fun addUser(user: User, onComplete: (Boolean) -> Unit) {
        val userData = hashMapOf(
            "id" to user.id,
            "district" to user.district,
            "name" to user.name,
            "creationTime" to System.currentTimeMillis()
        )

        db.collection(CollectionPath.USERS).document(user.id)
            .set(userData)
            .addOnSuccessListener {
                println("User added successfully")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                println("Fail to add a user: ${e.message}")
                onComplete(false)
            }

        getAllUser { users ->
            val sameDistrictUser = users.firstOrNull { user.district == it.district }
            if (sameDistrictUser != null) {
                chatRepository.createChat(
                    chatName = "district ${user.district}",
                    owner = user.id,
                    description = "Same district",
                ) {}
                addParticipantToChat(
                    chatName = "district ${user.district}",
                    participantId = sameDistrictUser.id,
                ) {}
            }
        }
    }

    private fun verifyUser(userId: String, callback: (User?) -> Unit) {
        db.collection(CollectionPath.USERS)
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)?.copy(id = document.id)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun getUser(userId: String, callback: (User?) -> Unit) {
        db.collection(CollectionPath.USERS)
            .get()
            .addOnSuccessListener { result ->
                val users = result.map { document ->
                    document.toObject(User::class.java).copy(id = document.id)
                }
                callback(users.find { it.id == userId })
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun getAllUser(callback: (List<User>) -> Unit) {
        db.collection(CollectionPath.USERS)
            .get()
            .addOnSuccessListener { result ->
                val users = result.map { document ->
                    document.toObject(User::class.java).copy(id = document.id)
                }
                callback(users)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }
}