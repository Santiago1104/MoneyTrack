package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
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
fun EditarIngresoScreen(
    ingresoId: String?,
    viewModel: MoneyViewModel,
    onIngresoEditado: () -> Unit // Callback para redirigir después de la acción
) {
    // Obtener el ingreso usando el ingresoId
    val ingreso = viewModel.listaIngresos.observeAsState().value?.find { it.id == ingresoId }

    // Si el ingreso es nulo, no mostrar la pantalla
    if (ingreso == null) {
        Text("Cargando ingreso...")
        return
    }

    // Prellenar los campos con los datos actuales del ingreso
    var referencia by rememberSaveable  { mutableStateOf(TextFieldValue(ingreso.nombre)) }
    var valor by rememberSaveable  { mutableStateOf(TextFieldValue(ingreso.valor.toString())) }
    var errorMensaje by rememberSaveable  { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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

                // Caja de texto para Valor
                TextField(
                    value = valor,
                    onValueChange = { valor = it },
                    label = { Text("Valor") },
                    placeholder = { Text("$ 0.00") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMensaje != null
                )

                // Mensaje de error
                errorMensaje?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Fila de botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    viewModel.borrarIngreso(ingreso.id)
                    onIngresoEditado() // Navegar de vuelta después de eliminar
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFec5353))
            ) {
                Text("Eliminar")
            }

            Button(
                onClick = {
                    val valorIngreso = valor.text.toDouble()

                    // Validaciones
                    if (valorIngreso == null) {
                        errorMensaje = "Por favor, ingrese un valor válido"
                    } else if (valorIngreso < 0) {
                        errorMensaje = "No se permiten valores negativos"
                    } else if (valor.text.length > 7) {
                        errorMensaje = "El número no puede tener más de 7 dígitos"
                    } else if (!valor.text.matches(Regex("^[0-9]+\$"))) {
                        errorMensaje = "Solo se permiten números enteros"
                    } else {
                        // Actualizar el ingreso si el valor es válido
                        val updatedIngreso = ingreso.copy(
                            nombre = referencia.text,
                            valor = valorIngreso.toDouble()
                        )
                        viewModel.actualizarIngreso(updatedIngreso)
                        onIngresoEditado() // Navegar de vuelta después de guardar
                        errorMensaje = null // Limpiar error después de guardar
                    }
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A65D8))
            ) {
                Text("Guardar")
            }
        }
    }

    Spacer(modifier = Modifier.height(32.dp))
}