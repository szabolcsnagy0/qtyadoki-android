package hu.android.qtyadoki.ui.screens.welcomescreen

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.android.qtyadoki.R
import hu.android.qtyadoki.ui.ScreenRoutes
import hu.android.qtyadoki.ui.components.LoginForm
import hu.android.qtyadoki.ui.viewmodels.AuthenticationViewModel

/**
 * Composable function for the LoginScreen.
 * @param navController NavController for navigating between screens.
 * @param viewModel: AuthenticationViewModel for handling the login.
 */
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthenticationViewModel
) {
    var errorText by remember { mutableStateOf("") }

    val loggedIn by viewModel.isLoggedIn.observeAsState()
    val loginError by viewModel.loginError.observeAsState(false)

    WelcomeScreen(
        inputForm = {
            LoginForm(
                buttonText = stringResource(id = R.string.login_text),
                onButtonClicked = { email, pass ->
                    viewModel.loginUser(email, pass)
                },
                errorText = errorText,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        },
        onTextButtonClicked = {
            navController.navigate(ScreenRoutes.RegistrationScreen.route) {
                popUpTo(ScreenRoutes.RegistrationScreen.route) {
                    inclusive = true
                }
            }
        },
        smallText = stringResource(R.string.no_account),
        textButtonText = stringResource(R.string.registration)
    )

    var onLoginScreen by remember {
        mutableStateOf(true)
    }

    if (loggedIn == true && onLoginScreen) {
        onLoginScreen = false
        Toast.makeText(
            LocalContext.current,
            viewModel.loginText.value ?: stringResource(id = R.string.login_successful),
            Toast.LENGTH_SHORT
        ).show()
        navController.navigate(ScreenRoutes.Menu.route) {
            popUpTo(ScreenRoutes.Authentication.route) {
                inclusive = true
            }
        }
    }

    errorText = if (loginError == true) {
        stringResource(R.string.login_failed)
    } else ""
}