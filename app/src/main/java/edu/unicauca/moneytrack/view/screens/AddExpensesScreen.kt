package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.unicauca.moneytrack.model.clsExpense
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel
import java.util.UUID

@Composable
fun AddExpensesScreen(
    navController: NavController,
    referencia: String? = null, // Recibe la referencia desde otra pantalla
) {
    // Inicializa el ViewModel
    val moneyViewModel: MoneyViewModel = viewModel() // Asegúrate de que MoneyViewModel esté bien definido

    // Variables de estado para los campos
    var reference by remember { mutableStateOf(referencia ?: "") } // Inicializa con referencia si se proporciona
    var expenseName by remember { mutableStateOf("") }
    var expenseValue by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var customCategory by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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

        // Campo de referencia (puede estar prellenado)
        TextField(
            value = reference,
            onValueChange = { reference = it }, // Permite cambiar la referencia
            label = { Text("Referencia del Gasto") },
            modifier = Modifier.fillMaxWidth()
        )

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
        var expanded by remember { mutableStateOf(false) }

        Box {
            TextField(
                value = if (selectedCategory == "Otro") customCategory else selectedCategory,
                onValueChange = { if (selectedCategory == "Otro") customCategory = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = selectedCategory != "Otro", // Solo permite escritura si selecciona "Otro"
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Category")
                    }
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categorias.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(text = categoria) },
                        onClick = {
                            selectedCategory = categoria
                            expanded = false
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

        // Botón para guardar el gasto
        Button(
            onClick = {
                val valorGasto = expenseValue.toDoubleOrNull()

                if (expenseName.isNotBlank() && valorGasto != null && (selectedCategory.isNotBlank() || customCategory.isNotBlank())) {
                    // Crear un nuevo gasto
                    val nuevaCategoria = if (selectedCategory == "Otro") customCategory else selectedCategory
                    val nuevoGasto = clsExpense(
                        id = UUID.randomUUID().toString(),
                        nombre = expenseName,
                        valor = valorGasto,
                        referencia = reference, // Usar la referencia
                        categoria = nuevaCategoria // Usar la categoría seleccionada
                    )
                    moneyViewModel.agregarGasto(nuevoGasto) // Llama al ViewModel para agregar el gasto
                    // Limpiar campos
                    reference = ""
                    expenseName = ""
                    expenseValue = ""
                    selectedCategory = ""
                    customCategory = ""
                } else {
                    errorMessage = "Por favor ingresa un nombre válido, un valor numérico y selecciona una categoría."
                }
            },
            modifier = Modifier.fillMaxWidth()
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
