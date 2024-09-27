package edu.unicauca.moneytrack.model

public data class clsExpense(
    var id: String = "",
    var nombre: String = "",
    var categoria: String = "",
    var valor: Double = 0.0,
    var fecha: String = "",
    var totalDineroId: String = ""
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre,
            "categoria" to categoria,
            "valor" to valor,
            "fecha" to fecha,
            "totalDineroId" to totalDineroId
        )
    }
}
