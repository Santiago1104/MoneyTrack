package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel


@Composable
fun EditarIngresoScreen(ingresoId: String?, viewModel: MoneyViewModel) {
    // Obtener el ingreso usando el ingresoId
    val ingreso = viewModel.listaIngresos.observeAsState().value?.find { it.id == ingresoId }

    // Si el ingreso es nulo, no mostrar la pantalla
    if (ingreso == null) {
        Text("Cargando ingreso...")
        return
    }

    // Prellenar los campos con los datos actuales del ingreso
    var referencia by remember { mutableStateOf(TextFieldValue(ingreso.nombre)) }
    var valor by remember { mutableStateOf(TextFieldValue(ingreso.valor.toString())) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Editar Ingreso",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
        )

        Text(
            text = "A continuación, ingresa los datos asociados al nuevo ingreso.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
        )

        // Campo de texto para Referencia
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextField(
                    value = referencia,
                    onValueChange = { referencia = it },
                    label = { Text("Referencia") },
                    placeholder = { Text("Ej: Bancolombia") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Campo de texto para Valor
                TextField(
                    value = valor,
                    onValueChange = { valor = it },
                    label = { Text("Valor") },
                    placeholder = { Text("$ 0.00") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Fila de botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.borrarIngreso(ingreso.id) },
                modifier = Modifier
                    .width(100.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A65D8))
            ) {
                Text("Eliminar")
            }

            Button(
                onClick = {
                    val updatedIngreso = ingreso.copy(
                        nombre = referencia.text,
                        valor = valor.text.toDoubleOrNull() ?: 0.0
                    )
                    viewModel.actualizarIngreso(updatedIngreso)
                },
                modifier = Modifier
                    .width(100.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A65D8))
            ) {
                Text("Guardar")
            }
        }
    }

    Spacer(modifier = Modifier.height(32.dp))
}