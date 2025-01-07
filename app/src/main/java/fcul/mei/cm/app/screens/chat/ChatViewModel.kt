package fcul.mei.cm.app.screens.chat

import android.util.Log
import fcul.mei.cm.app.database.ChatRepository

class ChatViewModel {

    private val chatRepository: ChatRepository = ChatRepository()

    fun createChat( chatName: String, owner: String, description: String) {

        chatRepository.createChat(chatName,owner,description){ result ->
            if (result) Log.d("Chat","Chat was created!")
            else Log.w("Chat","Chat was not created!")
        }

    }

}