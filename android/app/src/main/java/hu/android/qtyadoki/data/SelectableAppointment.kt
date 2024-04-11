package hu.android.qtyadoki.data

data class SelectableAppointment(
    val appointment: String
) {
    override fun toString(): String {
        return appointment.substring(11, 16)
    }
}

