package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
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
@Composable
fun AddExpensesScreen(
    navController: NavController,
    moneyViewModel: MoneyViewModel = viewModel(),
) {
    val listaIngresos by moneyViewModel.listaIngresos.observeAsState(emptyList())
    val listaGastos by moneyViewModel.listaGastos.observeAsState(emptyList())

    var expenseName by remember { mutableStateOf("") }
    var expenseValue by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var customCategory by remember { mutableStateOf("") }
    var selectedReference by remember { mutableStateOf<clsEntry?>(null) }
    var expandedReference by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var categorias by remember { mutableStateOf(mutableListOf("Transporte", "Alimentación", "Servicios", "Arriendo")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Añadir Gasto",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp),
            color = MaterialTheme.colorScheme.primary
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
                // Lógica de validación y guardado
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
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