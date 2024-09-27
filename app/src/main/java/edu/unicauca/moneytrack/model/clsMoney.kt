package edu.unicauca.moneytrack.model

data class clsMoney(
    var id: String = "",
    var total: Double = 0.0
) {
    fun toMap(): Map<String, Any> {
        return mapOf(

            "total" to total
        )
    }
}