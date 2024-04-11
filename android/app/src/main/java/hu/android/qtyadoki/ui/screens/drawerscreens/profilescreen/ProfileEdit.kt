package hu.android.qtyadoki.ui.screens.drawerscreens.profilescreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.android.qtyadoki.R
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.ui.components.EditTextWithTitle
import hu.android.qtyadoki.ui.components.PictureWithEditIcon
import hu.android.qtyadoki.ui.components.camera.ChooseImage
import hu.android.qtyadoki.ui.components.camera.cameraAccess
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightGreyAlpha
import hu.android.qtyadoki.ui.viewmodels.ProfileEditViewModel
import java.io.File

/**
 * Composable function for the ProfileEdit screen.
 * @param viewModel: ProfileEditViewModel which holds the data.
 * @param navController: NavController for navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEdit(
    viewModel: ProfileEditViewModel = remember {
        ProfileEditViewModel()
    },
    navController: NavController
) {
    var name by remember { viewModel.name }
    var phone by remember { viewModel.phone }
    var address by remember { viewModel.address }

    val context = LocalContext.current

    var cameraClicked by remember {
        mutableStateOf(false)
    }

    var imageUrl by remember {
        mutableStateOf(ApiService.getGlideURLProfile())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = stringResource(id = R.string.cancel_edit),
                            tint = LightBlue
                        )
                    }
                }, actions = {
                    IconButton(
                        onClick = {
                            viewModel.updateUserData { success, message ->
                                if (message.isNotBlank()) {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                                if (success) {
                                    navController.popBackStack()
                                }
                            }
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = stringResource(id = R.string.save_edit),
                            tint = LightBlue
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 25.dp)
                .fillMaxWidth()
        ) {
            PictureWithEditIcon(
                onCameraClicked = {
                    cameraClicked = true
                },
                imageUrl = imageUrl
            )
            OutlinedTextField(
                value = name,
                onValueChange = { newText ->
                    name = newText
                },
                modifier = Modifier
                    .defaultMinSize(minHeight = 35.dp)
                    .background(color = Color.Transparent, shape = CircleShape)
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                singleLine = true,
                shape = CircleShape
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp),
                thickness = 2.dp,
                color = LightGreyAlpha
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                EditTextWithTitle(
                    titleText = stringResource(id = R.string.phone_number),
                    prevText = phone,
                    onValueChange = {
                        phone = it
                    },
                    modifier = Modifier.padding(bottom = 40.dp, top = 20.dp)
                )
                EditTextWithTitle(
                    titleText = stringResource(id = R.string.address),
                    prevText = address,
                    onValueChange = {
                        address = it
                    },
                    modifier = Modifier.padding(bottom = 40.dp)
                )
            }
        }
    }

    if (cameraClicked) {
        if (cameraAccess()) {
            ChooseImage(onConfirmation = { uri, path ->
                // Create file from uri or path
                val file: File? = if (uri != null) {
                    viewModel.createImageFile(uri, context)
                } else if (path != null) {
                    File(path)
                } else null
                // Update user picture
                imageUrl = null
                viewModel.updateUserPicture(image = file) { _, message ->
                    if (message.isNotBlank()) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                    imageUrl = ApiService.getGlideURLProfile()
                }
                cameraClicked = false
            }, onDismiss = {
                cameraClicked = false
            })
        }
    }

}