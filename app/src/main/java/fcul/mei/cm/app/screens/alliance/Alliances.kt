package fcul.mei.cm.app.screens.alliance

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Alliances(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Box(
        modifier = modifier.fillMaxSize()
    ){
        Button(
            onClick = onClick,
            modifier = Modifier.align(Alignment.Center)
        ){
            Text("Create your own alliance")
        }
    }
}