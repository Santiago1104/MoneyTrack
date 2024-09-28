package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel


@Composable
fun HomeScreen(
    viewModel: MoneyViewModel,
    onAddGastoClick: () -> Unit,
    onAddIngresoClick: () -> Unit
) {
    val dineroActual by viewModel.dinero.observeAsState(initial = null)
    val ingresos by viewModel.listaIngresos.observeAsState(emptyList())
    val gastos by viewModel.listaGastos.observeAsState(emptyList())

    // Calculamos el total de ingresos y gastos
    val totalIngresos = ingresos.sumOf { it.valor }
    val totalGastos = gastos.sumOf { it.valor }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Money Track", style = MaterialTheme.typography.headlineMedium )

        Spacer(modifier = Modifier.height(16.dp))

        // Componente de la barra circular
        CircularProgressIndicatorWithText(
            ingresos = totalIngresos,
            gastos = totalGastos,
            dineroActual = dineroActual?.total ?: 0.0
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de Nuevo Gasto e Ingreso
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = onAddGastoClick) {
                Text("Nuevo gasto")
            }
            Button(onClick = onAddIngresoClick) {
                Text("Nuevo ingreso")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Recientes", style = MaterialTheme.typography.headlineMedium )
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

            // Fondo de la barra circular
            drawArc(
                color = Color.LightGray,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )

            // Barra de ingresos
            drawArc(
                color = Color(0xFF03DAC5),
                startAngle = 270f,
                sweepAngle = (progressIngresos * 360).toFloat(),
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )

            // Barra de gastos
            drawArc(
                color = Color(0xFF6200EA),
                startAngle = 270f + (progressIngresos * 360).toFloat(),
                sweepAngle = (progressGastos * 360).toFloat(),
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
        }

        // Texto del dinero actual
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$${dineroActual.format(2)}", style = MaterialTheme.typography.headlineMedium )
            Text(text = "Dinero actual", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)
