package edu.unicauca.moneytrack.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class clsEntry(
    override var id: String = "",
    override var nombre: String = "",
    override var valor: Double = 0.0,
    override var fecha: String = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
) : clsTransaction() {
    override fun toMap(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre,
            "valor" to valor,
            "fecha" to fecha
        )
    }
}