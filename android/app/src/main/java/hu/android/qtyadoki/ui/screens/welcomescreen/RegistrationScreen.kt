package hu.android.qtyadoki.ui.screens.welcomescreen

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.android.qtyadoki.R
import hu.android.qtyadoki.data.OwnerRegistrationModel
import hu.android.qtyadoki.ui.ScreenRoutes
import hu.android.qtyadoki.ui.components.RegistrationForm
import hu.android.qtyadoki.ui.viewmodels.RegistrationViewModel

/**
 * Composable function which displays the registration screen.
 * @param navController NavController which handles the navigation between screens.
 * @param viewModel RegistrationViewModel which handles the registration process.
 */
@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = remember {
        RegistrationViewModel()
    }
) {
    val regResult = remember {
        mutableStateOf("")
    }

    val regSuccess = remember {
        mutableStateOf(false)
    }

    val user = remember {
        mutableStateOf(OwnerRegistrationModel("", "", "", "", ""))
    }

    WelcomeScreen(
        inputForm = {
            RegistrationForm(
                buttonText = stringResource(id = R.string.registration),
                onButtonClicked = { name, phone, email, pass, address ->
                    user.value = OwnerRegistrationModel(
                        name = name,
                        phone = phone,
                        email = email,
                        password = pass,
                        address = address
                    )
                    viewModel.registerUser(
                        user.value,
                        regResult,
                        regSuccess
                    )
                },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        },
        onTextButtonClicked = {
            navController.navigate(ScreenRoutes.LoginScreen.route) {
                popUpTo(ScreenRoutes.LoginScreen.route) {
                    inclusive = true
                }
            }
        },
        smallText = stringResource(R.string.has_account),
        textButtonText = stringResource(id = R.string.login_text),
        imageWeight = 0.3f
    )


    if (regResult.value.isNotBlank()) {
        Toast.makeText(LocalContext.current, regResult.value, Toast.LENGTH_SHORT).show()
        regResult.value = ""
    }

    if (regSuccess.value) {
        navController.navigate(ScreenRoutes.LoginScreen.route) {
            popUpTo(ScreenRoutes.LoginScreen.route)
        }
    }
}