package edu.unicauca.moneytrack.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import edu.unicauca.moneytrack.model.clsEntry
import edu.unicauca.moneytrack.model.clsExpense
import edu.unicauca.moneytrack.model.clsMoney
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class MoneyViewModel: ViewModel () {
    private val db = Firebase.firestore

    private var _listaGastos = MutableLiveData<List<clsExpense>>(emptyList())
    val listaGastos: LiveData<List<clsExpense>> = _listaGastos

    private var _listaIngresos = MutableLiveData<List<clsEntry>>(emptyList())
    val listaIngresos: LiveData<List<clsEntry>> = _listaIngresos

    private var _dinero = MutableLiveData<clsMoney?>()
    val dinero: MutableLiveData<clsMoney?> = _dinero

    init {
        obtenerGastos()
        obtenerIngresos()
    }

    fun obtenerGastos() {
        db.collection("gastos").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("MoneyViewModel", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val gastos = snapshot.documents.mapNotNull { it.toObject(clsExpense::class.java) }
                _listaGastos.postValue(gastos)
            } else {
                Log.d("MoneyViewModel", "Current data: null")
            }
        }
    }

    // Obtener el gasto por ID, devolviendo LiveData
    fun getExpenseByIdLive(expenseId: String): LiveData<clsExpense?> {
        val expenseLiveData = MutableLiveData<clsExpense?>()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val expense = _listaGastos.value?.find { it.id == expenseId }
                expenseLiveData.postValue(expense)
            } catch (e: Exception) {
                e.printStackTrace()
                expenseLiveData.postValue(null) // En caso de error, devolver null
            }
        }

        return expenseLiveData
    }

    fun agregarGasto(expense: clsExpense) {
        // Verificar si hay suficiente dinero
        if (_dinero.value?.total ?: 0.0 >= expense.valor) {
            expense.id = UUID.randomUUID().toString()
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    db.collection("gastos").document(expense.id).set(expense).await()
                    _listaGastos.postValue(_listaGastos.value?.plus(expense))
                    // Restar el gasto del dinero total
                    actualizarDineroTotal(-expense.valor) // Restar el valor del gasto
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            throw Exception("No hay suficiente dinero para agregar este gasto.")
        }
    }

    fun actualizarGasto(expense: clsExpense) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Actualiza el gasto en Firestore
                db.collection("gastos").document(expense.id).update(expense.toMap()).await()
                // No es necesario actualizar _listaGastos aquí, el listener se encargará de eso
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun borrarGasto(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Buscar el gasto a eliminar
                val gastoAEliminar = _listaGastos.value?.find { it.id == id }
                if (gastoAEliminar != null) {
                    actualizarDineroTotal(-gastoAEliminar.valor) // Restituir el dinero gastado
                }

                // Eliminar el gasto de la base de datos
                db.collection("gastos").document(id).delete().await()

                // No es necesario actualizar _listaGastos aquí, ya que el listener lo hará automáticamente.
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun obtenerIngresos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resultado = db.collection("ingresos").get().await()
                val ingresos = resultado.documents.mapNotNull { it.toObject(clsEntry::class.java) }
                _listaIngresos.postValue(ingresos)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun agregarIngreso(ingreso: clsEntry) {
        ingreso.id = UUID.randomUUID().toString()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("ingresos").document(ingreso.id).set(ingreso).await()
                _listaIngresos.postValue(_listaIngresos.value?.plus(ingreso))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun actualizarIngreso(ingreso: clsEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("ingresos").document(ingreso.id).update(ingreso.toMap()).await()
                _listaIngresos.postValue(_listaIngresos.value?.map { if (it.id == ingreso.id) ingreso else it })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun borrarIngreso(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("ingresos").document(id).delete().await()
                _listaIngresos.postValue(_listaIngresos.value?.filter { it.id != id })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun actualizarDineroTotal(cambio: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dineroActual = _dinero.value ?: clsMoney(id = "", total = 0.0)
                val nuevoTotal = dineroActual.total + cambio
                val dineroActualizado = dineroActual.copy(total = nuevoTotal)
                db.collection("dinero").document("total").set(dineroActualizado).await()
                _dinero.postValue(dineroActualizado)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
