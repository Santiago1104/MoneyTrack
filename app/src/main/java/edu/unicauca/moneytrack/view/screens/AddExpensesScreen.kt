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
import edu.unicauca.moneytrack.model.clsExpense
import edu.unicauca.moneytrack.model.clsEntry
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.AlertDialog

@Composable
fun AddExpensesScreen(
    navController: NavController,
    moneyViewModel: MoneyViewModel = viewModel(),
) {
    // Obtener la lista de ingresos y gastos del ViewModel
    val listaIngresos by moneyViewModel.listaIngresos.observeAsState(emptyList())
    val listaGastos by moneyViewModel.listaGastos.observeAsState(emptyList())

    // Variables de estado para los campos
    var expenseName by remember { mutableStateOf("") }
    var expenseValue by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var customCategory by remember { mutableStateOf("") }
    var selectedReference by remember { mutableStateOf<clsEntry?>(null) }
    var expandedReference by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) } // Controla el cuadro de diálogo emergente
// Mensaje del cuadro de diálogo
    var dialogMessage by remember { mutableStateOf("") }
    var categorias by remember { mutableStateOf(mutableListOf("Transporte", "Alimentación", "Servicios", "Arriendo")) }

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
            ) {                // Mostrar categorías existentes
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
        // Botón de guardar
        Button(
            onClick = {
                errorMessage = ""
                showSuccessMessage = false
                val valorGasto = expenseValue.toIntOrNull()

                // Validaciones
                if (expenseName.isNotBlank() && valorGasto != null && selectedReference != null &&
                    (selectedCategory.isNotBlank() || customCategory.isNotBlank())) {
                    // Validar y agregar la nueva categoría a la lista si es necesario
                    val nuevaCategoria = if (selectedCategory == "Otro") {
                        if (customCategory.isNotBlank() && !categorias.contains(customCategory)) {
                            customCategory.also {
                                categorias = categorias.toMutableList().apply { add(it) } // Agregar la nueva categoría a la lista
                            }
                        } else {
                            errorMessage = "Por favor ingresa un nombre de categoría válido."
                            return@Button
                        }
                    } else {
                        selectedCategory
                    }

                    // Crear un nuevo gasto
                    val nuevoGasto = clsExpense(
                        id = UUID.randomUUID().toString(),
                        nombre = expenseName,
                        valor = valorGasto.toDouble(),
                        referencia = selectedReference?.nombre ?: "",
                        categoria = nuevaCategoria
                    )    // Agregar el nuevo gasto
                    moneyViewModel.agregarGasto(nuevoGasto)     // Limpiar los campos
                    expenseName = ""
                    expenseValue = ""
                    selectedCategory = ""
                    customCategory = ""
                    selectedReference = null// Mostrar el cuadro de diálogo de éxito
                    dialogMessage = "Gasto guardado exitosamente"
                    showDialog = true
                    showSuccessMessage = true
                } else {
                    errorMessage = "Por favor ingresa un nombre válido, un valor numérico, selecciona una referencia y una categoría."
                    dialogMessage = errorMessage // Actualizar el mensaje de error
                    showDialog = true // Mostrar el cuadro de diálogo de error
                }
            },  modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A65D8)) // Color personalizado
        ) {
            Text("Guardar")
        }
        // Cuadro de diálogo emergente para éxito o error
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { /* No hacer nada aquí, se cerrará automáticamente */ },
                title = { Text(if (showSuccessMessage) "Éxito" else "Error") },
                text = { Text(dialogMessage) },
                //necesito eliminat esto
                confirmButton = { TextButton(onClick = { showDialog = false }) { Text("Cerrar") } }
            )
        }
// Después de 6 segundos, cerrar el diálogo y navegar hacia atrás
        LaunchedEffect(showDialog) {
            if (showDialog) {
                delay(6000) // Espera de 6 segundos
                showDialog = false
                navController.navigateUp() // Navegar hacia atrás
            }
        }
    }
}