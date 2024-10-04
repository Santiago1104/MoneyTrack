package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun AddExpensesScream(navController: NavController) {
    var expenseName by remember { mutableStateOf("") }
    var expenseValue by remember { mutableStateOf("") }

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

        TextField(
            value = expenseName,
            onValueChange = { expenseName = it },
            label = { Text("Nombre del Gasto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = expenseValue,
            onValueChange = { expenseValue = it },
            label = { Text("Valor en Pesos") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Aquí puedes agregar la lógica para guardar el gasto en Firebase
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigateUp() }, // Regresa a la pantalla anterior
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddExpensesScreen() {
    val navController = rememberNavController()
    AddExpensesScream(navController = navController)
}
