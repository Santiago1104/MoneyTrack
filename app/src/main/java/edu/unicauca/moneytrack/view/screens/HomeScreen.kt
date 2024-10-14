package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import edu.unicauca.moneytrack.R
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel

@Composable
fun HomeScreen(
    viewModel: MoneyViewModel,
    onAddGastoClick: () -> Unit,
    onAddIngresoClick: () -> Unit,
    onEditIncomeClick: (String) -> Unit,
    onEditGastoClick: (String) -> Unit
) {
    val dineroActual by viewModel.dinero.observeAsState(initial = null)
    val ingresos by viewModel.listaIngresos.observeAsState(emptyList())
    val gastos by viewModel.listaGastos.observeAsState(emptyList())
    val totalIngresos = ingresos.sumOf { it.valor }
    val totalGastos = gastos.sumOf { it.valor }

    var showDialog by remember { mutableStateOf(false) }
    var financialAdvice by remember { mutableStateOf("") }


    when {
        totalIngresos > totalGastos -> {
            financialAdvice = """
                ¡Felicitaciones! Estás ganando más de lo que gastas.
                Aquí algunos consejos:
                1. Considera aumentar tus ahorros o invertir ese excedente.
                2. Usa tus ingresos extra para pagar deudas si las tienes.
                3. Revisa oportunidades para generar ingresos pasivos.
            """.trimIndent()
        }
        totalGastos > totalIngresos -> {
            financialAdvice = """
                Cuidado, estás gastando más de lo que ganas.
                Aquí algunos consejos:
                1. Revisa tus gastos y elimina aquellos innecesarios.
                2. Crea un presupuesto y ajusta tu estilo de vida para no gastar de más.
                3. Considera aumentar tus ingresos buscando un trabajo adicional o freelance.
            """.trimIndent()
        }
        else -> {
            financialAdvice = """
                Tus ingresos y gastos están equilibrados, pero puedes mejorar tu estabilidad financiera.
                Aquí algunos consejos:
                1. Ahorra al menos el 10% de tus ingresos para emergencias.
                2. Busca formas de diversificar tus fuentes de ingreso.
                3. Controla tus gastos y no gastes más de lo que realmente necesitas.
            """.trimIndent()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.AppTitle), style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        CircularProgressIndicatorWithText(
            ingresos = totalIngresos,
            gastos = totalGastos,
            dineroActual = dineroActual?.total ?: 0.0
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onAddIngresoClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A65D8))
            ) {
                Text(stringResource(id = R.string.AddEntryButton), color = Color.White)
            }
            IconButton(
                onClick = { showDialog = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_advice),
                    contentDescription = "Consejos",
                    tint = Color(0xFFFFA500)
                )
            }
            Button(
                onClick = onAddGastoClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF39C9CD))
            ) {
                Text(stringResource(id = R.string.AddExpenseButton), color = Color.White)
            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.Recents),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.Start)
        )

        val latestIngreso = ingresos.maxByOrNull { it.fecha }
        val latestGasto = gastos.maxByOrNull { it.fecha }

        latestIngreso?.let {
            TransactionItem(
                name = it.nombre,
                amount = "$${it.valor.format(2)}",
                type = "Ingreso",
                date = it.fecha,
                isPositive = true,
                onClick = { onEditIncomeClick(it.id) }
            )
        }

        latestGasto?.let {
            TransactionItem(
                name = it.nombre,
                amount = "$${it.valor.format(2)}",
                type = "Gasto",
                date = it.fecha,
                isPositive = false,
                onClick = { onEditGastoClick(it.id) }
            )
        }
    }

    // Popup (AlertDialog) mostrando los consejos basados en la situación financiera
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Consejos financieros")
            },
            text = {
                Text(financialAdvice)
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A65D8))
                ) {
                    Text("Cerrar", color = Color.White)
                }
            }
        )
    }
}




@Composable
fun CircularProgressIndicatorWithText(
    ingresos: Double,
    gastos: Double,
    dineroActual: Double
) {
    val total = ingresos + gastos
    val progressIngresos = if (total > 0) ingresos / total else 0.5
    val progressGastos = if (total > 0) gastos / total else 0.5

    Box(contentAlignment = Alignment.Center) {


        Canvas(modifier = Modifier.size(200.dp)) {
            val strokeWidth = 16.dp.toPx()

            drawArc(
                color = Color.LightGray,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = Color(0xFF2A65D8),
                startAngle = 270f,
                sweepAngle = (progressIngresos * 360).toFloat(),
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = Color(0xFF39C9CD),
                startAngle = 270f + (progressIngresos * 360).toFloat(),
                sweepAngle = (progressGastos * 360).toFloat(),
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$${dineroActual.format(2)}", style = MaterialTheme.typography.headlineMedium)
            Text(text = stringResource(id = R.string.CurrentMoney), style = MaterialTheme.typography.bodyMedium)
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .padding(end = 4.dp)
                    .background(Color(0xFF2A65D8))
            )
            Text(text = stringResource(id = R.string.Entrys), style = MaterialTheme.typography.bodyMedium)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .padding(end = 4.dp)
                    .background(Color(0xFF39C9CD))
            )
            Text(text = stringResource(id = R.string.Expenses), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)
