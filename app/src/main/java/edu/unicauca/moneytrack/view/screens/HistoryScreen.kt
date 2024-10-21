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

    // Combinando ingresos y gastos en una sola lista y ordenando por fecha
    val transacciones = (ingresos.map { it to "Ingreso" } + gastos.map { it to "Gasto" })
        .sortedByDescending { it.first.fecha }  // Ordenar por fecha descendente

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Historial de Transacciones",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "A continuación, se presentan los movimientos históricos.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // LazyColumn combinada para Ingresos y Gastos
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(transacciones) { (transaccion, tipo) ->
                val isPositive = tipo == "Ingreso"
                val amountSign = if (isPositive) "+" else "-"

                TransactionItem(
                    name = transaccion.nombre,
                    amount = "$amountSign${transaccion.valor}",
                    type = tipo,
                    date = transaccion.fecha,
                    isPositive = isPositive,
                    onClick = {
                        if (isPositive) {
                            navController.navigate("editIngreso/${transaccion.id}")
                        } else {
                            navController.navigate("editGasto/${transaccion.id}")
                        }
                    }
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
