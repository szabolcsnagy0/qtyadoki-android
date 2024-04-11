package hu.android.qtyadoki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.api.TokenManager
import hu.android.qtyadoki.ui.Navigation
import hu.android.qtyadoki.ui.theme.QtyaDokiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val tokenManager = TokenManager(applicationContext)
        ApiService.tokenManager = tokenManager
        setContent {
            QtyaDokiTheme {
                Navigation(tokenManager)
            }
        }
    }
}