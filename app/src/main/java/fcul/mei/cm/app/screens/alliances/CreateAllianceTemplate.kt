package fcul.mei.cm.app.screens.alliances

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fcul.mei.cm.app.viewmodel.AlliancesViewModel


//TODO ir buscar o user e ser o owner e fazer a navigation
// TODO se ele criar uma coisa com um nome que ja existe devia aparecer um popup ou assim

@Composable
fun CreateAllianceTemplate(
    viewModel: AlliancesViewModel,
    modifier: Modifier,
    onClickCreateAlliance: (Boolean) -> Unit
) {
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

            // todo mandar o owner id
            Button(
                onClick = {
                    val result = viewModel.createChat(
                        chatName = chatName,
                        //owner = "mafalda",
                        description = chatDescription,
                    )
                    viewModel.addMember(
                        chatName,
                        "123456",
                        "Joao",
                        "entered"
                    )
                    viewModel.addMember(
                        chatName,
                        "345678",
                        "Maria",
                        "entered"
                    )
                    viewModel.addMember(
                        chatName,
                        "2345678",
                        "Catarina",
                        "entered"
                    )
                    onClickCreateAlliance(result)
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
