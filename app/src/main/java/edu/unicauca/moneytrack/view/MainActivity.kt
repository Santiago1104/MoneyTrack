package edu.unicauca.moneytrack.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import edu.unicauca.moneytrack.model.clsExpense
import edu.unicauca.moneytrack.view.navigation.BottomNavItem
import edu.unicauca.moneytrack.view.screens.*
import edu.unicauca.moneytrack.viewmodel.MoneyViewModel
import com.google.firebase.FirebaseApp



class MainActivity : ComponentActivity() {
    private val moneyViewModel: MoneyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
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
            composable("home") {
                HomeScreen(
                    viewModel = moneyViewModel,
                    onAddGastoClick = { navController.navigate("addExpenses") },
                    onAddIngresoClick = { navController.navigate("addIngreso") },
                )
            }
            composable("adminGastos") { ManageExpensesScreen(navController) } // administrar gastos
            composable("addExpenses") { AddExpensesScreen(navController) } // añadir gasto
            composable("editGasto/{expenseId}") { backStackEntry ->
                val expenseId = backStackEntry.arguments?.getString("expenseId")
                EditExpensesScreen(navController, clsExpense(), expenseId) // Pasar el ID del gasto y el ViewModel
            }
            composable("addIngreso") { NuevoIngresoScreen() }
            composable("editIngreso") { EditarIngresoScreen() }
            composable("history") {
                TransactionHistoryScreen(
                    navController = navController,
                    moneyViewModel = moneyViewModel
                )
            }
            composable("profile") { TestScreen(moneyViewModel = moneyViewModel) }
            composable("authors") { AuthorsScreen(navController) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.History,
        BottomNavItem.Profile
    )
    NavigationBar(
        modifier = Modifier.height(58.dp) // Ajustar la altura de la barra de navegación
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
@Composable
fun ManageExpensesScreen(navController: NavController, expenses: List<clsExpense>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Administrar Gastos",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = { navController.navigate("addExpenses") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir Gasto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar una lista de gastos (puedes usar LazyColumn para una mejor presentación)
        for (expense in expenses) {
            Button(
                onClick = {
                    navController.navigate("editGasto/${expense.id}") // Usar el ID del gasto
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar Gasto: ${expense.nombre}") // Muestra el nombre del gasto
            }
            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre botones
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("history") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver Historial de Gastos")
        }
    }
}

