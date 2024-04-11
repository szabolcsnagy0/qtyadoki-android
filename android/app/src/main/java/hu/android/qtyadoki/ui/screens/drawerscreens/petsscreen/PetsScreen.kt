package hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import hu.android.qtyadoki.R
import hu.android.qtyadoki.ui.components.ResultToast
import hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen.petslist.ListItem
import hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen.petslist.NewPetDialog
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.LightGreyAlpha
import hu.android.qtyadoki.ui.theme.quickSandFamily
import hu.android.qtyadoki.ui.viewmodels.PetsViewModel
import java.io.File

/**
 * Composable function for the Pets screen.
 * @param viewModel: PetsViewModel object.
 * @param modifier: Modifier.
 * @param onItemClicked: Function that is called when a pet is selected. The parameter is the pet's id.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PetsScreen(
    viewModel: PetsViewModel,
    modifier: Modifier = Modifier,
    onItemClicked: (Int) -> Unit = { _ -> }
) {
    val petList by viewModel.petsList.observeAsState()

    var addPet by remember {
        mutableStateOf(false)
    }

    val opResult: MutableState<Boolean?> = remember {
        mutableStateOf(null)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing.value,
        onRefresh = viewModel::refreshPetList
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addPet = true
                },
                contentColor = Color.White,
                containerColor = LightBlue,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.add))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 25.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    stringResource(R.string.my_pets),
                    fontFamily = quickSandFamily,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LightGrey
                )
            }
            HorizontalDivider(thickness = 2.dp, color = LightGreyAlpha)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        bottom = 30.dp
                    )
                ) {
                    items(petList ?: emptyList()) { item ->
                        ListItem(data = item, onClick = {
                            onItemClicked(it)
                        },
                            onTransferAccepted = {
                                viewModel.acceptPet(it, onResponseCallback = { success ->
                                    opResult.value = success
                                })
                            },
                            onTransferRejected = {
                                viewModel.rejectPet(it, onResponseCallback = { success ->
                                    opResult.value = success
                                })
                            })
                    }
                }
                PullRefreshIndicator(
                    refreshing = viewModel.isRefreshing.value,
                    scale = true,
                    state = pullRefreshState,
                    contentColor = LightBlue,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }

    val context = LocalContext.current

    if (addPet) {
        NewPetDialog(onDismiss = {
            addPet = false
        }, onSuccess = { uri, name, species ->
            // If the user selected a picture, create a file from the uri.
            val file: File? = if (uri != null) {
                viewModel.createImageFile(uri, context)
            } else null
            // Add the pet to the database.
            viewModel.addPet(name, species) { petId ->
                if (petId != null) {
                    // If the user selected a picture, update the pet's picture.
                    if (file != null) {
                        viewModel.updatePetPicture(image = file, petId = petId) { success, _ ->
                            opResult.value = success
                        }
                    } else opResult.value = true
                } else opResult.value = false
            }
            addPet = false
        })
    }

    if (opResult.value != null) {
        ResultToast(result = opResult.value)
        opResult.value = null
    }
}