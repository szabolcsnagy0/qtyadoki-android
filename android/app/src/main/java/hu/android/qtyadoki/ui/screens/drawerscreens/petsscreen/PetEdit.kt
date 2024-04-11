package hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hu.android.qtyadoki.R
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.ui.components.PictureWithEditIcon
import hu.android.qtyadoki.ui.components.camera.ChooseImage
import hu.android.qtyadoki.ui.components.camera.cameraAccess
import hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen.petdetails.MedicationListItem
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.LightGreyAlpha
import hu.android.qtyadoki.ui.theme.quickSandFamily
import hu.android.qtyadoki.ui.viewmodels.PetDetailsViewModel
import java.io.File

/**
 * Screen for editing a pet.
 * @param viewModel: PetDetailsViewModel for the screen.
 * @param navController: NavController for the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetEdit(
    viewModel: PetDetailsViewModel,
    navController: NavController = rememberNavController()
) {

    val petModel by viewModel.petModel.observeAsState()
    val medications by viewModel.medicationList.observeAsState()

    val name = remember { mutableStateOf(petModel?.petName) }

    var cameraClicked by remember {
        mutableStateOf(false)
    }

    /**
     * Image url for the pet.
     */
    var imageUrl by remember {
        mutableStateOf(ApiService.getGlideURLPet(petModel?.petId))
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
                            contentDescription = stringResource(R.string.cancel_edit),
                            tint = LightBlue
                        )
                    }
                }, actions = {
                    IconButton(
                        onClick = {
                            if (name.value != null && name.value != petModel?.petName) {
                                petModel?.petName = name.value!!
                            }
                            viewModel.saveChanges()
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = stringResource(R.string.save_edit),
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
            Text(
                text = "id: ${petModel?.petId}",
                fontFamily = quickSandFamily,
                fontWeight = FontWeight.Light,
                color = LightGrey,
                fontSize = 23.sp,
            )
            OutlinedTextField(
                value = name.value ?: "",
                onValueChange = { newText ->
                    name.value = newText
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.species) + ": ",
                    fontFamily = quickSandFamily,
                    fontWeight = FontWeight.Bold,
                    color = LightGrey,
                    fontSize = 23.sp,
                    modifier = Modifier.padding(end = 15.dp)
                )
                Text(
                    text = "${petModel?.species ?: 0}",
                    fontFamily = quickSandFamily,
                    fontWeight = FontWeight.Light,
                    color = LightGrey,
                    fontSize = 20.sp,
                )
            }
            Text(
                text = stringResource(R.string.medication) + ": ",
                fontFamily = quickSandFamily,
                fontWeight = FontWeight.Bold,
                color = LightGrey,
                fontSize = 23.sp,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            )
            if (medications == null || medications?.isEmpty() == true) {
                Text(
                    text = stringResource(R.string.no_medication),
                    fontFamily = quickSandFamily,
                    fontWeight = FontWeight.Light,
                    color = LightGrey,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
                    items(medications ?: emptyList()) { item ->
                        MedicationListItem(item = item)
                    }
                }
            }
        }
    }

    val context = LocalContext.current

    if (cameraClicked) {
        if (cameraAccess()) {
            ChooseImage(onConfirmation = { uri, path ->
                // File for the image
                val file: File? = if (uri != null) {
                    viewModel.createImageFile(uri, context)
                } else if (path != null) {
                    File(path)
                } else null
                imageUrl = null
                if (petModel != null)
                    viewModel.updatePetPicture(
                        image = file,
                        petId = petModel!!.petId
                    ) { _, message ->
                        if (message.isNotBlank()) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        imageUrl = ApiService.getGlideURLPet(petModel?.petId)
                    }
                cameraClicked = false
            }, onDismiss = {
                cameraClicked = false
            })
        }
    }
}