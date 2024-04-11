package hu.android.qtyadoki.data

data class MedicationData(
    val PrescId: Int,
    val date: String,
    val name: String,
    val type: String
){
    /**
     * Returns the date in a formatted way.
     */
    fun getFormattedDate(): String {
        return date.substring(0, 10)
    }
}
