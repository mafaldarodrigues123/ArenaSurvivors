package fcul.mei.cm.app.screens.alliances

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fcul.mei.cm.app.R
import fcul.mei.cm.app.database.AlliancesRepository
import fcul.mei.cm.app.domain.Message
import fcul.mei.cm.app.domain.User
import fcul.mei.cm.app.utils.CollectionPath
import fcul.mei.cm.app.viewmodel.AlliancesViewModel


// TODO quando o owner manda mensagem aparecer owner

@Composable
fun ChatTemplate(
    viewModel: AlliancesViewModel,
    allianceId: String,
    modifier: Modifier = Modifier
) {
    val messages = remember { mutableStateListOf<Message>() }
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var isPanelVisible by remember { mutableStateOf(false) } // Controla a visibilidade da janela deslizante
    var members by remember { mutableStateOf<List<User>>(emptyList()) }
    val db = Firebase.firestore

    LaunchedEffect(Unit) {
        //todo remove hard coded chat name
        AlliancesRepository().getAllMembers("1").collect {
            Log.d("FETCHING MEMBERS", "$it")
            members = it
        }
    }

    LaunchedEffect(Unit) {
        val chatRef = db.collection(CollectionPath.ALLIANCES).document(allianceId).collection(CollectionPath.CHAT)

        chatRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("FireStore", "ERROR: ${error.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val updatedMessages = snapshot.documents.mapNotNull { document ->
                    document.toObject(Message::class.java)
                }
                messages.clear()
                messages.addAll(updatedMessages.sortedBy { it.timestamp })
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.start_talking), color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    reverseLayout = true
                ) {
                    items(messages.sortedByDescending { it.timestamp }) { message ->
                        MessageBubble(message = message.text)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .background(Color(0xFFF0F0F0))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        if (inputText.text.isEmpty()) {
                            Text(stringResource(R.string.type_message), color = Color.Gray)
                        }
                        innerTextField()
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (inputText.text.isNotBlank()) {
                        viewModel.sendMessageToFireStore("1", inputText.text)
                        inputText = TextFieldValue("")
                    }
                }) {
                    Text(stringResource(R.string.send_message))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão para abrir a janela deslizante
            Button(onClick = { isPanelVisible = true }) {
                Text(stringResource(R.string.open_window))
            }
        }

        // Janela deslizante
        AnimatedVisibility(
            visible = isPanelVisible,
            enter = slideInHorizontally(initialOffsetX = { it }), // Entra pela direita
            exit = slideOutHorizontally(targetOffsetX = { it })  // Sai pela direita
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .background(
                        Color.LightGray,
                        shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                    )
                    .align(Alignment.CenterEnd)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Ícone "X" no canto superior direito
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Text("X")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn {
                        items(members) { member ->
                            UsersCard(member)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = message,
            modifier = Modifier
                .padding(8.dp)
                .background(Color(0xFFDCF8C6))
                .padding(16.dp),
            color = Color.Black
        )
    }
}

@Composable
fun UsersCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.name) + {user.name},
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Description: ${user.district}",
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Owner: ${user.status}",
                    color = Color.Gray
                )
            }

            Button(
                onClick = { /* Ação do botão */ }
            ) {
                Text(stringResource(R.string.request_to_join))
            }
        }
    }
}

