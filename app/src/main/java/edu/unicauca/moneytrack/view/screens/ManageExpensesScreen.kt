package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import edu.unicauca.moneytrack.model.clsExpense

@Composable
fun ManageExpensesScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Administrador de Gastos",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = {
                // Navega a la pantalla de "Mis Gastos"
                navController.navigate("mis_gastos")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Mis Gastos")
        }

        Button(
            onClick = {
                // Navega a la pantalla de "Añadir Gasto"
                navController.navigate("add_expense")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Añadir Gasto")
        }

        Button(
            onClick = {
                // Navega a la pantalla de "Modificar Gasto"
                navController.navigate("edit_expense")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Modificar Gasto")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewExpenseManagerScreen() {
    val navController = rememberNavController() // Controlador de navegación para preview
    ManageExpensesScreen(navController = navController)
}
//pantalla de mis gastos
@Composable
fun MisGastosScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mis Gastos",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(text = "Lista de Gastos")
        Button(onClick = {
            // Navegar a otra pantalla si es necesario
            navController.navigateUp() // Regresa a la pantalla anterior
        }) {
            Text("Volver")
        }

    }
}

