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
}