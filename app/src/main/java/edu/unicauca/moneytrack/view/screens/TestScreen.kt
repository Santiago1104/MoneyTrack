package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.unicauca.moneytrack.model.clsEntry
import edu.unicauca.moneytrack.model.clsExpense
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel
import java.util.UUID

@Composable
fun TestScreen(moneyViewModel: MoneyViewModel, modifier: Modifier = Modifier) {
    // Observar datos del ViewModel
    val dineroTotal by moneyViewModel.dinero.observeAsState()
    val listaGastos by moneyViewModel.listaGastos.observeAsState()
    val listaIngresos by moneyViewModel.listaIngresos.observeAsState()

    // Variable de estado para la referencia seleccionada
    var selectedReference by remember { mutableStateOf("") }

    // Lista de referencias (en este caso, los ingresos)
    val referencias = listaIngresos?.map { it.nombre } ?: emptyList()

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

        // Dropdown para seleccionar una referencia
        var expanded by remember { mutableStateOf(false) }
        Box {
            TextField(
                value = selectedReference,
                onValueChange = { /* El valor se selecciona en el menú desplegable */ },
                label = { Text("Seleccionar referencia") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar referencia")
                    }
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                referencias.forEach { referencia ->
                    DropdownMenuItem(
                        text = { Text(referencia) },
                        onClick = {
                            selectedReference = referencia
                            expanded = false
                        }
                    )
                }
            }
        }

        // Botón para agregar gasto con datos de prueba
        Button(onClick = {
            val nuevoGasto = clsExpense(
                id = UUID.randomUUID().toString(), // Generar ID único
                nombre = "Transporte",
                valor = 10000.0,
                categoria = "Movilidad",
                referencia = selectedReference // Usar la referencia seleccionada
            )
            moneyViewModel.agregarGasto(nuevoGasto)
            moneyViewModel.actualizarDineroTotal(-nuevoGasto.valor)
            moneyViewModel.obtenerGastos()
        }) {
            Text("Añadir Gasto de Prueba")
        }

        // Botón para agregar ingreso con datos de prueba
        Button(onClick = {
            val nuevoIngreso = clsEntry(
                id = UUID.randomUUID().toString(), // Generar ID único
                nombre = "Venta Freelance",
                valor = 50000.0
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
