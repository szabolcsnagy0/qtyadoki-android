package hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen.petslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.signature.MediaStoreSignature
import hu.android.qtyadoki.R
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.data.PetModel
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.quickSandFamily

/**
 * List item for the pets list.
 * @param data: PetModel - the pet's data.
 * @param onClick: (Int) -> Unit - the function to be called when the item is clicked.
 * @param onTransferAccepted: (Int) -> Unit - the function to be called when the transfer is accepted.
 * @param onTransferRejected: (Int) -> Unit - the function to be called when the transfer is rejected.
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ListItem(
    data: PetModel,
    onClick: (Int) -> Unit = { _ -> },
    onTransferAccepted: (Int) -> Unit = { _ -> },
    onTransferRejected: (Int) -> Unit = { _ -> }
) {
    ElevatedCard(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .clickable(onClick = {
                if (data.state != PetModel.State.OUT)
                    onClick(data.petId)
            }),
        shape = RoundedCornerShape(15.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .padding(horizontal = 0.dp)
                .fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(3f)
            ) {
                Surface(
                    modifier = Modifier
                        .size(70.dp)
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
                        it.signature(
                            MediaStoreSignature(
                                "image/jpeg",
                                System.currentTimeMillis(),
                                0
                            )
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                ) {
                    Text(
                        text = data.petName,
                        color = LightGrey,
                        fontFamily = quickSandFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        maxLines = 1,
                    )
                }
            }
            if (data.state != PetModel.State.IDLE) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.weight(1.5f)
                ) {
                    if (data.state == PetModel.State.OUT) {
                        Text(
                            text = stringResource(R.string.pending),
                            color = LightGrey,
                            fontFamily = quickSandFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (data.state == PetModel.State.IN) {
                        IconButton(
                            onClick = {
                                onTransferRejected(data.petId)
                            }
                        ) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = stringResource(R.string.decline),
                                tint = LightBlue,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                onTransferAccepted(data.petId)
                            }
                        ) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = stringResource(R.string.accept),
                                tint = LightBlue,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
