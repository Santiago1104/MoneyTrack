package edu.unicauca.moneytrack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.unicauca.moneytrack.model.clsEntry
import edu.unicauca.moneytrack.model.clsExpense
import edu.unicauca.moneytrack.model.clsMoney
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class MoneyViewModel : ViewModel() {
    private val db = Firebase.firestore

    private var _listaGastos = MutableLiveData<List<clsExpense>>(emptyList())
    val listaGastos: LiveData<List<clsExpense>> = _listaGastos

    private var _listaIngresos = MutableLiveData<List<clsEntry>>(emptyList())
    val listaIngresos: LiveData<List<clsEntry>> = _listaIngresos

    private var _dinero = MutableLiveData<clsMoney?>()
    val dinero: LiveData<clsMoney?> = _dinero

    private var gastosListener: ListenerRegistration? = null
    private var ingresosListener: ListenerRegistration? = null

    init {
        configurarListeners()
    }

    private fun configurarListeners() {
        // Listener para la colección de gastos
        gastosListener = db.collection("gastos").addSnapshotListener { snapshot, e ->
            if (e != null) {
                e.printStackTrace()
                return@addSnapshotListener
            }
            val gastos = snapshot?.documents?.mapNotNull { it.toObject(clsExpense::class.java) } ?: emptyList()
            _listaGastos.postValue(gastos)
        }

        // Listener para la colección de ingresos
        ingresosListener = db.collection("ingresos").addSnapshotListener { snapshot, e ->
            if (e != null) {
                e.printStackTrace()
                return@addSnapshotListener
            }
            val ingresos = snapshot?.documents?.mapNotNull { it.toObject(clsEntry::class.java) } ?: emptyList()
            _listaIngresos.postValue(ingresos)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Eliminar los listeners
        gastosListener?.remove()
        ingresosListener?.remove()
    }

    fun obtenerGastos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resultado = db.collection("gastos").get().await()
                val gastos = resultado.documents.mapNotNull { it.toObject(clsExpense::class.java) }
                _listaGastos.postValue(gastos)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun agregarGasto(expense: clsExpense) {
        expense.id = UUID.randomUUID().toString()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("gastos").document(expense.id).set(expense).await()
                // No es necesario actualizar _listaGastos aquí, ya que el listener lo hará
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun actualizarGasto(expense: clsExpense) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("gastos").document(expense.id).update(expense.toMap()).await()
                _listaGastos.postValue(_listaGastos.value?.map { if (it.id == expense.id) expense else it })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun borrarGasto(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("gastos").document(id).delete().await()
                _listaGastos.postValue(_listaGastos.value?.filter { it.id != id })
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
}
