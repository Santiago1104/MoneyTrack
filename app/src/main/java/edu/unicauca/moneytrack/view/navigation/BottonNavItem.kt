package edu.unicauca.moneytrack.view.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import edu.unicauca.moneytrack.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val label: Int,
    @DrawableRes val icon: Int
) {
    object Home : BottomNavItem("home", R.string.home, R.drawable.ic_home)
    object History : BottomNavItem("history", R.string.history, R.drawable.ic_history)
    object Profile : BottomNavItem("profile", R.string.profile, R.drawable.ic_profile)
    // Nuevos elementos de navegación
    //object ManageExpenses : BottomNavItem("manage_expenses", R.string.manage_expenses, R.drawable.ic_manage_expenses) // Falta añadir los iconos
    //object AddExpense : BottomNavItem("add_expense", R.string.add_expense, R.drawable.ic_add_expense)

}