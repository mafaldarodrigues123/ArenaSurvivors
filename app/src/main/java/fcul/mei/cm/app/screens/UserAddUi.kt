package fcul.mei.cm.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fcul.mei.cm.app.viewmodel.UserViewModel

@Composable
fun AddUserScreen(
    userViewModel: UserViewModel,
    onClickUserAdded: (Boolean) -> Unit
) {
    var district by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var showConfirmation by remember { mutableStateOf(false) }

    if(userViewModel.getUserId() == null) {
        if (showConfirmation) {
            Text(
                text = "User added successfully!",
                modifier = Modifier.padding(16.dp),
                color = Color.Green,
                fontSize = 18.sp
            )
            onClickUserAdded(showConfirmation)
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = district,
                onValueChange = { district = it },
                label = { Text("District") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Button(
                onClick = {
                    if (district.isNotEmpty() && name.isNotEmpty()) {
                        userViewModel.addUser(district.toInt(), name)
                        showConfirmation = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Add User")
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tribute already registered")
        }
    }
}
