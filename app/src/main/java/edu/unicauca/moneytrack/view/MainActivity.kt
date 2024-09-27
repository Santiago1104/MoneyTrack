package edu.unicauca.moneytrack.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.unicauca.moneytrack.view.screens.HomeScreen
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
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("test") { TestScreen(moneyViewModel = moneyViewModel) }
    }
}
