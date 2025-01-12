package fcul.mei.cm.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import fcul.mei.cm.app.database.AlliancesRepository

class AlliancesViewModel : ViewModel() {

    private val chatRepository: AlliancesRepository = AlliancesRepository()

    fun createChat(chatName: String, owner: String, description: String): Boolean {
        var isSuccessful = false
        chatRepository.createChat(chatName, owner, description) { result ->
            if (result) Log.d("Chat", "Chat was created!")
            else Log.w("Chat", "Chat was not created!")
            isSuccessful = result
        }
        return isSuccessful
    }

    fun getAllChats() = chatRepository.getAllChats()

    fun addMember(
        chatName: String,
        id: String,
        memberName: String,
        status: String,
    ){
        chatRepository.addMember(
            chatName = chatName,
            memberId = id,
            memberName = memberName,
            status = status,
            onComplete = {},
            district = 1
        )
    }

}