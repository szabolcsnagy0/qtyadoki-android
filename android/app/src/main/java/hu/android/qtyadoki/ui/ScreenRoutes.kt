package hu.android.qtyadoki.ui

/**
 * Sealed class for the screen routes.
 * @param route The route of the screen.
 */
sealed class ScreenRoutes(val route: String) {
    object RegistrationScreen : ScreenRoutes("registration_screen")
    object LoginScreen : ScreenRoutes("login_screen")
    object Authentication: ScreenRoutes("auth")
    object Menu: ScreenRoutes("menu_view")
    object PetsScreen: ScreenRoutes("pets_screen")
    object PetsDetailScreen : ScreenRoutes("pets_detail/{pet_id}")
    object PetsEditScreen : ScreenRoutes("pets_edit")
    object ProfileScreen : ScreenRoutes("profile_screen")
    object ProfileEditScreen : ScreenRoutes("profile_edit")
    object AppointmentsScreen : ScreenRoutes("appointments_screen")
    object AppointmentsDetails : ScreenRoutes("appointments_details/{appointment_id}")
    object DrawerView: ScreenRoutes("drawer_view")
    object Logout: ScreenRoutes("logout")
}
