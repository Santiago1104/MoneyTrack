package edu.unicauca.moneytrack.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class clsExpense(
    override var id: String = "",
    override var nombre: String = "",
    override var valor: Double = 0.0,
    override var fecha: String = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), // Aquí usas String en vez de LocalDate
    var categoria: String = ""
) : clsTransaction() {
    override fun toMap(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre,
            "categoria" to categoria,
            "valor" to valor,
            "fecha" to fecha
        )
    }
}
