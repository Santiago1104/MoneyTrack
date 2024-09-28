package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.unicauca.moneytrack.R

@Composable
fun AuthorsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.autores), fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        Text(text = stringResource(R.string.creador_1_daniel_santiago_mu_oz_rodriguez), fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = stringResource(R.string.creador_2_andr_s_felipe_herrera_artunduaga), fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = stringResource(R.string.creador_3_andrea_cuantindioy_ortiz), fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = stringResource(R.string.creador_3_elizabeth_qui_ones), fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = stringResource(R.string.github_de_los_creadores_https_github_com_santiago1104_moneytrack), fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
    }
}
