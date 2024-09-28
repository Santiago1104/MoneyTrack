package edu.unicauca.moneytrack.view.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.unicauca.moneytrack.model.clsEntry
import edu.unicauca.moneytrack.model.clsExpense
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel


@Composable
fun TestScreen(moneyViewModel: MoneyViewModel, modifier: Modifier = Modifier) {
    // Observar datos del ViewModel
    val dineroTotal by moneyViewModel.dinero.observeAsState()
    val listaGastos by moneyViewModel.listaGastos.observeAsState()
    val listaIngresos by moneyViewModel.listaIngresos.observeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mostrar el Dinero Total
        Text(
            text = "Dinero Total: ${dineroTotal?.total ?: 0.0}",
            style = MaterialTheme.typography.headlineMedium
        )

        // Mostrar la lista de Gastos
        Text(text = "Gastos:", style = MaterialTheme.typography.headlineSmall)
        listaGastos?.forEach { gasto ->
            Text(text = "${gasto.nombre}: ${gasto.valor} / Categoría: ${gasto.categoria} / ${gasto.fecha}")
        }

        // Mostrar la lista de Ingresos
        Text(text = "Ingresos:", style = MaterialTheme.typography.headlineSmall)
        listaIngresos?.forEach { ingreso ->
            Text(text = "${ingreso.nombre}/ ${ingreso.valor} / ${ingreso.fecha}")
        }

        // Botón para agregar gasto con datos quemados de prueba
        Button(onClick = {
            val nuevoGasto = clsExpense(
                id = "",
                nombre = "Transporte",
                categoria = "Movilidad",
                valor = 10000.0,
            )
            moneyViewModel.agregarGasto(nuevoGasto)
            moneyViewModel.actualizarDineroTotal(-nuevoGasto.valor)
            moneyViewModel.obtenerGastos()
        }) {
            Text("Añadir Gasto de Prueba")
        }

        // Botón para agregar ingreso con datos quemados de prueba
        Button(onClick = {
            val nuevoIngreso = clsEntry(
                id = "",
                nombre = "Venta Freelance",
                valor = 50000.0,
            )
            moneyViewModel.agregarIngreso(nuevoIngreso)
            moneyViewModel.actualizarDineroTotal(nuevoIngreso.valor)
        }) {
            Text("Añadir Ingreso de Prueba")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestScreenPreview() {
    TestScreen(moneyViewModel = MoneyViewModel())
}