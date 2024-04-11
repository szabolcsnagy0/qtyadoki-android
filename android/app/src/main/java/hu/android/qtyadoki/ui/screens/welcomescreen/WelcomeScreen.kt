package hu.android.qtyadoki.ui.screens.welcomescreen

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.android.qtyadoki.R
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightGrey

/**
 * Composable function for the welcome screen.
 * @param inputForm The input form composable function.
 * @param onTextButtonClicked The function to be called when the text button is clicked.
 * @param smallText The text to be displayed next to the text button.
 * @param textButtonText The text to be displayed on the text button.
 * @param modifier The modifier to be applied to the composable.
 * @param imageWeight The weight of the image.
 */
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    inputForm: @Composable () -> Unit = {},
    onTextButtonClicked: () -> Unit = {},
    smallText: String = "",
    textButtonText: String = "",
    imageWeight: Float = 1f
) {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.logo_anim)
    var atEnd by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center, modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Image(
            painter = rememberAnimatedVectorPainter(animatedImageVector = image, atEnd = atEnd),
            contentDescription = "",
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp)
                .weight(imageWeight)
                .clickable(indication = null,
                    interactionSource = remember { MutableInteractionSource() }) { atEnd = !atEnd }
        )
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.size(16.dp))
            inputForm()
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = smallText,
                    color = LightGrey
                )
                TextButton(onClick = {
                    onTextButtonClicked()
                }) {
                    Text(
                        text = textButtonText,
                        color = LightBlue
                    )
                }
            }
        }
    }
}