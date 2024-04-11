package hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen.petdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.android.qtyadoki.data.MedicationData
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.quickSandFamily

/**
 * A composable that displays a medication item.
 * @param item The medication item to display.
 */
@Composable
fun MedicationListItem(item: MedicationData) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    ElevatedCard(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .clickable(onClick = {
                openDialog = true
            }),
        shape = RoundedCornerShape(15.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = item.name,
                color = LightGrey,
                fontFamily = quickSandFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Text(
                text = item.getFormattedDate(),
                color = LightGrey,
                fontFamily = quickSandFamily,
                fontWeight = FontWeight.Light,
                fontSize = 15.sp,
                maxLines = 1,
            )
        }
    }

    if (openDialog) {
        MedicationDetailsDialog(data = item, onDismissRequest = {
            openDialog = false
        })
    }
}