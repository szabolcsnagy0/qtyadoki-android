package hu.android.qtyadoki.ui.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import hu.android.qtyadoki.api.ApiService
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * Interface for image upload.
 */
interface ImageHelper {

    /**
     * Uploads the pet's picture.
     *
     * @param image The pet's picture.
     * @param petId The pet's id.
     * @param onResult The result of the upload.
     */
    fun updatePetPicture(
        image: File?,
        petId: Int,
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        if (image == null) {
            onResult(false, "Hiba! Kép nem található!")
            return
        }
        val requestFile = image.asRequestBody()
        val call = ApiService.getInstance().updatePetPicture(petId = petId, requestFile)
        initCall(call, onResult)
    }

    /**
     * Uploads the user's picture.
     *
     * @param image The user's picture.
     * @param onResult The result of the upload.
     */
    fun updateUserPicture(
        image: File?,
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        if (image == null) {
            onResult(false, "Hiba! Kép nem található!")
            return
        }

        val requestFile = image.asRequestBody()
        val call = ApiService.getInstance().updateUserPicture(requestFile)
        initCall(call, onResult)
    }

    /**
     * Uploads the pet's picture.
     * @param call - The call to be executed.
     * @param onResult - The result of the upload.
     */
    private fun initCall(
        call: Call<ResponseBody>?,
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        call!!.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    onResult(true, "Sikeres frissítés!")
                } else onResult(false, "Hiba! Hibakód: ${response.code()} ${response.message()}")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ImageHelper", t.message.toString())
                onResult(false, "Hiba! ${t.message.toString()}!")
            }
        })
    }

    /**
     * Creates a file from the uri.
     */
    fun createImageFile(
        uri: Uri?,
        context: Context
    ): File? {
        if (uri == null) {
            return null
        }

        val filePath = getRealPathFromURI(context, uri)

        val file = File(filePath)
        if (!file.exists()) {
            return null
        }
        return file
    }

    /**
     * Gets the real path of a picture from the given uri.
     */
    private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        if (contentUri.scheme == "content") {
            context.contentResolver.openInputStream(contentUri)?.use { inputStream ->
                val tempFile = File.createTempFile("temp", null, context.cacheDir)
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                return tempFile.absolutePath
            }
        }
        return contentUri.path ?: ""
    }
}