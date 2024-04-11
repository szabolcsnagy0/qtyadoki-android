package hu.android.qtyadoki.ui.screens.drawerscreens.appointmentsscreen.appointmentslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.signature.MediaStoreSignature
import hu.android.qtyadoki.R
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.data.AppointmentData
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.quickSandFamily

/**
 * Composable function for displaying a single appointment in the list.
 * @param data The appointment data to display.
 * @param onClick The function to execute when the appointment is clicked.
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ListItem(
    data: AppointmentData,
    onClick: (Int) -> Unit = { _ -> }
) {
    ElevatedCard(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .clickable(onClick = {
                onClick(data.appId)
            }),
        shape = RoundedCornerShape(15.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
        ) {
            Surface(
                modifier = Modifier
                    .size(80.dp)
                    .padding(10.dp),
                shape = CircleShape
            ) {
                GlideImage(
                    model = ApiService.getGlideURLPet(data.petId),
                    loading = placeholder(R.drawable.dog_image_placeholder),
                    contentDescription = stringResource(id = R.string.profile_picture),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Crop,
                ) {
                    it.signature(MediaStoreSignature("image/jpeg", System.currentTimeMillis(), 0))
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = data.petName,
                    color = LightGrey,
                    fontFamily = quickSandFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 3.dp)
                )
                Text(
                    text = data.getFormattedDate(),
                    color = LightGrey,
                    fontFamily = quickSandFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 15.sp,
                    maxLines = 2,
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    }
}