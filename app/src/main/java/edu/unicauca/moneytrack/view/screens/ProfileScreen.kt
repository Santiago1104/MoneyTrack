package edu.unicauca.moneytrack.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.unicauca.moneytrack.R

data class SettingOption(val title: String, val description: String, val icon: Int)

@Composable
fun ProfileScreen(navController: NavHostController) {
    val settingsOptions = listOf(
        SettingOption("Ajustes de fecha y hora", "Configura la fecha y hora del dispositivo", R.drawable.ic_edit_calendar),
        SettingOption("Ajustes de pantalla", "Modifica el brillo y tamaño de pantalla", R.drawable.ic_screen),
        SettingOption("Políticas de privacidad", "Consulta nuestras políticas de privacidad", R.drawable.ic_privacy_tip),
        SettingOption("Acerca de", "Información sobre la aplicación", R.drawable.ic_info),
        SettingOption("Autores", "Conoce a los autores de la aplicación", R.drawable.ic_authors)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(settingsOptions) { setting ->
            SettingItem(setting)
        }
    }
}

@Composable
fun SettingItem(setting: SettingOption) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = setting.icon),
                contentDescription = setting.title,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = setting.title, fontSize = 18.sp)
                Text(text = setting.description, fontSize = 14.sp)
            }
        }
    }
}
