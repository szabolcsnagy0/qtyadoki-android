package hu.android.qtyadoki.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.data.NewPet
import hu.android.qtyadoki.data.PetModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * View model for the Pets screen.
 */
class PetsViewModel : ViewModel(), ImageHelper {

    private var _petsList = MutableLiveData<List<PetModel>>(
        emptyList()
    )
    var petsList = _petsList

    val isRefreshing = mutableStateOf(false)

    init {
        getPetsList()
    }

    /**
     * Refreshes the list of pets.
     */
    fun refreshPetList() {
        isRefreshing.value = true
        petsList.value = emptyList()
        getPetsList(onCallback = {
            isRefreshing.value = false
        })
    }

    /**
     * Gets the list of pets from the server.
     * @param onCallback Callback function to be called after the request is finished.
     */
    private fun getPetsList(onCallback: () -> Unit = {}) {
        val call = ApiService.getInstance().getPetsList()
        call!!.enqueue(object : Callback<List<PetModel>?> {

            override fun onResponse(
                call: Call<List<PetModel>?>,
                response: Response<List<PetModel>?>
            ) {
                if (response.isSuccessful) {
                    val newData = response.body()!!
                    if (petsList.value != newData)
                        petsList.value = newData
                }
                onCallback()
            }

            override fun onFailure(call: Call<List<PetModel>?>, t: Throwable) {
                Log.e("PetsViewModel", "Error while getting pets list: ${t.message}")
                onCallback()
            }
        })
    }

    /**
     * Adds a new pet to the server.
     * @param name Name of the pet.
     * @param species Species of the pet.
     * @param onResponseCallback Callback function to be called after the request is finished. The parameter is the ID of the new pet.
     */
    fun addPet(name: String, species: String, onResponseCallback: (Int?) -> Unit) {
        val call = ApiService.getInstance().addPet(NewPet(name, species))
        call!!.enqueue(object : Callback<PetModel> {

            override fun onResponse(
                call: Call<PetModel>,
                response: Response<PetModel>
            ) {
                getPetsList()
                onResponseCallback(response.body()?.petId)
            }

            override fun onFailure(call: Call<PetModel>, t: Throwable) {
                onResponseCallback(null)
            }
        })
    }

    /**
     * Accepts a pet that is in transfer.
     */
    fun acceptPet(petId: Int, onResponseCallback: (Boolean) -> Unit = { _ -> }) {
        val call = ApiService.getInstance().acceptPet(petId)
        call!!.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                petsList.value = emptyList()
                getPetsList()
                onResponseCallback(response.isSuccessful)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("PetsViewModel", "Error while accepting pet: ${t.message}")
                onResponseCallback(false)
            }
        })
    }

    /**
     * Rejects a pet that is in transfer.
     */
    fun rejectPet(petId: Int, onResponseCallback: (Boolean) -> Unit = { _ -> }) {
        val call = ApiService.getInstance().rejectPet(petId)
        call!!.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                petsList.value = emptyList()
                getPetsList()
                onResponseCallback(response.isSuccessful)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("PetsViewModel", "Error while rejecting pet: ${t.message}")
                onResponseCallback(false)
            }
        })
    }
}