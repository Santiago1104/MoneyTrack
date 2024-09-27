package edu.unicauca.moneytrack.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class clsExpense(
    override var id: String = "",
    override var nombre: String = "",
    override var valor: Double = 0.0,
    override var fecha: LocalDate = LocalDate.now(),
    var categoria: String = ""
) : clsTransaction() {
    override fun toMap(): Map<String, Any> {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return mapOf(
            "nombre" to nombre,
            "categoria" to categoria,
            "valor" to valor,
            "fecha" to fecha.format(formatter)
        )
    }
}
