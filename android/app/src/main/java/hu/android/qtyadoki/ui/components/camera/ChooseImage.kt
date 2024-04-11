package hu.android.qtyadoki.ui.components.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import hu.android.qtyadoki.R
import hu.android.qtyadoki.ui.theme.quickSandFamily

/**
 * Composable function to choose an image from the gallery or take a photo with the camera.
 * @param onConfirmation: (uri: Uri?,  path: String?) -> Unit - callback function to be called when an image is selected
 * @param onDismiss: () -> Unit - callback function to be called when the dialog is dismissed
 */
@Composable
fun ChooseImage(
    onConfirmation: (uri: Uri?,  path: String?) -> Unit,
    onDismiss: () -> Unit,
) {
    var hasImage by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var imagePath by remember {
        mutableStateOf<String?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            hasImage = uri != null
            imageUri = uri
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )

    val context = LocalContext.current


    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextButton(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        imagePicker.launch("image/*")
                    },
                ) {
                    Text(
                        text = stringResource(R.string.select_image),
                        fontFamily = quickSandFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
                TextButton(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        val (uri, path) = CameraFileProvider.getImageUriAndPath(context)
                        imageUri = uri
                        imagePath = path
                        cameraLauncher.launch(uri)
                    },
                ) {
                    Text(
                        text = stringResource(R.string.take_photo),
                        fontFamily = quickSandFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }

    if (hasImage && imageUri != null) {
        onConfirmation(imageUri, imagePath)
    }
}