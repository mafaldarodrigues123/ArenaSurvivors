package fcul.mei.cm.app.screens

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.credentials.GetCredentialRequest
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import fcul.mei.cm.app.R
import fcul.mei.cm.app.ui.theme.AppTheme
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit


@Composable
fun Home(
    modifier: Modifier = Modifier,
    onClickChatButton:() -> Unit,
    onClickHealthButton:() -> Unit,
    onClickAliacesList: () -> Unit,
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
    }
}
