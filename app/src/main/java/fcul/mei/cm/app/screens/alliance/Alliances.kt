package fcul.mei.cm.app.screens.alliance

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Alliances(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Box(
        modifier = modifier
    ){
        Button(
            onClick = onClick
        ){
            Text("Create your own alliance")
        }
    }
}