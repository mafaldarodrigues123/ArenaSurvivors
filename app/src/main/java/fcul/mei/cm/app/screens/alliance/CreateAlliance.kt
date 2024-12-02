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
import fcul.mei.cm.app.database.ChatRepository

@Composable
fun CreateAlliance(
    modifier: Modifier,
    onComplete: (Boolean) -> Unit
) {
    val chatRepository: ChatRepository = ChatRepository()
    var chatName by remember { mutableStateOf("") }
    var chatDescription by remember { mutableStateOf("") }

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

            Button(
                onClick = {
                    chatRepository.createChat(
                        chatName = chatName,
                        owner = "mafalda",
                        description = chatDescription,
                        onComplete = onComplete
                    )
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
