package fcul.mei.cm.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import fcul.mei.cm.app.database.ChatRepository

class ChatViewModel : ViewModel(){

    private val chatRepository: ChatRepository = ChatRepository()

    fun createChat( chatName: String, owner: String, description: String) {

        chatRepository.createChat(chatName,owner,description){ result ->
            if (result) Log.d("Chat","Chat was created!")
            else Log.w("Chat","Chat was not created!")
        }

    }

    fun getAllChats(){

    }

}