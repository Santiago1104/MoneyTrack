package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel

@Composable
fun TransactionHistoryScreen(
    navController: NavController,
    moneyViewModel: MoneyViewModel = viewModel()
) {
    // Obteniendo las listas de ingresos y gastos del ViewModel
    val ingresos by moneyViewModel.listaIngresos.observeAsState(emptyList())
    val gastos by moneyViewModel.listaGastos.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Historial",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "A continuación, se presentan los gastos e ingresos históricos.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // LazyColumn combinada para Ingresos y Gastos
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Sección de ingresos
            item {
                Text(
                    text = "Ingresos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            items(ingresos) { ingreso ->
                TransactionItem(
                    name = ingreso.nombre,
                    amount = "+${ingreso.valor}",
                    type = "Ingreso",
                    date = ingreso.fecha,
                    isPositive = true,
                    onClick = { navController.navigate("editIngreso/${ingreso.id}") }
                )
            }

            // Divisor entre ingresos y gastos
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.Gray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sección de gastos
            item {
                Text(
                    text = "Gastos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            items(gastos) { gasto ->
                TransactionItem(
                    name = gasto.nombre,
                    amount = "-${gasto.valor}",
                    type = "Gasto",
                    date = gasto.fecha,
                    isPositive = false,
                    onClick = { navController.navigate("editGasto/${gasto.id}") }
                )
            }
        }
    }
}


@Composable
fun TransactionItem(
    name: String,
    amount: String,
    type: String,
    date: String,
    isPositive: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = type, color = Color.Gray, fontSize = 14.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = amount,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (isPositive) Color(0xFF00C853) else Color(0xFFD50000) // Verde o rojo según el valor
                    )
                    Text(text = date, color = Color.Gray, fontSize = 14.sp)
                }
            }
        }
    }
}
