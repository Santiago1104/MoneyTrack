package edu.unicauca.moneytrack.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import edu.unicauca.moneytrack.view.navigation.BottomNavItem
import edu.unicauca.moneytrack.view.screens.HomeScreen
import edu.unicauca.moneytrack.view.screens.TransactionHistoryScreen // Importa la pantalla de historial
import edu.unicauca.moneytrack.view.screens.TestScreen
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel

class MainActivity : ComponentActivity() {
    private val moneyViewModel: MoneyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp(moneyViewModel)
        }
    }
}

@Composable
fun MyApp(moneyViewModel: MoneyViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Aquí añades las screens con su respectiva ruta
            composable("home") { HomeScreen(navController) }
            composable("test") { TestScreen(moneyViewModel = moneyViewModel) }
            composable("history") {
                TransactionHistoryScreen(navController = navController, moneyViewModel = moneyViewModel)
            }
            composable("profile") { /* Pantalla del perfil o configuraciones */ }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.History,
        BottomNavItem.Profile
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                label = { Text(stringResource(id = item.label)) },
                icon = {
                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.label)
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
