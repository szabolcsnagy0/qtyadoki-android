package hu.android.qtyadoki.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.data.OwnerModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * View model for the profile edit screen.
 */
class ProfileEditViewModel : ViewModel(), ImageHelper {
    private val ownerData: MutableState<OwnerModel> = mutableStateOf(OwnerModel())

    val phone: MutableState<String> = mutableStateOf(ownerData.value.phone)
    val name: MutableState<String> = mutableStateOf(ownerData.value.name)
    val address: MutableState<String> = mutableStateOf(ownerData.value.address)

    init {
        getUserData()
    }

    /**
     * Get the user data from the server.
     */
    private fun getUserData() {
        val call = ApiService.getInstance().getUserData()
        call!!.enqueue(object : Callback<OwnerModel?> {

            override fun onResponse(call: Call<OwnerModel?>, response: Response<OwnerModel?>) {
                if (response.isSuccessful) {
                    val newData = response.body()!!
                    if (newData != ownerData.value) {
                        ownerData.value = newData
                        phone.value = ownerData.value.phone
                        name.value = ownerData.value.name
                        address.value = ownerData.value.address
                    }
                }
            }

            override fun onFailure(call: Call<OwnerModel?>, t: Throwable) {
                Log.e("ProfileEditViewModel", "getUserData: ${t.message.toString()}")
            }
        })
    }

    /**
     * Update the user data on the server.
     * @param onResult Callback function to handle the result.
     */
    fun updateUserData(
        onResult: (Boolean, String) -> Unit,
    ) {
        ownerData.value.name = name.value
        ownerData.value.address = address.value
        ownerData.value.phone = phone.value
        Log.i("userdata", ownerData.value.toString())
        val call = ApiService.getInstance().updateUserData(ownerData.value)
        call!!.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    onResult(true, "Sikeres frissítés!")
                } else onResult(false, "Hiba! Hibakód: ${response.code()} ${response.message()}")

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ProfileEditViewModel", "updateUserData: ${t.message.toString()}")
                onResult(false, "Hiba! ${t.message.toString()}!")
            }
        })
    }
}