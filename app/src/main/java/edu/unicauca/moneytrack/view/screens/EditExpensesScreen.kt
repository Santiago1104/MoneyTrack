package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.unicauca.moneytrack.model.clsEntry
import edu.unicauca.moneytrack.model.clsExpense
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel
import java.util.UUID

import java.text.SimpleDateFormat
import java.util.Date
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.saveable.rememberSaveable


@Composable
fun EditExpensesScreen(
    navController: NavController, expenseId: String, moneyViewModel: MoneyViewModel = viewModel()
) {
    val listaGastos by moneyViewModel.listaGastos.observeAsState(emptyList())
    val listaIngresos by moneyViewModel.listaIngresos.observeAsState(emptyList())

    val expenseToEdit = listaGastos.find { it.id == expenseId }

    var expenseName by rememberSaveable(expenseToEdit) { mutableStateOf(expenseToEdit?.nombre ?: "") }
    var expenseValue by rememberSaveable(expenseToEdit) {
        mutableStateOf(
            expenseToEdit?.valor?.toInt()?.toString() ?: ""
        )
    }
    var selectedCategory by rememberSaveable(expenseToEdit) {
        mutableStateOf(
            expenseToEdit?.categoria ?: ""
        )
    }
    var customCategory by rememberSaveable(expenseToEdit) { mutableStateOf("") }
    var selectedReference by rememberSaveable(expenseToEdit) { mutableStateOf<clsEntry?>(null) }
    var expandedReference by rememberSaveable { mutableStateOf(false) }
    var expandedCategory by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var showSuccessMessage by rememberSaveable { mutableStateOf(false) }

    val categorias = mutableListOf("Transporte", "Alimentación", "Servicios", "Arriendo")

    LaunchedEffect(expenseToEdit) {
        selectedReference = listaIngresos.find { it.nombre == expenseToEdit?.referencia }
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
            text = "Editar Gasto",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp), // Borde más redondeado para un look más suave
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // Elevación sutil
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp), // Margen interno más amplio para dar más espacio
            ) {
                // Campo de nombre del gasto con validación
                Box {
                    TextField(value = expenseName, onValueChange = {
                        if (!it.all { char -> char.isDigit() }) { // Validar que no sea numérico
                            expenseName = it
                        } else {
                            errorMessage = "El nombre del gasto no puede ser numérico."
                        }
                    }, label = {
                        Text(
                            "Nombre del Gasto",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Selección de categoría
                Box {
                    TextField(
                        value = if (selectedCategory == "Otro") customCategory else selectedCategory,
                        onValueChange = {
                            if (selectedCategory == "Otro") {
                                if (!it.all { char -> char.isDigit() }) { // Validar que no sea numérico
                                    customCategory = it
                                } else {
                                    errorMessage = "La categoría no puede ser numérica."
                                }
                            }
                        },
                        label = {
                            Text(
                                "Categoría", color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = selectedCategory != "Otro",
                        trailingIcon = {
                            IconButton(onClick = { expandedCategory = !expandedCategory }) {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Seleccionar Categoría"
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    DropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }) {
                        categorias.forEach { categoria ->
                            DropdownMenuItem(text = { Text(text = categoria) }, onClick = {
                                selectedCategory = categoria
                                expandedCategory = false
                                if (categoria != "Otro") customCategory = ""
                            })
                        }
                        DropdownMenuItem(text = { Text(text = "Otro") }, onClick = {
                            selectedCategory = "Otro"
                            expandedCategory = false
                        })
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de valor del gasto
                TextField(value = expenseValue,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() } && it.length <= 7) { // Validar solo números enteros no negativos
                            expenseValue = it
                        } else {
                            errorMessage = "El valor debe ser un número entero no negativo."
                        }
                    },
                    label = {
                        Text(
                            "Valor en Pesos", color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Mensaje de error
                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    errorMessage = ""
                    showSuccessMessage = false
                    val valorGasto = expenseValue.toIntOrNull()

                    if (expenseName.isNotBlank() && valorGasto != null && selectedReference != null && (selectedCategory.isNotBlank() || customCategory.isNotBlank())) {

                        val nuevaCategoria = if (selectedCategory == "Otro") {
                            if (customCategory.isNotBlank() && !categorias.contains(
                                    customCategory
                                )
                            ) {
                                customCategory.also { categorias.add(it) }
                            } else {
                                errorMessage = "Por favor ingresa un nombre de categoría válido."
                                return@Button
                            }
                        } else {
                            selectedCategory
                        }

                        val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
                        val fechaActual = formatoFecha.format(Date())

                        val updatedGasto = expenseToEdit?.copy(
                            nombre = expenseName,
                            valor = valorGasto.toDouble(),
                            referencia = selectedReference?.nombre ?: "",
                            categoria = nuevaCategoria,
                            fecha = fechaActual
                        )

                        if (updatedGasto != null) {
                            moneyViewModel.actualizarGasto(updatedGasto)
                            showSuccessMessage = true
                            navController.navigateUp()
                        }
                    } else {
                        errorMessage = "Por favor completa todos los campos correctamente."
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A65D8)) // Color personalizado
            ) {
                Text("Guardar")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    expenseToEdit?.let {
                        moneyViewModel.borrarGasto(it.id)
                        navController.navigateUp()
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
