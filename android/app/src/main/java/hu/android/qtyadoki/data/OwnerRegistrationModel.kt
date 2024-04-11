package hu.android.qtyadoki.data

data class OwnerRegistrationModel(
    var name: String = "",
    var phone: String = "",
    var address: String = "",
    var email: String = "",
    var password: String? = null,
    var type: String = "owner"
)
