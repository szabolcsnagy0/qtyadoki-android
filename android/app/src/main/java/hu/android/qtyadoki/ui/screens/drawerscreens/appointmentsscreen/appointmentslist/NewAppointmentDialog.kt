package hu.android.qtyadoki.ui.screens.drawerscreens.appointmentsscreen.appointmentslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.window.Dialog
import hu.android.qtyadoki.R
import hu.android.qtyadoki.data.PetModel
import hu.android.qtyadoki.data.SelectableAppointment
import hu.android.qtyadoki.data.VetModel
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.quickSandFamily
import hu.android.qtyadoki.ui.viewmodels.AppointmentsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Dialog for creating new appointment.
 * @param viewModel: AppointmentsViewModel for getting data.
 * @param onDismiss: () -> Unit function for dismissing dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAppointmentDialog(
    viewModel: AppointmentsViewModel,
    onDismiss: () -> Unit = {}, onSuccess: () -> Unit = {}
) {
    viewModel.initDialogOptions()

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

    val vets by viewModel.vetList.collectAsState()
    val pets by viewModel.petList.collectAsState()

    // Dropdowns
    val vetDropdownExpanded = remember {
        mutableStateOf(false)
    }

    val petDropdownExpanded = remember {
        mutableStateOf(false)
    }

    val datesDropdownExpanded = remember {
        mutableStateOf(false)
    }

    // Selected items
    var selectedPet by remember { viewModel.selectedPet }
    var selectedVet by remember { viewModel.selectedVet }
    var selectedDate by remember { viewModel.selectedDate }
    var selectedAppointment by remember { viewModel.selectedAppointment }

    // Date picker
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis >= System.currentTimeMillis()
        }
    })

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var showFreeAppointments by remember {
        mutableStateOf(false)
    }

    val freeAppointmentsList by viewModel.datesList.collectAsState()

    /**
     * Change height of dialog based on the content.
     */
    val modifier = if (showFreeAppointments) {
        Modifier
            .height(300.dp)
    } else {
        Modifier
            .height(470.dp)
    }

    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard(
            modifier = modifier
                .fillMaxWidth()
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
                if (!showFreeAppointments) {
                    CustomDropdown(
                        items = pets,
                        title = stringResource(R.string.pet_name),
                        expanded = petDropdownExpanded,
                        selectedItem = selectedPet?.petName
                            ?: stringResource(id = R.string.pick_animal),
                        onItemSelected = { pet ->
                            selectedPet = (pet as PetModel)
                        },
                        modifier = Modifier.padding(bottom = 25.dp, top = 10.dp)
                    )
                    CustomDropdown(
                        items = vets,
                        title = stringResource(R.string.vet),
                        expanded = vetDropdownExpanded,
                        selectedItem = selectedVet?.name
                            ?: stringResource(id = R.string.pick_vet),
                        onItemSelected = { vet ->
                            selectedVet = (vet as VetModel)
                        },
                        modifier = Modifier.padding(bottom = 25.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.appointment),
                            color = LightGrey,
                            fontFamily = quickSandFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        OutlinedButton(
                            onClick = {
                                showDatePicker = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = selectedDate ?: stringResource(id = R.string.pick_date),
                                color = LightBlue,
                                fontFamily = quickSandFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp,
                            )
                        }
                    }
                } else {
                    CustomDropdown(
                        items = freeAppointmentsList,
                        title = stringResource(R.string.appointment),
                        expanded = datesDropdownExpanded,
                        selectedItem = selectedAppointment?.toString()
                            ?: stringResource(id = R.string.pick_hour),
                        onItemSelected = { appo ->
                            selectedAppointment = (appo as SelectableAppointment)
                        },
                        modifier = Modifier.padding(bottom = 10.dp, top = 30.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (!showFreeAppointments) {
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
                                viewModel.getFreeAppointmentsOfVet()
                                showFreeAppointments = true
                            },
                            enabled = (selectedPet != null && selectedVet != null && selectedDate != null)
                        ) {
                            Text(
                                text = stringResource(R.string.next),
                                fontFamily = quickSandFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        }
                    } else {
                        TextButton(
                            modifier = Modifier.padding(8.dp),
                            onClick = {
                                showFreeAppointments = false
                            },
                        ) {
                            Text(
                                text = stringResource(R.string.back),
                                fontFamily = quickSandFamily,
                                fontWeight = FontWeight.Normal,
                                color = LightGrey,
                                fontSize = 18.sp
                            )
                        }
                        TextButton(
                            modifier = Modifier.padding(8.dp),
                            onClick = {
                                onSuccess()
                            },
                            enabled = (selectedAppointment != null)
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

    /**
     * Show date picker when user clicks on the date field.
     */
    if (showDatePicker) {
        DatePickerDialog(onDismissRequest = {
            showDatePicker = false
        }, confirmButton = {
            TextButton(onClick = {
                selectedDate = formatter.format(datePickerState.selectedDateMillis)
                showDatePicker = false
            }) {
                Text(text = stringResource(id = R.string.save))
            }
        }, dismissButton = {
            TextButton(onClick = {
                showDatePicker = false
            }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false,
                )
            }
        }
    }
}

/**
 * Custom dropdown menu.
 * @param items: List of items to show in the dropdown.
 * @param title: Title of the dropdown.
 * @param expanded: MutableState<Boolean> for showing/hiding dropdown.
 * @param selectedItem: String for showing selected item.
 * @param onItemSelected: (Any) -> Unit function for selecting item.
 * @param modifier: Modifier for styling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdown(
    items: List<Any>,
    title: String,
    expanded: MutableState<Boolean>,
    selectedItem: String,
    onItemSelected: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = title,
            color = LightGrey,
            fontFamily = quickSandFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {
            expanded.value = !expanded.value
        }) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                shape = CircleShape,
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded.value, onDismissRequest = {
                    expanded.value = false
                }, modifier = Modifier
                    .height(200.dp)
                    .background(color = Color.White)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(text = {
                        Text(
                            text = item.toString(),
                            color = LightGrey,
                            fontFamily = quickSandFamily,
                            fontWeight = FontWeight.Normal
                        )
                    }, onClick = {
                        onItemSelected(item)
                        expanded.value = false
                    })
                }
            }
        }
    }
}