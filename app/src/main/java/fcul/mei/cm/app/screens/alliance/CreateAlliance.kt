package fcul.mei.cm.app.screens.alliance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.ktx.database
import fcul.mei.cm.app.database.ChatRepository

private val database = com.google.firebase.ktx.Firebase.database("https://arena-survivors-e1316-default-rtdb.europe-west1.firebasedatabase.app/")

@Composable
fun CreateAlliance(
    modifier: Modifier,
    onComplete: (Boolean) -> Unit
) {
    val chatRepository: ChatRepository = ChatRepository()
    var chatName by remember { mutableStateOf("") }
    var chatDescription by remember { mutableStateOf("") }


    val chatNames = remember { mutableStateOf<List<String>>(emptyList()) }
    var showWarning by remember { mutableStateOf(false) } // For displaying the warning

    // Fetch chat names when the Composable is first displayed
    LaunchedEffect(Unit) {
        getChatNames { names ->
            chatNames.value = names // Update the state with chat names
        }
    }



        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = chatName,
                onValueChange = { chatName = it },
                label = { Text("Name of Chat") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = chatDescription,
                onValueChange = { chatDescription = it },
                label = { Text("Description") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .heightIn(min = 120.dp)
            )

            if (showWarning) {
                Text(
                    text = "Chat name already exists, please choose another.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(onClick = {
                if (chatNames.value.contains(chatName)) {
                    showWarning = true // Show the warning
                } else {
                    showWarning = false
                    chatRepository.createChat(
                        chatName = chatName,
                        owner = "mafalda",
                        description = chatDescription,
                        onComplete = onComplete
                    )
                }
            },
                modifier = Modifier.fillMaxWidth(),
                enabled = chatName.isNotBlank() && chatDescription.isNotBlank()
            ) {
                Text(
                    text = "Create Group",
                    fontSize = 18.sp
                )
            }
    }

}

fun getChatNames(onResult: (List<String>) -> Unit) {
    val chatsRef = database.getReference("chats")

    chatsRef.get()
        .addOnSuccessListener { dataSnapshot ->
            val chatNames = dataSnapshot.children.mapNotNull { it.key }
            onResult(chatNames) // Pass the result to the callback
        }
        .addOnFailureListener { e ->
            println("Error retrieving chats: ${e.message}")
            onResult(emptyList()) // Return an empty list in case of an error
        }
}