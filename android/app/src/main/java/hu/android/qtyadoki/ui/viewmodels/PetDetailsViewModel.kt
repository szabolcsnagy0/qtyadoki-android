package hu.android.qtyadoki.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.data.MedicationData
import hu.android.qtyadoki.data.PetModel
import hu.android.qtyadoki.data.TransferData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * View model for the PetDetails screen.
 * @param petId The id of the pet to be displayed.
 */
class PetDetailsViewModel(val petId: Int? = null) : ViewModel(), ImageHelper {
    private var selectedPetId: MutableState<Int?> = mutableStateOf(null)
    val petModel: MutableLiveData<PetModel> = MutableLiveData(
        PetModel(0, "", "", PetModel.State.IDLE)
    )

    val transferSuccess: MutableLiveData<Boolean?> = MutableLiveData(null)
    val updateSuccess: MutableLiveData<Boolean?> = MutableLiveData(null)

    private var _medicationList = MutableLiveData<List<MedicationData>>(
        emptyList()
    )
    var medicationList = _medicationList

    init {
        if (petId != null)
            selectPet(petId)
    }

    /**
     * Gets the details of the pet with the given id.
     */
    private fun getPetDetails() {
        if (selectedPetId.value == null) return
        val call = ApiService.getInstance().getPetById(selectedPetId.value!!)
        call!!.enqueue(object : Callback<PetModel?> {

            override fun onResponse(call: Call<PetModel?>, response: Response<PetModel?>) {
                if (response.isSuccessful && petModel.value != response.body()) {
                    petModel.value = response.body()
                }
            }

            override fun onFailure(call: Call<PetModel?>, t: Throwable) {
                Log.e("PetDetailsViewModel", "getPetDetails: ${t.message}")
            }
        })
    }

    /**
     * Transfers the pet with the given id to the user with the given email.
     * @param recipientEmail The email of the user to transfer the pet to.
     */
    fun transferPet(recipientEmail: String) {
        if (petModel.value == null) return
        val transferData = TransferData(petModel.value!!.petId, recipientEmail)
        val call = ApiService.getInstance().transferPet(transferData)
        call!!.enqueue(object : Callback<ResponseBody?> {

            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                transferSuccess.value = response.isSuccessful
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                transferSuccess.value = false
            }
        })
    }

    /**
     * Gets the medications of the pet with the given id.
     */
    private fun getMedications() {
        if (selectedPetId.value == null) return
        val call = ApiService.getInstance().getMedicationsOfPet(selectedPetId.value!!)
        call!!.enqueue(object : Callback<List<MedicationData>?> {

            override fun onResponse(
                call: Call<List<MedicationData>?>,
                response: Response<List<MedicationData>?>
            ) {
                if (response.isSuccessful && medicationList != response.body()) {
                    _medicationList.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<MedicationData>?>, t: Throwable) {
                Log.e("PetDetailsViewModel", "getMedications: ${t.message}")
            }
        })
    }

    /**
     * Saves the changes made to the pet.
     */
    fun saveChanges() {
        if (petModel.value == null) return
        val call = ApiService.getInstance().updatePet(petModel.value)
        call!!.enqueue(object : Callback<ResponseBody?> {

            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                updateSuccess.value = response.isSuccessful
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                updateSuccess.value = false
            }
        })
    }

    /**
     * Selects the pet with the given id.
     * @param petId The id of the pet to be selected.
     */
    fun selectPet(petId: Int) {
        selectedPetId.value = petId
        getPetDetails()
        getMedications()
    }
}