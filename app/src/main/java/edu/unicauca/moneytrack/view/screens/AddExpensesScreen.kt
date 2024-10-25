package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun AddExpensesScreen(
    navController: NavController,
    moneyViewModel: MoneyViewModel = viewModel(),
) {
    val listaIngresos by moneyViewModel.listaIngresos.observeAsState(emptyList())
    val listaGastos by moneyViewModel.listaGastos.observeAsState(emptyList())

// guardar el estado en el giro de pantalla
    var expenseName by remember  { mutableStateOf("") }
    var expenseValue by remember  { mutableStateOf("") }
    var selectedCategory by remember  { mutableStateOf("") }
    var customCategory by remember  { mutableStateOf("") }
    var selectedReference by remember  { mutableStateOf<clsEntry?>(null) }
    var expandedReference by remember  { mutableStateOf(false) }
    var expandedCategory by remember  { mutableStateOf(false) }
    var errorMessage by remember  { mutableStateOf("") }
    var showSuccessMessage by remember  { mutableStateOf(false) }
    var showDialog by remember  { mutableStateOf(false) }
    var dialogMessage by remember  { mutableStateOf("") }
    var categorias by remember  {
        mutableStateOf(
            mutableListOf(
                "Transporte",
                "Alimentación",
                "Servicios",
                "Arriendo"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Nuevo Gasto",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
        )

        // Dropdown para seleccionar la referencia del gasto
        Card(
            shape = RoundedCornerShape(16.dp), // Borde más redondeado para un look más suave
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // Elevación sutil
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Espaciado más pequeño para mantener la armonía
        ) {
            Column(modifier = Modifier.padding(16.dp)) { // Añadir padding dentro del Card
                // Campo para la referencia
                Box {
                    TextField(
                        value = selectedReference?.nombre ?: "",
                        onValueChange = { },
                        label = { Text("Referencia del Gasto") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                        ),
                        trailingIcon = {
                            IconButton(onClick = { expandedReference = !expandedReference }) {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Reference"
                                )
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

                // Campo para el nombre del gasto
                TextField(
                    value = expenseName,
                    onValueChange = { expenseName = it },
                    label = { Text("Nombre del Gasto") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    )
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
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                        ),
                        trailingIcon = {
                            IconButton(onClick = { expandedCategory = !expandedCategory }) {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Category"
                                )
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
                        if (it.all { char -> char.isDigit() } && it.length <= 7) {
                            expenseValue = it
                        }
                    },
                    label = { Text("Valor en Pesos") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de guardar
        Button(
            onClick = {
                errorMessage = ""
                showSuccessMessage = false
                val valorGasto = expenseValue.toIntOrNull()
                // Validación del nombre del gasto (no debe ser numérico)
                if (expenseName.isBlank() || expenseName.all { it.isDigit() }) {
                    errorMessage = "El nombre del gasto no puede estar vacío ni ser numérico."
                    dialogMessage = errorMessage
                    showDialog = true
                    return@Button
                }
                // Validación de la categoría "Otro" (no debe ser numérica)
                if (selectedCategory == "Otro" && customCategory.all { it.isDigit() }) {
                    errorMessage = "La nueva categoría no puede ser numérica."
                    dialogMessage = errorMessage
                    showDialog = true
                    return@Button
                }
                // Validación del valor del gasto (debe ser un número positivo)
                if (valorGasto == null || valorGasto <= 0) {
                    errorMessage = "El valor del gasto debe ser un número entero positivo."
                    dialogMessage = errorMessage
                    showDialog = true
                    return@Button
                }
                // Si todas las validaciones pasan
                if (selectedReference != null && (selectedCategory.isNotBlank() || customCategory.isNotBlank())) {
                    val nuevaCategoria = if (selectedCategory == "Otro") {
                        if (customCategory.isNotBlank() && !categorias.contains(customCategory)) {
                            customCategory.also {
                                categorias = categorias.toMutableList().apply { add(it) }
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
                    )
                    // Agregar el nuevo gasto
                    moneyViewModel.agregarGasto(nuevoGasto)
                    // Limpiar los campos
                    expenseName = ""
                    expenseValue = ""
                    selectedCategory = ""
                    customCategory = ""
                    selectedReference = null
                    // Mostrar el cuadro de diálogo de éxito
                    dialogMessage = "Gasto guardado exitosamente"
                    showDialog = true
                    showSuccessMessage = true
                } else {
                    errorMessage =
                        "Por favor ingresa un nombre válido, un valor numérico, selecciona una referencia y una categoría."
                    dialogMessage = errorMessage
                    showDialog = true
                }

            },
            modifier = Modifier
                .width(150.dp)
                .height(40.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A65D8))
        ) {
            Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
        }

        // Cuadro de diálogo emergente para éxito o error
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { /* No hacer nada aquí, se cerrará automáticamente */ },
                title = { Text(if (showSuccessMessage) "Éxito" else "Error") },
                text = { Text(dialogMessage) },
                confirmButton = { TextButton(onClick = { showDialog = false }) { Text("Cerrar") } },
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        }

        LaunchedEffect(showDialog) {
            if (showDialog) {
                delay(3000)
                showDialog = false
                navController.navigateUp()
            }
        }
    }
}
