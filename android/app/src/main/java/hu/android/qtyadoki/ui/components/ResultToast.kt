package hu.android.qtyadoki.ui.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import hu.android.qtyadoki.R

/**
 * Show a toast message based on the result of the operation.
 *
 * @param result The result of the operation.
 */
@Composable
fun ResultToast(result: Boolean?) {
    if (result != null) {
        val text = if (result == true) {
            stringResource(id = R.string.success)
        } else {
            stringResource(id = R.string.error)
        }
        Toast.makeText(
            LocalContext.current,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}