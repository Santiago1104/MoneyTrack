package edu.unicauca.moneytrack.model

import java.time.LocalDate

abstract class clsTransaction(
    open var id: String = "",
    open var nombre: String = "",
    open var valor: Double = 0.0,
    open var fecha: LocalDate = LocalDate.now(),
) {
    abstract fun toMap(): Map<String, Any>
}