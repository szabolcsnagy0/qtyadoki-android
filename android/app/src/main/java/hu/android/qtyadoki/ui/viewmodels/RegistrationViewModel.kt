package hu.android.qtyadoki.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.data.OwnerRegistrationModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ViewModel for the Registration screen.
 */
class RegistrationViewModel : ViewModel() {

    /**
     * Register a new user.
     * @param ownerModel The new user's data.
     * @param result The result of the registration.
     * @param success The success of the registration.
     */
    fun registerUser(
        ownerModel: OwnerRegistrationModel,
        result: MutableState<String>,
        success: MutableState<Boolean>
    ) {
        val call = ApiService.getInstance().registerUser(ownerModel)
        call!!.enqueue(object : Callback<OwnerRegistrationModel?> {

            override fun onResponse(
                call: Call<OwnerRegistrationModel?>,
                response: Response<OwnerRegistrationModel?>
            ) {
                if (response.isSuccessful) {
                    success.value = true
                    result.value = "Sikeres regisztráció!"
                } else result.value =
                    "Hiba! Próbáld újra! Hibakód: ${response.code()} ${response.message()}"
            }

            override fun onFailure(call: Call<OwnerRegistrationModel?>, t: Throwable) {
                success.value = false
                result.value = "Hiba! ${t.message.toString()}!"
            }
        })
    }
}