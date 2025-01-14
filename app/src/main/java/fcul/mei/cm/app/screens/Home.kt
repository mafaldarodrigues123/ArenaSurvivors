package fcul.mei.cm.app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fcul.mei.cm.app.R


@Composable
fun Home(
    modifier: Modifier = Modifier,
    onClickChatButton:() -> Unit,
    onClickHealthButton:() -> Unit,
    onClickAliacesList: () -> Unit,
    onClickUserButton: () -> Unit,
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
            onClick = onClickHealthButton
        ) {
            Image(
                painter = painterResource(id = R.drawable.health),
                contentDescription = "Button Icon",
            )
        }

        IconButton(
            modifier = modifier
                .size(92.dp)
                .align(Alignment.TopEnd)
                .padding(16.dp),
            onClick = onClickAliacesList
        ) {
            Image(
                painter = painterResource(id = R.drawable.health),
                contentDescription = "Button Icon",
            )
        }

        IconButton(
            modifier = modifier
                .size(92.dp)
                .align(Alignment.TopStart)
                .padding(16.dp),
            onClick = onClickUserButton
        ) {
            Icon(Icons.Rounded.AccountCircle, contentDescription = "Add user")
        }
    }
}
