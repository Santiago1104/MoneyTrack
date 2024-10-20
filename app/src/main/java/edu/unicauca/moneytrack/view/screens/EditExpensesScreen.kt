package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.unicauca.moneytrack.model.clsEntry
import edu.unicauca.moneytrack.model.clsExpense
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel

@Composable
fun EditExpensesScreen(
    navController: NavController,
    moneyViewModel: MoneyViewModel = viewModel(),
    expenseId: String // Necesitamos el ID del gasto a editar
) {
    val expense by moneyViewModel.getExpenseByIdLive(expenseId).observeAsState()

    // Si no se encuentra el gasto, podemos mostrar un mensaje o regresar
    if (expense == null) {
        Text("Gasto no encontrado.")
        return
    }

    // Variables de estado para los campos editables
    var expenseName by remember { mutableStateOf(expense?.nombre ?: "") }
    var expenseValue by remember { mutableStateOf(expense?.valor?.toString() ?: "") }
    var selectedReference by remember { mutableStateOf<clsEntry?>(null) } // Referencia del gasto
    var selectedCategory by remember { mutableStateOf(expense?.categoria ?: "") }
    var errorMessage by remember { mutableStateOf("") }

    // Obtener la lista de referencias del ViewModel
    val listaIngresos by moneyViewModel.listaIngresos.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Editar Gasto",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo para modificar el nombre del gasto
        TextField(
            value = expenseName,
            onValueChange = { expenseName = it },
            label = { Text("Nombre del Gasto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para modificar el valor del gasto
        TextField(
            value = expenseValue,
            onValueChange = { expenseValue = it },
            label = { Text("Valor en Pesos") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selección de referencia con Dropdown
        var expandedReference by remember { mutableStateOf(false) }

        Box {
            TextField(
                value = selectedReference?.nombre ?: "", // Mostrar el nombre de la referencia seleccionada
                onValueChange = { /* Campo de referencia solo se selecciona */ },
                label = { Text("Referencia del Gasto") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true, // El campo es solo de lectura
                trailingIcon = {
                    IconButton(onClick = { expandedReference = !expandedReference }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Reference")
                    }
                }
            )
            DropdownMenu(
                expanded = expandedReference,
                onDismissRequest = { expandedReference = false }
            ) {
                listaIngresos.forEach { ingreso ->
                    DropdownMenuItem(
                        text = { Text(text = ingreso.nombre) }, // Mostrar el nombre del ingreso
                        onClick = {
                            selectedReference = ingreso // Asignar el ingreso seleccionado como referencia
                            expandedReference = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para modificar la categoría del gasto
        TextField(
            value = selectedCategory,
            onValueChange = { selectedCategory = it },
            label = { Text("Categoría del Gasto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Mostrar mensaje de error si hay alguno
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        // Botón para guardar los cambios
        Button(
            onClick = {
                // Limpiar mensaje de error antes de realizar las validaciones
                errorMessage = ""

                val valorGasto = expenseValue.toDoubleOrNull()

                // Validaciones
                when {
                    expenseName.isBlank() -> {
                        errorMessage = "Por favor ingresa un nombre válido."
                    }
                    valorGasto == null -> {
                        errorMessage = "Por favor ingresa un valor numérico válido."
                    }
                    valorGasto < 0 -> {
                        errorMessage = "No se permiten valores negativos."
                    }
                    expenseValue.length > 7 -> {
                        errorMessage = "El valor no puede tener más de 7 dígitos."
                    }
                    !expenseValue.matches(Regex("^[0-9]+\$")) -> {
                        errorMessage = "Solo se permiten números enteros."
                    }
                    selectedCategory.isBlank() -> {
                        errorMessage = "Por favor ingresa una categoría."
                    }
                    selectedReference == null -> {
                        errorMessage = "Por favor selecciona una referencia."
                    }
                    else -> {
                        val nuevoGasto = expense!!.copy(
                            nombre = expenseName,
                            valor = valorGasto,
                            referencia = selectedReference?.nombre ?: "", // Usar la referencia seleccionada
                            categoria = selectedCategory
                        )
                        moneyViewModel.actualizarGasto(nuevoGasto) // Llamando al método de ViewModel
                        navController.popBackStack() // Regresar a la pantalla anterior
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Cambios")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para eliminar el gasto
        Button(
            onClick = {
                moneyViewModel.borrarGasto(expense!!.id) // Eliminar el gasto usando el ViewModel y restituir el dinero
                navController.popBackStack() // Regresar a la pantalla anterior
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar Gasto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver al Home
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
