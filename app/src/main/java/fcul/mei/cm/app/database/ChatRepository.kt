package fcul.mei.cm.app.database

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fcul.mei.cm.app.domain.User

//TODO meter no chat automatico tudo owner
class ChatRepository{
    val db = Firebase.firestore

    fun createChat(
        chatName: String,
        owner: String,
        description: String,
        onComplete: (Boolean) -> Unit
    ) {
        val chatData = hashMapOf(
            "chatName" to chatName,
            "owner" to owner,
            "description" to description,
            "creationTime" to System.currentTimeMillis()
        )

        db.collection("chats").document(chatName)
            .set(chatData)
            .addOnSuccessListener {
                val adminData = hashMapOf(
                    "id" to owner,
                    "role" to "admin",
                    "joinedAt" to System.currentTimeMillis()
                )

                db.collection("chats").document(chatName)
                    .collection("participants").document(owner)
                    .set(adminData)
                    .addOnSuccessListener {
                        println("Chat and admin participant created successfully")
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        println("Error adding admin participant: ${e.message}")
                        onComplete(false)
                    }
            }
            .addOnFailureListener { e ->
                println("Error creating chat: ${e.message}")
                onComplete(false)
            }
    }

    private fun getAllChats(callback: (List<User>) -> Unit) {
        db.collection("users")
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