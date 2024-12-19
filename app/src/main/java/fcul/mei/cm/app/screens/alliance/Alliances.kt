package fcul.mei.cm.app.screens.alliance

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Alliances(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val chatNames = remember { mutableStateOf<List<String>>(emptyList()) }

    // Fetch chat names when the Composable is first displayed
    LaunchedEffect(Unit) {
        getChatNames { names ->
            chatNames.value = names // Update the state with chat names
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Display chat names in a scrollable LazyColumn
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // To make the list fill up the available space
            ) {
                items(items = chatNames.value) { chatName -> // Iterate over the list of chat names
                    OutlinedButton(
                        onClick = { }, // Trigger onClick when a button is clicked
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = chatName)
                    }
                }
            }

            Button(
                onClick = onClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Create your own alliance")
            }
        }
    }
}