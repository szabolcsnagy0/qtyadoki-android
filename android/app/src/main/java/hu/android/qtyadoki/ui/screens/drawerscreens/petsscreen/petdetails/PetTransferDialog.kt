package hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen.petdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import hu.android.qtyadoki.R
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.quickSandFamily

/**
 * PetTransferDialog is a dialog that appears when the user wants to transfer a pet to another user.
 * @param onDismiss - function that is called when the dialog is dismissed.
 * @param onSuccess - function that is called when the user clicks on the save button. The parameter is the email of the user.
 */
@Composable
fun PetTransferDialog(
    onDismiss: () -> Unit = {},
    onSuccess: (String) -> Unit = { _ -> }
) {
    var email by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.transfer_pet),
                    fontFamily = quickSandFamily,
                    fontWeight = FontWeight.Bold,
                    color = LightGrey,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 20.dp)
                )
                OutlinedTextField(
                    value = email, onValueChange = { newText ->
                        email = newText
                    },
                    modifier = Modifier
                        .defaultMinSize(minHeight = 35.dp)
                        .background(color = Color.Transparent, shape = CircleShape)
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.email_placeholder),
                            color = LightBlue,
                            fontFamily = quickSandFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    singleLine = true,
                    shape = CircleShape,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = onDismiss,
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            fontFamily = quickSandFamily,
                            fontWeight = FontWeight.Normal,
                            color = LightGrey,
                            fontSize = 18.sp
                        )
                    }
                    TextButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            onSuccess(email)
                        },
                        enabled = (email.isNotBlank())
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            fontFamily = quickSandFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}