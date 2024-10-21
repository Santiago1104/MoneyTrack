package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
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
import java.util.UUID
@Composable
fun EditExpensesScreen(
    navController: NavController,
    expenseId: String,
    moneyViewModel: MoneyViewModel = viewModel()
) {
    // Obtener la lista de gastos y referencias del ViewModel
    val listaGastos by moneyViewModel.listaGastos.observeAsState(emptyList())
    val listaIngresos by moneyViewModel.listaIngresos.observeAsState(emptyList())

    // Buscar el gasto que se va a editar
    val expenseToEdit = listaGastos.find { it.id == expenseId }

    // Variables de estado para los campos
    var expenseName by remember { mutableStateOf(expenseToEdit?.nombre ?: "") }
    var expenseValue by remember { mutableStateOf(expenseToEdit?.valor?.toInt()?.toString() ?: "") }
    var selectedCategory by remember { mutableStateOf(expenseToEdit?.categoria ?: "") }
    var customCategory by remember { mutableStateOf("") }
    var selectedReference by remember { mutableStateOf<clsEntry?>(null) }
    var expandedReference by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) }

    val categorias = mutableListOf("Transporte", "Alimentación", "Servicios", "Arriendo")

    // Cargar la referencia actual
    LaunchedEffect(expenseToEdit) {
        selectedReference = listaIngresos.find { it.nombre == expenseToEdit?.referencia }
    }

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

        // Dropdown para seleccionar la referencia del gasto
        Box {
            TextField(
                value = selectedReference?.nombre ?: "",
                onValueChange = { },
                label = { Text("Referencia del Gasto") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
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
                        text = { Text(ingreso.nombre) },
                        onClick = {
                            selectedReference = ingreso
                            expandedReference = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de nombre del gasto
        TextField(
            value = expenseName,
            onValueChange = { expenseName = it },
            label = { Text("Nombre del Gasto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selección de categoría con Dropdown
        Box {
            TextField(
                value = if (selectedCategory == "Otro") customCategory else selectedCategory,
                onValueChange = { if (selectedCategory == "Otro") customCategory = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = selectedCategory != "Otro",
                trailingIcon = {
                    IconButton(onClick = { expandedCategory = !expandedCategory }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Category")
                    }
                }
            )
            DropdownMenu(
                expanded = expandedCategory,
                onDismissRequest = { expandedCategory = false }
            ) {
                categorias.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(text = categoria) },
                        onClick = {
                            selectedCategory = categoria
                            expandedCategory = false
                            if (categoria != "Otro") customCategory = ""
                        }
                    )
                }
                // Opción para agregar una nueva categoría
                DropdownMenuItem(
                    text = { Text(text = "Otro") },
                    onClick = {
                        selectedCategory = "Otro"
                        expandedCategory = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para ingresar el valor del gasto
        TextField(
            value = expenseValue,
            onValueChange = {
                // Validar que solo se ingresen números enteros y que no sean negativos
                if (it.all { char -> char.isDigit() } && it.length <= 7) {
                    expenseValue = it
                }
            },
            label = { Text("Valor en Pesos") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Mensajes de error o éxito
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
        if (showSuccessMessage) {
            Text(text = "Gasto actualizado exitosamente", color = MaterialTheme.colorScheme.primary)
        }

        // Botón para actualizar el gasto
        Button(
            onClick = {
                errorMessage = ""
                showSuccessMessage = false
                val valorGasto = expenseValue.toIntOrNull()

                // Validaciones
                if (expenseName.isNotBlank() && valorGasto != null && selectedReference != null &&
                    (selectedCategory.isNotBlank() || customCategory.isNotBlank())) {

                    // Validar que la nueva categoría no esté repetida
                    val nuevaCategoria = if (selectedCategory == "Otro") {
                        if (customCategory.isNotBlank() && !categorias.contains(customCategory)) {
                            customCategory.also { categorias.add(it) } // Agregar nueva categoría a la lista
                        } else {
                            errorMessage = "Por favor ingresa un nombre de categoría válido."
                            return@Button
                        }
                    } else {
                        selectedCategory
                    }

                    // Actualizar el gasto
                    val updatedGasto = expenseToEdit?.copy(
                        nombre = expenseName,
                        valor = valorGasto.toDouble(),
                        referencia = selectedReference?.nombre ?: "",
                        categoria = nuevaCategoria
                    )

                    if (updatedGasto != null) {
                        moneyViewModel.actualizarGasto(updatedGasto) // Actualizar gasto

                        // Limpiar campos
                        expenseName = ""
                        expenseValue = ""
                        selectedCategory = ""
                        customCategory = ""
                        selectedReference = null

                        // Actualizar la lista de gastos
                        moneyViewModel.obtenerGastos() // Actualizar lista de gastos después de agregar

                        showSuccessMessage = true
                    }
                } else {
                    errorMessage = "Por favor ingresa un nombre válido, un valor numérico, selecciona una referencia y una categoría."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para eliminar el gasto
        Button(
            onClick = {
                expenseToEdit?.let {
                    moneyViewModel.borrarGasto(it.id) // Eliminar el gasto
                    navController.navigateUp() // Volver a la pantalla anterior después de eliminar
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Eliminar Gasto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver
        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
