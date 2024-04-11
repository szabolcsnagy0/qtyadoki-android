package hu.android.qtyadoki.ui.screens.drawerscreens.appointmentsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.collectAsState
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
import hu.android.qtyadoki.R
import hu.android.qtyadoki.ui.components.ResultToast
import hu.android.qtyadoki.ui.screens.drawerscreens.appointmentsscreen.appointmentslist.AppointmentsList
import hu.android.qtyadoki.ui.screens.drawerscreens.appointmentsscreen.appointmentslist.NewAppointmentDialog
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.LightGreyAlpha
import hu.android.qtyadoki.ui.theme.quickSandFamily
import hu.android.qtyadoki.ui.viewmodels.AppointmentsViewModel

/**
 * Composable function for the Appointments screen.
 * @param viewModel: AppointmentsViewModel for the screen.
 * @param modifier: Modifier for the screen.
 * @param onItemClicked: Function for the item click.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppointmentsScreen(
    viewModel: AppointmentsViewModel,
    modifier: Modifier = Modifier,
    onItemClicked: (Int) -> Unit = { _ -> }
) {
    val appointmentsList by viewModel.appointmentsList.collectAsState()

    var addAppointment by remember {
        mutableStateOf(false)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing.value,
        onRefresh = viewModel::refreshAppointmentsList
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addAppointment = true
                },
                contentColor = Color.White,
                containerColor = LightBlue,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(id = R.string.add))
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
                    stringResource(R.string.appointments),
                    fontFamily = quickSandFamily,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LightGrey
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                thickness = 2.dp,
                color = LightGreyAlpha
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                AppointmentsList(list = appointmentsList, onItemClicked = {
                    onItemClicked(it)
                })
                PullRefreshIndicator(
                    refreshing = viewModel.isRefreshing.value,
                    scale = true,
                    state = pullRefreshState,
                    contentColor = LightBlue,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }

        var addResult by remember<MutableState<Boolean?>> {
            mutableStateOf(null)
        }

        if (addAppointment) {
            viewModel.clearSelectedData()
            NewAppointmentDialog(viewModel = viewModel, onDismiss = {
                addAppointment = false
            },
                onSuccess = {
                    addAppointment = false
                    viewModel.addNewAppointment(onResult = { result ->
                        addResult = result
                    })
                })
        }

        if (addResult != null) {
            ResultToast(result = addResult)
            addResult = null
        }
    }
}