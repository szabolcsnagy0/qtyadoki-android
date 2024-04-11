package hu.android.qtyadoki.data

import com.google.gson.annotations.SerializedName

data class PetModel(
    var petId: Int,
    var petName: String,
    var species: String,
    var state: State
) {
    override fun toString(): String {
        return petName
    }

    /**
     * Transfer state of the pet
     */
    enum class State(val value: String) {
        @SerializedName("IN")
        IN("IN"),

        @SerializedName("OUT")
        OUT("OUT"),

        @SerializedName("IDLE")
        IDLE("IDLE")
    }
}
