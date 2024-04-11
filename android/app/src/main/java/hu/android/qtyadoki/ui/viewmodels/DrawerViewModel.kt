package hu.android.qtyadoki.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.data.OwnerModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * View model for the drawer.
 */
class DrawerViewModel : ViewModel() {
    val ownerData = mutableStateOf(OwnerModel())

    init {
        getUserData()
    }

    fun refreshUserData() {
        getUserData()
    }

    /**
     * Get the user data from the API.
     */
    private fun getUserData() {
        val call = ApiService.getInstance().getUserData()
        call!!.enqueue(object : Callback<OwnerModel?> {

            override fun onResponse(call: Call<OwnerModel?>, response: Response<OwnerModel?>) {
                if (response.isSuccessful) {
                    val newData: OwnerModel = response.body()!!
                    if (ownerData.value != newData)
                        ownerData.value = newData
                } else Log.e(
                    "DrawerViewModel",
                    "ERROR: " + response.code() + " " + response.message()
                )
            }

            override fun onFailure(call: Call<OwnerModel?>, t: Throwable) {
                Log.e("DrawerViewModel", "ERROR: " + t.message)
            }
        })
    }
}