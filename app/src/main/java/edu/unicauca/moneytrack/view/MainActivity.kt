package edu.unicauca.moneytrack.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import edu.unicauca.moneytrack.view.navigation.BottomNavItem
import edu.unicauca.moneytrack.view.screens.AuthorsScreen
import edu.unicauca.moneytrack.view.screens.HomeScreen
import edu.unicauca.moneytrack.view.screens.TestScreen
import edu.unicauca.moneytrack.view.screens.TransactionHistoryScreen
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
        ){
            composable("home") {
                HomeScreen(
                    viewModel = moneyViewModel,
                    onAddGastoClick = { navController.navigate("addGasto") },
                    onAddIngresoClick = { navController.navigate("addIngreso") }
                )
            }
            composable("addGasto") { /* Pantalla de agregar gasto */ }
            composable("addIngreso") { /* Pantalla de agregar ingreso */ }
            composable("editGasto") { /* Pantalla de eilimar crear gasto */ }
            composable("editIngreso") { /* Pantalla de eliminar crear ingreso */ }
            composable("history") { TransactionHistoryScreen(navController = navController, moneyViewModel = moneyViewModel) }
            composable("profile") { TestScreen(moneyViewModel = moneyViewModel)}
            composable("authors") { AuthorsScreen(navController) }
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
    NavigationBar (
        modifier = Modifier.height(58.dp) // Ajusta la altura de la barra de navegación
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                label = { Text(stringResource(id = item.label)) },
                icon = {
                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.label),
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