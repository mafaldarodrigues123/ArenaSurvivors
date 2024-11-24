package fcul.mei.cm.app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fcul.mei.cm.app.R
import fcul.mei.cm.app.ui.theme.AppTheme

@Composable
fun Home(
    modifier: Modifier = Modifier,
    onClickChatButton:() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        IconButton(
            modifier = modifier
                .size(92.dp)
                .align(Alignment.BottomStart)
                .padding(16.dp),
            onClick = onClickChatButton
        ) {
            Image(
                painter = painterResource(id = R.drawable.alliance),
                contentDescription = "Button Icon",
            )
        }

        IconButton(
            modifier = modifier
                .size(92.dp)
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { }
        ) {
            Image(
                painter = painterResource(id = R.drawable.health),
                contentDescription = "Button Icon",
            )
        }
    }
}
