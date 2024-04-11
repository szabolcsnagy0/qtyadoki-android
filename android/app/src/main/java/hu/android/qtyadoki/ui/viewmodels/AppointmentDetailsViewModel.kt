package hu.android.qtyadoki.ui.viewmodels

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.data.AppointmentData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * View model for the appointment details screen.
 * @param appointmentId The ID of the appointment to be displayed.

 */
class AppointmentDetailsViewModel(private val appointmentId: Int? = null) : ViewModel() {

    /**
     * The currently displayed appointment.
     */
    val currentAppointment: MutableState<AppointmentData?> = mutableStateOf(null)

    init {
        getAppointmentDetails()
    }

    private fun getAppointmentDetails() {
        if (appointmentId == null) return
        val call = ApiService.getInstance().getAppointmentData(appointmentId)
        call!!.enqueue(object : Callback<AppointmentData?> {

            override fun onResponse(
                call: Call<AppointmentData?>,
                response: Response<AppointmentData?>
            ) {
                if (response.isSuccessful) {
                    currentAppointment.value = response.body()
                } else {
                    Log.e("AppointmentDetails", "Error during API call: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AppointmentData?>, t: Throwable) {
                Log.e("AppointmentDetails", "Error during API call: ${t.message}")
            }
        })
    }

    /**
     * Gets the location of the vet's address.
     * @param context The context of the application.
     * @return The location of the vet's address or null if the address is not found.
     */
    @Suppress("DEPRECATION")
    suspend fun getLocationOfAddress(context: Context): LatLng? {
        if (currentAppointment.value == null || currentAppointment.value!!.vetAddress.isNullOrBlank()) {
            return null
        }

        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context)
                val results =
                    geocoder.getFromLocationName(currentAppointment.value!!.vetAddress!!, 1)
                if (results.isNullOrEmpty()) {
                    return@withContext null
                }
                return@withContext LatLng(results[0].latitude, results[0].longitude)
            } catch (e: Exception) {
                Log.e("GeocodingError", "Error during geocoding: ${e.message}")
                return@withContext null
            }
        }
    }
}