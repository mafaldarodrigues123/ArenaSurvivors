package fcul.mei.cm.app.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fcul.mei.cm.app.domain.Alliances

@Composable
fun AlliancesList(groups: List<Alliances>) {
    LazyColumn {
        items(groups) { group ->
            GroupCard(group = group)
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Name: ${group.groupName}",
                //style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Description: ${group.description}",
                //style = MaterialTheme.typography.body1,
                color = Color.Gray
            )
        }
    }
}
