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
import edu.unicauca.moneytrack.model.clsExpense
import edu.unicauca.moneytrack.model.clsEntry
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel
import java.util.UUID

@Composable
fun AddExpensesScreen(
    navController: NavController,
    moneyViewModel: MoneyViewModel = viewModel()
) {
    // Obtener la lista de ingresos del ViewModel
    val listaIngresos by moneyViewModel.listaIngresos.observeAsState(emptyList())

    // Variables de estado para los campos
    var expenseName by remember { mutableStateOf("") }
    var expenseValue by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var customCategory by remember { mutableStateOf("") }
    var selectedReference by remember { mutableStateOf<clsEntry?>(null) } // Cambiado a clsEntry
    var expandedReference by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) } // Variable para el feedback de éxito

    // Lista de categorías predefinidas
    val categorias = listOf("Arriendo", "Servicios", "Compras", "Transporte", "Otro")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Añadir Gasto",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Dropdown para seleccionar la referencia de que ingreso sale el gasto
        Box {
            TextField(
                value = selectedReference?.nombre ?: "", // Mostrar el nombre si la referencia está seleccionada
                onValueChange = { },
                label = { Text("Referencia del Gasto") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true, // Solo lectura
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
                        text = { Text(ingreso.nombre) }, // Mostrar el nombre del ingreso
                        onClick = {
                            selectedReference = ingreso // Almacena el ingreso seleccionado completo
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
                readOnly = selectedCategory != "Otro", // Solo permite escritura si selecciona "Otro"
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
                            if (categoria != "Otro") customCategory = "" // Limpia la categoría personalizada si no es "Otro"
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para ingresar el valor del gasto
        TextField(
            value = expenseValue,
            onValueChange = { expenseValue = it },
            label = { Text("Valor en Pesos") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Mostrar mensaje de error si los campos no son válidos
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        // Mostrar mensaje de éxito si se guarda correctamente
        if (showSuccessMessage) {
            Text(text = "Gasto guardado exitosamente", color = MaterialTheme.colorScheme.primary)
        }

// Obtener el dinero actual desde el ViewModel
        val dineroActual by moneyViewModel.dinero.observeAsState(initial = null)

// ...

        Button(
            onClick = {
                // Limpiar error y mensaje de éxito al iniciar el guardado
                errorMessage = ""
                showSuccessMessage = false

                val valorGasto = expenseValue.toDoubleOrNull()

                if (expenseName.isNotBlank() && valorGasto != null && selectedReference != null && (selectedCategory.isNotBlank() || customCategory.isNotBlank())) {
                    // Crear un nuevo gasto
                    val nuevaCategoria = if (selectedCategory == "Otro") customCategory else selectedCategory
                    val nuevoGasto = clsExpense(
                        id = UUID.randomUUID().toString(),
                        nombre = expenseName,
                        valor = valorGasto,
                        referencia = selectedReference?.nombre ?: "", // Asociar la referencia (nombre del ingreso)
                        categoria = nuevaCategoria
                    )
                    try {
                        moneyViewModel.agregarGasto(nuevoGasto) // Llamar al ViewModel para agregar el gasto

                        // Limpiar campos
                        expenseName = ""
                        expenseValue = ""
                        selectedCategory = ""
                        customCategory = ""
                        selectedReference = null // Limpiar la referencia seleccionada

                        // Mostrar mensaje de éxito
                        showSuccessMessage = true
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Error desconocido al agregar el gasto."
                    }
                } else {
                    errorMessage = "Por favor ingresa un nombre válido, un valor numérico, selecciona una referencia y una categoría."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = (dineroActual?.total ?: 0.0) > (expenseValue.toDoubleOrNull() ?: 0.0) // Habilitar solo si hay suficiente dinero
        ) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver a la pantalla anterior
        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
