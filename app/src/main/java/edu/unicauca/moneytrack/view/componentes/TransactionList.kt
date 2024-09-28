package edu.unicauca.moneytrack.view.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Transaction(
    val nombre: String,
    val valor: Double,
    val fecha: String,
    val esPositivo: Boolean
)

@Composable
fun TransactionList(
    title: String,
    transactions: List<Transaction>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(transactions) { transaction ->
                TransactionItem(
                    name = transaction.nombre,
                    amount = if (transaction.esPositivo) "+${transaction.valor}" else "-${transaction.valor}",
                    type = if (transaction.esPositivo) "Ingreso" else "Gasto",
                    date = transaction.fecha,
                    isPositive = transaction.esPositivo
                )
            }
        }
    }
}

@Composable
fun TransactionItem(name: String, amount: String, type: String, date: String, isPositive: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
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
