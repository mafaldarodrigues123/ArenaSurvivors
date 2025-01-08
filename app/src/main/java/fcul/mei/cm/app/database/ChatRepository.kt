package fcul.mei.cm.app.database

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fcul.mei.cm.app.domain.Alliances
import fcul.mei.cm.app.domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

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

    fun getAllChats() = flow {
        val list = listOf(Alliances(), Alliances())
        val list1 = listOf(Alliances(), Alliances(), Alliances(), Alliances(chatName = "AAAAAA"))
        emit(list)
        kotlinx.coroutines.delay(5000)
        emit(list1)
    }
//        db.collection("chats")
//            .get()
//            .addOnSuccessListener { result ->
//                var a = result.map { document ->
//                        document.toObject(Alliances::class.java)
//                    }
//
//            }
//            .addOnFailureListener {
//            }
//        }
}