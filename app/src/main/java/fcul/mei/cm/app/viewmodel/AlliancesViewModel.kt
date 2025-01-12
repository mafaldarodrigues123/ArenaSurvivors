package fcul.mei.cm.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import fcul.mei.cm.app.database.AlliancesRepository

class AlliancesViewModel(
    val user: UserViewModel
) : ViewModel() {

    private val chatRepository: AlliancesRepository = AlliancesRepository()

    fun createChat(chatName: String, description: String): Boolean {
        var isSuccessful = false
        if(user.isUserAdded.value){
            user.getUserId()?.let {
                chatRepository.createChat(chatName, it, description) { result ->
                    if (result) Log.d("Chat", "Chat was created!")
                    else Log.w("Chat", "Chat was not created!")
                    isSuccessful = result
                }
            }
            return isSuccessful
        }
       return false
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