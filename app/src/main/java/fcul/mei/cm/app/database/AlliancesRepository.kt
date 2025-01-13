package fcul.mei.cm.app.database

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fcul.mei.cm.app.domain.Alliances
import fcul.mei.cm.app.domain.User
import fcul.mei.cm.app.utils.CollectionPath
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

//TODO meter no chat automatico tudo owner
class AlliancesRepository {
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
                    "id" to "12222111",
                    "district" to 1,
                    "role" to "member",
                    "name" to "ines",
                    "status" to "entered",
                    "joinedAt" to System.currentTimeMillis()
                )


                db.collection(CollectionPath.CHATS).document(chatName)
                    .collection(CollectionPath.PARTICIPANTS).document(owner)
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

    fun getAllChats(): Flow<List<Alliances>> = flow {
        try {
            val initialList = db.collection(CollectionPath.CHATS)
                .get()
                .await()
                .map { document -> document.toObject(Alliances::class.java) }
            emit(initialList)
        } catch (e: Exception) {
            emit(emptyList())
            e.printStackTrace()
        }
    }

    fun getAllMembers(chatName: String) = flow {
        try {
            val members = db.collection(CollectionPath.CHATS).document(chatName)
                .collection(CollectionPath.PARTICIPANTS)
                .get()
                .await()
                .map { document -> document.toObject(User::class.java)  }
            emit(members)
        } catch (e: Exception) {
            emit(emptyList())
            e.printStackTrace()
        }
    }

    fun addMember(
        chatName: String,
        memberId: String,
        memberName: String,
        district: Int,
        status: String = "pending",
        onComplete: (Boolean) -> Unit
    ) {
        val memberData = hashMapOf(
            "id" to memberId,
            "district" to district,
            "role" to "member",
            "name" to memberName,
            "status" to status,
            "joinedAt" to System.currentTimeMillis()
        )

        db.collection(CollectionPath.CHATS).document(chatName)
            .collection(CollectionPath.PARTICIPANTS).document(memberId)
            .set(memberData)
            .addOnSuccessListener {
                Log.d("ALLIANCES", "Member added successfully with status: $status")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.d("ALLIANCES", "Error adding member: ${e.message}")
                onComplete(false)
            }
    }
}