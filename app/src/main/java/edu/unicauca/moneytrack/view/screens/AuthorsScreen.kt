package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AuthorsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Autores", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        Text(text = "Creador 1: Daniel Santiago Muñoz Rodriguez", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = "Creador 2: Andrés Felipe Herrera Artunduaga", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = "Creador 3: Andrea Cuantindioy Ortiz", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = "Creador 3: Elizabeth Quiñones", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = "Github de los Creadores: https://github.com/Santiago1104/MoneyTrack", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
    }
}
