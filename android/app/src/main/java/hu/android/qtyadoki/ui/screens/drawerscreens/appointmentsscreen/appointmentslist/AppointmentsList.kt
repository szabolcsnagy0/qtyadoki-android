package hu.android.qtyadoki.ui.screens.drawerscreens.appointmentsscreen.appointmentslist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.android.qtyadoki.data.AppointmentData

/**
 * Composable function for displaying the list of appointments.
 * @param list The list of appointments.
 * @param onItemClicked The function to be called when an item is clicked.
 */
@Composable
fun AppointmentsList(
    list: List<AppointmentData>,
    onItemClicked: (Int) -> Unit = { _ -> }
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = 30.dp
        )
    ) {
        items(list) { item ->
            ListItem(data = item, onClick = {
                onItemClicked(it)
            })
        }
    }
}