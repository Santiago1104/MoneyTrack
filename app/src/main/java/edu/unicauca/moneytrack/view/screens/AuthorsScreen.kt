package edu.unicauca.moneytrack.view.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.unicauca.moneytrack.R

@Composable
fun AuthorsScreen(navController: NavHostController) {
    val context = LocalContext.current // Obtener el contexto actual para abrir el enlace

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
        // ClickableText para abrir el enlace del repositorio
        Text(
            text = AnnotatedString(
                text = "Haz clic ",
                spanStyle = SpanStyle(fontSize = 16.sp)
            ).plus(
                AnnotatedString(
                    text = "aquí",
                    spanStyle = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                )
            ).plus(
                AnnotatedString(
                    text = " para ver el repositorio en GitHub.",
                    spanStyle = SpanStyle(fontSize = 16.sp)
                )
            ),
            modifier = Modifier.clickable {
                // Acción para abrir el enlace en el navegador
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Santiago1104/MoneyTrack"))
                context.startActivity(intent)
            }
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
