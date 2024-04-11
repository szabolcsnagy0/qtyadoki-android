package hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen.petdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import hu.android.qtyadoki.R
import hu.android.qtyadoki.data.MedicationData
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.quickSandFamily

/**
 * Dialog for showing the details of a medication.
 * @param data: MedicationData
 * @param onDismissRequest: () -> Unit - function for dismissing the dialog
 */
@Composable
fun MedicationDetailsDialog(
    data: MedicationData,
    onDismissRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp)
            ) {
                TextLine(
                    boldText = stringResource(id = R.string.medication_name) + ": ",
                    normalText = data.name
                )
                TextLine(
                    boldText = stringResource(id = R.string.date) + ": ",
                    normalText = data.getFormattedDate()
                )
                TextLine(
                    boldText = stringResource(id = R.string.type) + ": ",
                    normalText = data.type
                )
            }
        }
    }
}

/**
 * Composable for showing a line of text.
 * @param boldText: String - text to be bold
 * @param normalText: String - text to be normal
 * @param modifier: Modifier - modifier for the composable
 */
@Composable
fun TextLine(
    boldText: String,
    normalText: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Text(
            text = boldText,
            fontFamily = quickSandFamily,
            fontWeight = FontWeight.Bold,
            color = LightGrey,
            fontSize = 20.sp,
            modifier = modifier.padding(end = 15.dp)
        )
        Text(
            text = normalText,
            fontFamily = quickSandFamily,
            fontWeight = FontWeight.Normal,
            color = LightGrey,
            fontSize = 20.sp,
        )
    }
}