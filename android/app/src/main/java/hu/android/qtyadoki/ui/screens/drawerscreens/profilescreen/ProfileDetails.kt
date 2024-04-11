package hu.android.qtyadoki.ui.screens.drawerscreens.profilescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import hu.android.qtyadoki.ui.components.TextWithTitle
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.LightGreyAlpha
import hu.android.qtyadoki.ui.theme.quickSandFamily
import hu.android.qtyadoki.ui.viewmodels.DrawerViewModel

/**
 * Composable function which displays the profile details.
 * @param viewModel: DrawerViewModel which contains the user data.
 * @param modifier: Modifier which will be applied to the composable.
 * @param onEditClicked: Function which will be called when the edit button is clicked.
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileDetails(
    viewModel: DrawerViewModel,
    modifier: Modifier = Modifier,
    onEditClicked: () -> Unit = {}
) {
    val user by viewModel.ownerData
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 25.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        GlideImage(
            model = ApiService.getGlideURLProfile(),
            loading = placeholder(R.drawable.dog_image_placeholder),
            contentDescription = stringResource(id = R.string.profile_picture),
            modifier = Modifier
                .size(100.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop,
        ) {
            it.signature(MediaStoreSignature("image/jpeg", System.currentTimeMillis(), 0))
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Text(
                text = user.name,
                fontFamily = quickSandFamily,
                fontWeight = FontWeight.SemiBold,
                color = LightGrey,
                fontSize = 28.sp,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            IconButton(
                onClick = {
                    onEditClicked()
                }
            ) {
                Icon(
                    Icons.Rounded.Edit,
                    contentDescription = stringResource(id = R.string.edit_description),
                    tint = LightBlue
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 20.dp),
            thickness = 2.dp,
            color = LightGreyAlpha
        )
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxHeight(0.95f)
        ) {
            TextWithTitle(
                titleText = stringResource(id = R.string.phone_number),
                subText = user.phone,
                modifier = Modifier.padding(bottom = 40.dp)
            )
            TextWithTitle(
                titleText = stringResource(id = R.string.address),
                subText = user.address,
                modifier = Modifier.padding(bottom = 40.dp)
            )
            TextWithTitle(
                titleText = stringResource(id = R.string.email), subText = user.email,
                modifier = Modifier.padding(bottom = 40.dp)
            )
        }
    }
}