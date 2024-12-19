package fcul.mei.cm.app.database

import com.google.firebase.Firebase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.firestore

class ChatRepository {
    private val database = com.google.firebase.ktx.Firebase.database("https://arena-survivors-e1316-default-rtdb.europe-west1.firebasedatabase.app/")

    fun createChat(
        chatName: String,
        owner: String,
        description: String,
        onComplete: (Boolean) -> Unit
    ) {
        val chatData = mapOf(
            "chatName" to chatName,
            "owner" to owner,
            "description" to description,
            "participants" to emptyList<String>(),
            "creationTime" to System.currentTimeMillis()
        )
        var chats = database.getReference("chats").get()
        // Save chat data under the "chats" node with chatName as the key
        database.getReference("chats").child(chatName)
            .setValue(chatData)
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