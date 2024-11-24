package fcul.mei.cm.app.database

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

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
            "participants" to emptyList<String>(),
            "creationTime" to System.currentTimeMillis()
        )

        db.collection("chats").document(chatName)
            .set(chatData)
            .addOnSuccessListener {
                println("Group created successfully")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                println("Error creating group: ${e.message}")
                onComplete(false)
            }
    }
}