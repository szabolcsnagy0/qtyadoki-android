package hu.android.qtyadoki.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.data.AppointmentData
import hu.android.qtyadoki.data.NewAppointment
import hu.android.qtyadoki.data.PetModel
import hu.android.qtyadoki.data.SelectableAppointment
import hu.android.qtyadoki.data.SelectedDate
import hu.android.qtyadoki.data.VetModel
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * View model for the Appointments screen.
 */
class AppointmentsViewModel : ImageHelper {
    /**
     * List of appointments.
     */
    private val _appointmentsList = MutableStateFlow<List<AppointmentData>>(
        emptyList()
    )
    val appointmentsList = _appointmentsList

    /**
     * List of pets for the new appointment dialog.
     */
    private val _petList = MutableStateFlow<List<PetModel>>(
        emptyList()
    )
    val petList = _petList

    /**
     * List of vets for the new appointment dialog.
     */
    private val _vetList = MutableStateFlow<List<VetModel>>(
        emptyList()
    )
    val vetList = _vetList

    /**
     * List of free appointments for the new appointment dialog.
     */
    private val _datesList = MutableStateFlow<List<SelectableAppointment>>(
        emptyList()
    )
    val datesList = _datesList

    /**
     * Selected data for the new appointment dialog.
     */
    val selectedPet: MutableState<PetModel?> = mutableStateOf(null)
    val selectedVet: MutableState<VetModel?> = mutableStateOf(null)
    val selectedDate: MutableState<String?> = mutableStateOf(null)
    val selectedAppointment: MutableState<SelectableAppointment?> = mutableStateOf(null)

    val isRefreshing = mutableStateOf(false)

    init {
        getAppointmentsList()
    }

    /**
     * Refreshes the list of appointments.
     */
    fun refreshAppointmentsList() {
        isRefreshing.value = true
        getAppointmentsList(onCallback = {
            isRefreshing.value = false
        })
    }

    /**
     * Gets the list of appointments.
     * @param onCallback Callback function for the result.
     */
    private fun getAppointmentsList(onCallback: () -> Unit = {}) {
        val call = ApiService.getInstance().getAppointmentsList()
        call!!.enqueue(object : Callback<List<AppointmentData>?> {

            override fun onResponse(
                call: Call<List<AppointmentData>?>,
                response: Response<List<AppointmentData>?>
            ) {
                if (response.isSuccessful) {
                    val newData = response.body()!!
                    if (newData != _appointmentsList.value)
                        _appointmentsList.value = newData
                }
                onCallback()
            }

            override fun onFailure(call: Call<List<AppointmentData>?>, t: Throwable) {
                Log.e("AppointmentsViewModel", "ERROR: " + t.message)
                onCallback()
            }
        })
    }

    /**
     * Initializes the data for the new appointment dialog.
     */
    fun initDialogOptions() {
        getVetList()
        getPetList()
    }

    /**
     * Gets the list of vets.
     */
    private fun getVetList() {
        val call = ApiService.getInstance().getVetList()
        call!!.enqueue(object : Callback<List<VetModel>?> {

            override fun onResponse(
                call: Call<List<VetModel>?>,
                response: Response<List<VetModel>?>
            ) {
                if (response.isSuccessful) {
                    val newData = response.body()!!
                    if (newData != _vetList.value) {
                        _vetList.value = newData
                    }
                } else {
                    Log.e("AppointmentsViewModel", "ERROR: " + response.code())
                }
            }

            override fun onFailure(call: Call<List<VetModel>?>, t: Throwable) {
                Log.e("AppointmentsViewModel", "ERROR: " + t.message)
            }
        })
    }

    /**
     * Gets the list of pets.
     */
    private fun getPetList() {
        val call = ApiService.getInstance().getPetsList()
        call!!.enqueue(object : Callback<List<PetModel>?> {

            override fun onResponse(
                call: Call<List<PetModel>?>,
                response: Response<List<PetModel>?>
            ) {
                if (response.isSuccessful) {
                    val newData = response.body()!!
                    if (newData != _petList.value)
                        _petList.value = newData
                } else Log.e("AppointmentsViewModel", "ERROR: " + response.code())
            }

            override fun onFailure(call: Call<List<PetModel>?>, t: Throwable) {
                Log.e("AppointmentsViewModel", "ERROR: " + t.message)
            }
        })
    }

    /**
     * Gets the free appointments of the selected vet.
     */
    fun getFreeAppointmentsOfVet() {
        if (selectedVet.value == null || selectedDate.value == null) return
        val selectedValues = SelectedDate(
            vetId = selectedVet.value!!.id,
            date = selectedDate.value!!
        )
        val call = ApiService.getInstance().getFreeAppointmentsOfVet(date = selectedValues)
        call!!.enqueue(object : Callback<List<SelectableAppointment>?> {

            override fun onResponse(
                call: Call<List<SelectableAppointment>?>,
                response: Response<List<SelectableAppointment>?>
            ) {
                if (response.isSuccessful) {
                    val newData = response.body()!!
                    if (newData != _datesList.value)
                        _datesList.value = newData
                } else Log.e("AppointmentsViewModel", "ERROR: " + response.code())
            }

            override fun onFailure(call: Call<List<SelectableAppointment>?>, t: Throwable) {
                Log.e("AppointmentsViewModel", "ERROR: " + t.message)
            }
        })
    }

    /**
     * Adds a new appointment.
     * @param onResult Callback function for the result.
     */
    fun addNewAppointment(onResult: (Boolean) -> Unit = { _ -> }) {
        if (selectedVet.value == null || selectedAppointment.value == null || selectedPet.value == null) return
        val newAppointment = NewAppointment(
            vetId = selectedVet.value!!.id,
            date = selectedAppointment.value!!.appointment,
            petId = selectedPet.value!!.petId
        )
        val call = ApiService.getInstance().addAppointment(newAppointment = newAppointment)
        call!!.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                getAppointmentsList()
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResult(false)
                Log.e("AppointmentsViewModel", "ERROR: " + t.message)
            }
        })
    }

    /**
     * Clears the selected data in the new appointment dialog.

     */
    fun clearSelectedData() {
        selectedPet.value = null
        selectedVet.value = null
        selectedDate.value = null
        selectedAppointment.value = null
    }
}