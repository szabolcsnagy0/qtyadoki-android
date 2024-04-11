package hu.android.qtyadoki.data

data class VetModel(
    val name: String,
    val id: Int
){
    override fun toString(): String {
        return name
    }
}
