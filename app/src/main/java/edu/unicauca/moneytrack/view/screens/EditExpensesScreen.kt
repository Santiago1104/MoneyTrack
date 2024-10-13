package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
    //var expenseReference by remember { mutableStateOf(expense?.referencia ?: "") }
    var selectedCategory by remember { mutableStateOf(expense?.categoria ?: "") }
    var errorMessage by remember { mutableStateOf("") }

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

        // Campo para modificar la referencia del gasto
        /*TextField(
            value = expenseReference,
            onValueChange = { expenseReference = it },
            label = { Text("Referencia del Gasto") },
            modifier = Modifier.fillMaxWidth()
        )*/

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
                val valorGasto = expenseValue.toDoubleOrNull()

                if (expenseName.isNotBlank() && valorGasto != null && selectedCategory.isNotBlank()) {
                    val nuevoGasto = expense!!.copy(
                        nombre = expenseName,
                        valor = valorGasto,
                //        referencia = expenseReference,
                        categoria = selectedCategory
                    )
                    moneyViewModel.actualizarGasto(nuevoGasto) // Llamando al método de ViewModel
                    navController.navigate("adminGastos") // Navegando de vuelta a la pantalla de administración de gastos
                } else {
                    errorMessage = "Por favor ingresa un nombre válido, un valor numérico y una categoría."
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
                moneyViewModel.borrarGasto(expense!!.id) // Eliminar el gasto usando el ViewModel
                navController.navigate("adminGastos") // Regresar a la pantalla de administración de gastos
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar Gasto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver a la pantalla anterior
        Button(
            onClick = { navController.navigate("adminGastos") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a Administrar Gastos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver al Home
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Home")
        }
    }
}
