package hu.android.qtyadoki.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraEnhance
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.signature.MediaStoreSignature
import hu.android.qtyadoki.R
import hu.android.qtyadoki.ui.theme.LightBlue

/**
 * A composable that displays an image with a camera icon in the bottom right corner.
 * @param imageUrl: The url of the image to be displayed
 * @param onCameraClicked: The function to be called when the camera icon is clicked
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PictureWithEditIcon(
    modifier: Modifier = Modifier,
    imageUrl: GlideUrl? = null,
    onCameraClicked: () -> Unit = {}
) {
    Box(modifier = modifier
        .padding(bottom = 5.dp)
        .background(Color.Transparent, shape = CircleShape)
        .clickable {
            onCameraClicked()
        }) {
        GlideImage(
            model = imageUrl,
            loading = placeholder(R.drawable.dog_image_placeholder),
            contentDescription = stringResource(id = R.string.profile_picture),
            modifier = Modifier
                .size(90.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop,
        ) {
            it.signature(MediaStoreSignature("image/jpeg", System.currentTimeMillis(), 0))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(Color.White, shape = CircleShape)
        ) {
            Icon(
                Icons.Rounded.CameraEnhance,
                contentDescription = stringResource(id = R.string.camera_icon),
                tint = LightBlue
            )
        }
    }
}

/**
 * A composable that displays an image with a camera icon in the bottom right corner.
 * @param imageUrl: The url of the image to be displayed
 * @param onCameraClicked: The function to be called when the camera icon is clicked
 */
@Composable
fun PictureWithEditIcon(
    modifier: Modifier = Modifier,
    imageUrl: Uri? = null,
    onCameraClicked: () -> Unit = {}
) {
    Box(modifier = modifier
        .padding(bottom = 5.dp)
        .background(Color.Transparent, shape = CircleShape)
        .clickable {
            onCameraClicked()
        }) {
        if (imageUrl != null) {
            Image(
                modifier = Modifier
                    .size(90.dp)
                    .clip(shape = CircleShape),
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.dog_image_placeholder),
                contentDescription = stringResource(id = R.string.profile_picture),
                modifier = Modifier
                    .size(90.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(Color.White, shape = CircleShape)
        ) {
            Icon(
                Icons.Rounded.CameraEnhance,
                contentDescription = stringResource(id = R.string.camera_icon),
                tint = LightBlue
            )
        }
    }
}