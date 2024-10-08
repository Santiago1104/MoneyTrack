package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
fun AddExpensesScreen(navController: NavController) {
    val viewModel: MoneyViewModel = viewModel() // Obtener el ViewModel
    var expenseName by remember { mutableStateOf("") }
    var expenseValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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

        // Campo para ingresar el nombre del gasto
        TextField(
            value = expenseName,
            onValueChange = { expenseName = it },
            label = { Text("Nombre del Gasto") },
            modifier = Modifier.fillMaxWidth()
        )

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

                if (expenseName.isNotBlank() && valorGasto != null) {
                    // Crear un nuevo gasto
                    val nuevoGasto = clsExpense(
                        id = UUID.randomUUID().toString(),
                        nombre = expenseName,
                        valor = valorGasto
                    )
                    viewModel.agregarGasto(nuevoGasto) // Llamar al ViewModel para agregar el gasto
                    expenseName = "" // Limpiar el campo de nombre
                    expenseValue = "" // Limpiar el campo de valor
                    navController.navigateUp() // Navegar de vuelta a la pantalla anterior
                } else {
                    errorMessage = "Por favor ingresa un nombre válido y un valor numérico."
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
