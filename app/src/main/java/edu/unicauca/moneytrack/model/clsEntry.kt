package edu.unicauca.moneytrack.model

data class clsEntry(
    var id: String = "",
    var nombre: String = "",
    var valor: Double = 0.0,
    var fecha: String = "",
    var totalDineroId: String = ""
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre,
            "valor" to valor,
            "fecha" to fecha,
            "totalDineroId" to totalDineroId
        )
    }
}
