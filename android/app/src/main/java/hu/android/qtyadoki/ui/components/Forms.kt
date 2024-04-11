package hu.android.qtyadoki.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.android.qtyadoki.R
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightBlueAlpha
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.quickSandFamily

/**
 * Component for a login form.
 * @param modifier The modifier of the component.
 * @param buttonText The text of the button.
 * @param onButtonClicked The function to be called when the button is clicked.
 * @param errorText The text to be displayed if there is an error.
 */
@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    buttonText: String = "",
    onButtonClicked: (String, String) -> Unit = { _, _ -> },
    errorText: String = ""
) {
    var email by remember {
        mutableStateOf("")
    }
    var pass by remember {
        mutableStateOf("")
    }
    Column(modifier) {
        Column {
            Text(
                text = buttonText,
                fontSize = 24.sp,
                color = LightGrey,
                fontFamily = quickSandFamily,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.size(16.dp))
            if (errorText.isNotBlank()) {
                Text(
                    text = errorText,
                    fontFamily = quickSandFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Red
                )
            }
            TextWithInput(
                text = stringResource(R.string.email), textChanged = {
                    email = it
                },
                keyboardType = KeyboardType.Email
            )
            TextWithInput(text = stringResource(R.string.pass), textChanged = {
                pass = it
            }, isPassword = true)
        }
        ElevatedButton(
            onClick = {
                onButtonClicked(email, pass)
            }, colors = ButtonDefaults.buttonColors(
                containerColor = LightBlue,
                contentColor = Color.White,
                disabledContainerColor = LightBlueAlpha
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 5.dp,
                pressedElevation = 5.dp,
                focusedElevation = 5.dp,
                hoveredElevation = 5.dp,
                disabledElevation = 0.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(top = 20.dp)
        ) {
            Text(
                text = buttonText,
                fontFamily = quickSandFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
    }
}

/**
 * Component for a registration form.
 * @param modifier The modifier of the component.
 * @param buttonText The text of the button.
 * @param onButtonClicked The function to be called when the button is clicked.
 */
@Composable
fun RegistrationForm(
    modifier: Modifier = Modifier,
    buttonText: String = "",
    onButtonClicked: (name: String, phone: String, email: String, pass: String, address: String) -> Unit = { _, _, _, _, _ -> }
) {
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var pass by remember {
        mutableStateOf("")
    }
    var passRep by remember {
        mutableStateOf("")
    }
    var phone by remember {
        mutableStateOf("")
    }
    var address by remember {
        mutableStateOf("")
    }
    val showPass = remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier)
    {
        Column {
            Text(
                text = buttonText,
                fontSize = 24.sp,
                color = LightGrey,
                fontFamily = quickSandFamily,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.size(16.dp))
            TextWithInput(text = stringResource(R.string.name), textChanged = {
                name = it
            })
            TextWithInput(text = stringResource(R.string.phone_number), textChanged = {
                phone = it
            }, keyboardType = KeyboardType.Phone)
            TextWithInput(text = stringResource(R.string.address), textChanged = {
                address = it
            })
            TextWithInput(text = stringResource(R.string.email), textChanged = {
                email = it
            }, keyboardType = KeyboardType.Email)
            TextWithInput(
                text = stringResource(R.string.pass), textChanged = {
                    pass = it
                }, isPassword = true,
                passwordVisibility = showPass
            )
            TextWithInput(
                text = stringResource(R.string.repeat_pass),
                textChanged = {
                    passRep = it
                },
                isPassword = true,
                passwordVisibility = showPass
            )
            if (pass != passRep) {
                Text(
                    text = stringResource(R.string.not_matching_passwords),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = quickSandFamily,
                    color = Color.Red
                )
            }
            ElevatedButton(
                onClick = {
                    onButtonClicked(name, phone, email, pass, address)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightBlue,
                    contentColor = Color.White,
                    disabledContainerColor = LightBlueAlpha
                ),
                enabled = (name.isNotBlank() && phone.isNotBlank() && email.isNotBlank() && pass.isNotBlank() && pass == passRep),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 5.dp,
                    focusedElevation = 5.dp,
                    hoveredElevation = 5.dp,
                    disabledElevation = 0.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 10.dp)
                    .heightIn(50.dp, 100.dp)
            ) {
                Text(
                    text = buttonText,
                    fontFamily = quickSandFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}



