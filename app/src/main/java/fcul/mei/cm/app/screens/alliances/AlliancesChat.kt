package fcul.mei.cm.app.screens.alliances

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import fcul.mei.cm.app.database.AlliancesRepository
import fcul.mei.cm.app.domain.Alliances
import fcul.mei.cm.app.domain.User
import fcul.mei.cm.app.viewmodel.AlliancesViewModel


// TODO quando nao tiver mensagens deve aparecer "no messages yet, start talking" ou alguma coisa assim
// TODO enviar a mensagem
// TODO quando o owner manda mensagem aparecer owner

@Composable
fun ChatTemplate(
    viewModel: AlliancesViewModel,
    modifier: Modifier = Modifier
) {
    val messages = remember { mutableStateListOf<String>() }
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var isPanelVisible by remember { mutableStateOf(false) } // Controla a visibilidade da janela deslizante
    var a by remember { mutableStateOf<List<User>>(emptyList()) }
    LaunchedEffect (Unit){
        AlliancesRepository().getAllMembers("abcdefghij").collect{
            a = it
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true
            ) {
                items(messages.size) { index ->
                    MessageBubble(message = messages[messages.size - 1 - index])
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
                            Text("Type a message...", color = Color.Gray)
                        }
                        innerTextField()
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (inputText.text.isNotBlank()) {
                        messages.add(inputText.text)
                        inputText = TextFieldValue("")
                    }
                }) {
                    Text("Send")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão para abrir a janela deslizante
            Button(onClick = { isPanelVisible = true }) {
                Text("Abrir Janela")
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
                    .background(Color.LightGray, shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .align(Alignment.CenterEnd) // Garante que a janela fique à direita
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
                        items(a) { group ->
                            UsersCard(group)
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
                    text = "Name: ${user.name}",
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
                Text("Request to join")
            }
        }
    }
}

