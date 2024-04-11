package hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen.petslist

import android.net.Uri
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import hu.android.qtyadoki.R
import hu.android.qtyadoki.ui.components.PictureWithEditIcon
import hu.android.qtyadoki.ui.components.camera.ChooseImage
import hu.android.qtyadoki.ui.components.camera.cameraAccess
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.quickSandFamily

/**
 * Dialog for adding a new pet
 * @param onDismiss: dismiss the dialog
 * @param onSuccess: the function to call when the user successfully added a new pet
 */
@Composable
fun NewPetDialog(
    onDismiss: () -> Unit = {},
    onSuccess: (Uri?, String, String) -> Unit = { _, _, _ -> }
) {
    var cameraClicked by remember {
        mutableStateOf(false)
    }

    var profileImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var petName by remember {
        mutableStateOf("")
    }

    var petSpecies by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PictureWithEditIcon(
                        onCameraClicked = {
                            cameraClicked = true
                        },
                        imageUrl = profileImageUri
                    )
                    OutlinedTextField(
                        value = petName, onValueChange = { newText ->
                            petName = newText
                        },
                        modifier = Modifier
                            .defaultMinSize(minHeight = 35.dp)
                            .background(color = Color.Transparent, shape = CircleShape)
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(),
                        label = {
                            Text(
                                text = stringResource(R.string.name),
                                color = LightBlue,
                                fontFamily = quickSandFamily,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        singleLine = true,
                        shape = CircleShape
                    )
                    OutlinedTextField(
                        value = petSpecies, onValueChange = { newText ->
                            petSpecies = newText
                        },
                        modifier = Modifier
                            .defaultMinSize(minHeight = 35.dp)
                            .background(color = Color.Transparent, shape = CircleShape)
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(),
                        label = {
                            Text(
                                text = stringResource(R.string.species),
                                color = LightBlue,
                                fontFamily = quickSandFamily,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        singleLine = true,
                        shape = CircleShape
                    )
                }
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
                            onSuccess(profileImageUri, petName, petSpecies)
                        },
                        enabled = (petName.isNotBlank() && petSpecies.isNotBlank())
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

    /**
     * If the user clicked on the camera icon, then choose an image from the gallery or take a photo
     */
    if (cameraClicked) {
        if (cameraAccess()) {
            ChooseImage(onConfirmation = { uri, _ ->
                if (uri != null) {
                    profileImageUri = uri
                }
                cameraClicked = false
            }, onDismiss = {
                cameraClicked = false
            })
        }
    }
}