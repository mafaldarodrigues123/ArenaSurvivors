package fcul.mei.cm.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import fcul.mei.cm.app.ui.theme.AppTheme
import fcul.mei.cm.app.ui.theme.arenaMap.ArenaMapUi
import fcul.mei.cm.app.ui.theme.arenaMap.ArenaMapWithSendCoordinates

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navHostController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                            title = {
                                Text(
                                    text = "Arena Survivors",
                                    color = Color.White,
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { navHostController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Menu Icon",
                                        tint = Color(0xFFFFA726)
                                    )
                                }
                            },
                        )
                    },
                ) { innerPadding ->
                    UiNav(
                        modifier = Modifier.padding(innerPadding),
                        navController = navHostController
                    )
                    ArenaMapWithSendCoordinates()
                }
            }
        }
    }
}