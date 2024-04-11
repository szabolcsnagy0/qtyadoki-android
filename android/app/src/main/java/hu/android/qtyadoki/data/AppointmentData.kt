package hu.android.qtyadoki.data

data class AppointmentData(
    var petName: String,
    var date: String,
    var vetName: String,
    var petId: Int,
    var appId: Int,
    var vetId: Int,
    var vetPhone: String? = null,
    var vetEmail: String? = null,
    var vetAddress: String? = null,
    var descriptions: List<String?>? = null
) {
    /**
     * Returns the date in a formatted way.
     */
    fun getFormattedDate(): String {
        return date.substring(0, 16)
    }

    /**
     * Returns the description in a formatted way.
     */
    fun getDescription(): String {
        var description = ""
        descriptions?.forEach {
            description += "$it\n"
        }
        return description
    }
}
