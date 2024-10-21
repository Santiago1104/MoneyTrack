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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.unicauca.moneytrack.model.clsEntry
import edu.unicauca.moneytrack.model.clsExpense
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel
import java.util.UUID


import java.text.SimpleDateFormat
import java.util.Date

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

    // Inicializar los campos del formulario con los datos del gasto
    var expenseName by remember(expenseToEdit) { mutableStateOf(expenseToEdit?.nombre ?: "") }
    var expenseValue by remember(expenseToEdit) { mutableStateOf(expenseToEdit?.valor?.toInt()?.toString() ?: "") }
    var selectedCategory by remember(expenseToEdit) { mutableStateOf(expenseToEdit?.categoria ?: "") }
    var customCategory by remember(expenseToEdit) { mutableStateOf("") }
    var selectedReference by remember(expenseToEdit) { mutableStateOf<clsEntry?>(null) }
    var expandedReference by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) }

    val categorias = mutableListOf("Transporte", "Alimentación", "Servicios", "Arriendo")

    // Cargar la referencia actual una vez que se carga el gasto
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

        // Contenedor para los botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Espaciado entre botones
        ) {
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

                        // Obtener la fecha actual en formato personalizado
                        val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
                        val fechaActual = formatoFecha.format(Date())

                        // Actualizar el gasto con la fecha formateada
                        val updatedGasto = expenseToEdit?.copy(
                            nombre = expenseName,
                            valor = valorGasto.toDouble(),
                            referencia = selectedReference?.nombre ?: "",
                            categoria = nuevaCategoria,
                            fecha = fechaActual // Cambiar la fecha a la fecha actual en el formato deseado
                        )

                        if (updatedGasto != null) {
                            moneyViewModel.actualizarGasto(updatedGasto) // Actualizar gasto
                            showSuccessMessage = true
                            navController.navigateUp() // Navegar hacia atrás después de guardar
                        }
                    } else {
                        errorMessage = "Por favor ingresa un nombre válido, un valor numérico, selecciona una referencia y una categoría."
                    }
                },
                modifier = Modifier.weight(1f), // Para que tome el mismo espacio que el botón de eliminar
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A65D8)) // Color personalizado
            ) {
                Text("Guardar")
            }

            Spacer(modifier = Modifier.width(16.dp)) // Espacio entre botones

            // Botón para eliminar el gasto
            Button(
                onClick = {
                    expenseToEdit?.let {
                        moneyViewModel.borrarGasto(it.id) // Eliminar el gasto
                        navController.navigateUp() // Volver a la pantalla anterior después de eliminar
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar Gasto")
            }
        }
    }
}