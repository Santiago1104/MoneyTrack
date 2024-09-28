package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.background
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

@Composable
fun AddIngresoScreen() {
    var referencia by remember { mutableStateOf(TextFieldValue("")) }
    var valor by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Editar Ingreso",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Ingresa los datos asociados al nuevo ingreso.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Caja de texto para Referencia
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column {
                TextField(
                    value = referencia,
                    onValueChange = { referencia = it },
                    label = { Text("Referencia") },
                    placeholder = { Text("Ej: Bancolombia") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Caja de texto para Valor
                TextField(
                    value = valor,
                    onValueChange = { valor = it },
                    label = { Text("Valor") },
                    placeholder = { Text("$ 0.00") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botones Eliminar y Guardar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* Acción para eliminar */ },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFdd5035))
            ) {
                Text("Eliminar")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { /* Acción para guardar */ },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A65D8))


            ) {
                Text("Guardar")
            }
        }
    }

    Spacer(modifier = Modifier.height(32.dp))

    // Barra de navegación inferior
    BottomNavigationBar()
}

@Composable
fun BottomNavigationBar() {
}

@Preview(showBackground = true)
@Composable
fun PreviewEditarIngresoScreen() {
    AddIngresoScreen()
}
