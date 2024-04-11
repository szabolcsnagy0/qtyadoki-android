package hu.android.qtyadoki.ui.screens.drawerscreens.drawermenu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.ui.graphics.vector.ImageVector
import hu.android.qtyadoki.ui.ScreenRoutes

/**
 * A sealed class that represents a single item in the navigation drawer.
 */
sealed class NavMenuItem(
    var title: String,
    var icon: ImageVector,
    var route: String
) {
    object Profile :
        NavMenuItem(
            "Profil",
            Icons.Filled.Person,
            ScreenRoutes.ProfileScreen.route
        )

    object Pets :
        NavMenuItem(
            "Állataim",
            Icons.Filled.Pets,
            ScreenRoutes.PetsScreen.route
        )

    object Appointments :
        NavMenuItem(
            "Időpontok",
            Icons.Filled.CalendarMonth,
            ScreenRoutes.AppointmentsScreen.route
        )

    object Logout :
        NavMenuItem(
            "Kijelentkezés",
            Icons.AutoMirrored.Filled.Logout,
            ScreenRoutes.Logout.route
        )
}
