package edu.unicauca.moneytrack.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf("Semana") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Menu action */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color.Gray
                )
            }

            Text(text = "Inicio", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            IconButton(onClick = { /* Profile action */ }) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    // Placeholder for profile image
                    Image(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Week/Month Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFEFEF), shape = CircleShape)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Semana",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (selectedTab == "Semana") Color.White else Color(0xFFEFEFEF))
                    .clickable { selectedTab = "Semana" }
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                fontSize = 14.sp,
                color = if (selectedTab == "Semana") Color.Black else Color.Gray,
                fontWeight = if (selectedTab == "Semana") FontWeight.Bold else FontWeight.Normal
            )

            Text(
                text = "Mes",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (selectedTab == "Mes") Color.White else Color(0xFFEFEFEF))
                    .clickable { selectedTab = "Mes" }
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                fontSize = 14.sp,
                color = if (selectedTab == "Mes") Color.Black else Color.Gray,
                fontWeight = if (selectedTab == "Mes") FontWeight.Bold else FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Spending Card with Chart
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "$950.00", fontSize = 28.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "Cantidad gastada esta semana", fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                // Placeholder for a chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color(0xFFE0E0E0)) // Just a placeholder background
                ) {
                    // Aquí iría la implementación del gráfico real.
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Items
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { /* Navigate to Presupuesto */ }
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Presupuesto",
                    modifier = Modifier.size(48.dp)
                )
                Text(text = "Presupuesto", fontSize = 12.sp)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { /* Navigate to Seguimiento de gastos */ }
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Seguimiento de gastos",
                    modifier = Modifier.size(48.dp)
                )
                Text(text = "Seguimiento de gastos", fontSize = 12.sp)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { /* Navigate to Metas de ahorro */ }
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Metas de ahorro",
                    modifier = Modifier.size(48.dp)
                )
                Text(text = "Metas de ahorro", fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}
