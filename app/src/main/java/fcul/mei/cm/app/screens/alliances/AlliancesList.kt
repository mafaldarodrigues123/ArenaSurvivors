package fcul.mei.cm.app.screens.alliances

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fcul.mei.cm.app.domain.Alliances
import fcul.mei.cm.app.viewmodel.ChatViewModel

@Composable
fun AlliancesList(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel,
    onCreateAllianceClick: () -> Unit
) {
    var a by remember { mutableStateOf<List<Alliances>>(emptyList()) }
    LaunchedEffect (Unit){
        viewModel.getAllChats().collect{
            a = it
        }
    }

    Column (
        modifier = modifier
    ) {
        Button(
            onClick = onCreateAllianceClick
        ) {
            Text("Create Alliance")
        }

        LazyColumn {
            items(a) { group ->
                GroupCard(group = group)
            }
        }
    }


}

@Composable
fun GroupCard(group: Alliances) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Name: ${group.chatName}",
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Description: ${group.description}",
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "owner: ${group.owner}",
                    color = Color.Gray
                )
            }

            Button(
              //  modifier = Modifier.align(Alignment.End),
                onClick = {}
            ) {
                Text("Request to join")
            }
        }

    }
}
