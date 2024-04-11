package hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen.petdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.signature.MediaStoreSignature
import hu.android.qtyadoki.R
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.data.PetModel
import hu.android.qtyadoki.ui.ScreenRoutes
import hu.android.qtyadoki.ui.components.ResultToast
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.LightGreyAlpha
import hu.android.qtyadoki.ui.theme.quickSandFamily
import hu.android.qtyadoki.ui.viewmodels.PetDetailsViewModel

/**
 * Composable function for the PetDetails screen.
 * @param navController: NavController
 * @param viewModel: PetDetailsViewModel
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun PetDetails(
    navController: NavController,
    viewModel: PetDetailsViewModel
) {
    val dogModel by viewModel.petModel.observeAsState()

    val medications by viewModel.medicationList.observeAsState()

    var showTransferDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(ScreenRoutes.Menu.route) {
                                popUpTo(ScreenRoutes.Menu.route) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.backbutton_description),
                            tint = LightBlue
                        )
                    }
                }, actions = {
                    if (dogModel?.state == PetModel.State.IDLE) {
                        IconButton(onClick = {
                            showTransferDialog = true
                        }) {
                            Icon(
                                Icons.AutoMirrored.Rounded.Send,
                                contentDescription = stringResource(R.string.transfer_description),
                                tint = LightBlue

                            )
                        }
                        IconButton(
                            onClick = {
                                navController.navigate(ScreenRoutes.PetsEditScreen.route)
                            }
                        ) {
                            Icon(
                                Icons.Rounded.Edit,
                                contentDescription = stringResource(R.string.edit_description),
                                tint = LightBlue
                            )
                        }
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
            GlideImage(
                model = ApiService.getGlideURLPet(dogModel?.petId),
                loading = placeholder(R.drawable.dog_image_placeholder),
                contentDescription = stringResource(id = R.string.profile_picture),
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop,
            ) {
                it.signature(MediaStoreSignature("image/jpeg", System.currentTimeMillis(), 0))
            }
            Text(
                text = dogModel?.petName ?: "",
                fontFamily = quickSandFamily,
                fontWeight = FontWeight.SemiBold,
                color = LightGrey,
                fontSize = 28.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
            )
            Text(
                text = "id: ${dogModel?.petId}",
                fontFamily = quickSandFamily,
                fontWeight = FontWeight.Light,
                color = LightGrey,
                fontSize = 20.sp,
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
                    text = "${dogModel?.species ?: 0}",
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

    /**
     * When the user wants to transfer the pet to another user, this dialog will appear.
     */
    if (showTransferDialog) {
        PetTransferDialog(
            onDismiss = {
                showTransferDialog = false
            },
            onSuccess = { email ->
                viewModel.transferPet(email)
                showTransferDialog = false
            }
        )
    }

    val transferSuccess by viewModel.transferSuccess.observeAsState()
    val updateSuccess by viewModel.updateSuccess.observeAsState()

    /**
     * If the transfer was successful, the user will be navigated back to the menu screen.
     * Otherwise, a toast will appear.
     */
    if (transferSuccess != null) {
        if (transferSuccess == true) {
            navController.navigate(ScreenRoutes.Menu.route) {
                popUpTo(ScreenRoutes.Menu.route) {
                    inclusive = true
                }
            }
        } else {
            ResultToast(result = false)
        }
        viewModel.transferSuccess.value = null
    }

    if (updateSuccess != null) {
        ResultToast(result = updateSuccess)
        viewModel.updateSuccess.value = null
    }
}