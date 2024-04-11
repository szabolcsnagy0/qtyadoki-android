package hu.android.qtyadoki.ui.screens.drawerscreens.appointmentsscreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import hu.android.qtyadoki.R
import hu.android.qtyadoki.ui.components.TextWithTitle
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.LightGreyAlpha
import hu.android.qtyadoki.ui.theme.quickSandFamily
import hu.android.qtyadoki.ui.viewmodels.AppointmentDetailsViewModel

/**
 * This screen shows the details of an appointment.
 *
 * @param appId The id of the appointment.
 * @param navController The navController used for navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetails(
    appId: Int,
    navController: NavController = rememberNavController()
) {
    val viewModel = remember { AppointmentDetailsViewModel(appId) }
    var openNavigation by remember { mutableStateOf(false) }
    var openPhone by remember { mutableStateOf(false) }
    val appointment by viewModel.currentAppointment

    val cameraPositionState = rememberCameraPositionState()
    var mapInitialized by remember { mutableStateOf(false) }
    val context = LocalContext.current

    /**
     * If the appointment is not null and the vetAddress is not null, then the camera position
     * will be set to the location of the vetAddress.
     */
    LaunchedEffect(appointment) {
        if (appointment != null && appointment!!.vetAddress != null) {
            val location = viewModel.getLocationOfAddress(context)
            if (location != null) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 10f)
                mapInitialized = true
            }
        }
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
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.backbutton_description),
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    stringResource(R.string.appointment_details),
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
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                TextWithTitle(
                    titleText = stringResource(id = R.string.pet_name),
                    subText = appointment?.petName ?: "",
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                TextWithTitle(
                    titleText = stringResource(R.string.vet_name),
                    subText = appointment?.vetName ?: "",
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                TextWithTitle(
                    titleText = stringResource(id = R.string.appointment),
                    subText = appointment?.getFormattedDate() ?: "",
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                TextWithTitle(
                    titleText = stringResource(id = R.string.vet_phone),
                    subText = appointment?.vetPhone ?: "",
                    modifier = Modifier
                        .clickable {
                            openPhone = true
                        }
                        .padding(bottom = 20.dp)
                )
                TextWithTitle(
                    titleText = stringResource(id = R.string.vet_email),
                    subText = appointment?.vetEmail ?: "",
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                TextWithTitle(
                    titleText = stringResource(id = R.string.location),
                    subText = appointment?.vetAddress ?: "",
                    modifier = Modifier
                        .clickable {
                            openNavigation = true
                        }
                        .padding(bottom = 20.dp)
                )
                if (!appointment?.getDescription().isNullOrBlank()) {
                    TextWithTitle(
                        titleText = stringResource(id = R.string.description),
                        subText = appointment?.getDescription() ?: "",
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }
                if(mapInitialized) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(30.dp))
                    ) {
                        GoogleMap(
                            modifier = Modifier
                                .fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            uiSettings = MapUiSettings(
                                zoomControlsEnabled = true,
                                zoomGesturesEnabled = false,
                                scrollGesturesEnabled = false,
                                tiltGesturesEnabled = false,
                                compassEnabled = false,
                                myLocationButtonEnabled = false,
                                indoorLevelPickerEnabled = false,
                                mapToolbarEnabled = false
                            ),
                        ) {
                            Marker(
                                state = MarkerState(
                                    position = cameraPositionState.position.target
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
    if (openNavigation) {
        openNavigation = false
        if (!appointment?.vetAddress.isNullOrBlank())
            OpenNavigationAppAndSearch(appointment?.vetAddress!!)
    }
    if (openPhone) {
        openPhone = false
        if (!appointment?.vetPhone.isNullOrBlank())
            OpenPhoneApp(phoneNumber = appointment?.vetPhone!!)
    }
}

/**
 * Opens the phone app and dials the given phone number.
 */
@Composable
fun OpenPhoneApp(phoneNumber: String) {
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}

/**
 * Opens the navigation app and searches for the given address.
 */
@Composable
fun OpenNavigationAppAndSearch(address: String) {
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("geo:0,0?q=$address")
    }
    context.startActivity(intent)
}