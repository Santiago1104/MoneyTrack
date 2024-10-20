package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import edu.unicauca.moneytrack.model.clsEntry
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun NuevoIngresoScreen(
    viewModel: MoneyViewModel,
    onIngresoGuardado: () -> Unit // Callback para navegar después de guardar
) {
    var referencia by remember { mutableStateOf(TextFieldValue("")) }
    var valor by remember { mutableStateOf(TextFieldValue("")) }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Nuevo ingreso",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
        )

        Text(
            text = "Ingresa los datos asociados al nuevo ingreso.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
        )

        // Texto para Referencia
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

        // Botón Guardar
        Button(
            onClick = {
                val valorIngreso = valor.text.toDoubleOrNull()
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
                    // Obtener la fecha actual en formato personalizado
                    val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
                    val fechaActual = formatoFecha.format(Date())

                    // Guardar el ingreso si el valor es válido
                    viewModel.agregarIngreso(
                        clsEntry(
                            id = "",
                            nombre = referencia.text,
                            valor = valorIngreso.toDouble(),
                            fecha = fechaActual
                        )
                    )
                    onIngresoGuardado() // Navegar después de guardar
                    errorMensaje = null // Limpiar error después de guardado
                }
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

    Spacer(modifier = Modifier.height(32.dp))
}
