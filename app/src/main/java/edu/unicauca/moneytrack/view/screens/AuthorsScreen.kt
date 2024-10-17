package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.unicauca.moneytrack.R

@Composable
fun AuthorsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0)) // Fondo gris claro
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Título
        Text(
            text = stringResource(R.string.autores),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Tarjetas para cada autor
        AuthorCard(name = stringResource(R.string.creador_1_daniel_santiago_mu_oz_rodriguez))
        AuthorCard(name = stringResource(R.string.creador_2_andr_s_felipe_herrera_artunduaga))
        AuthorCard(name = stringResource(R.string.creador_3_andrea_cuantindioy_ortiz))
        AuthorCard(name = stringResource(R.string.creador_3_elizabeth_qui_ones))

        Spacer(modifier = Modifier.height(24.dp))

        // Información del repositorio de GitHub
        Text(
            text = stringResource(R.string.github_de_los_creadores_https_github_com_santiago1104_moneytrack),
            fontSize = 16.sp,
            color = Color.Blue,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun AuthorCard(name: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Nombre del autor
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
