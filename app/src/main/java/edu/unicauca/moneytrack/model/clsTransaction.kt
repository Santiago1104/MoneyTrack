package edu.unicauca.moneytrack.model
abstract class clsTransaction(
    open var id: String = "",
    open var nombre: String = "",
    open var valor: Double = 0.0,
    open var fecha: String = "",
) {
    abstract fun toMap(): Map<String, Any>
}