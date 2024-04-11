package hu.android.qtyadoki.ui.screens.drawerscreens.drawermenu

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import hu.android.qtyadoki.ui.ScreenRoutes
import hu.android.qtyadoki.ui.screens.drawerscreens.appointmentsscreen.AppointmentsScreen
import hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen.PetsScreen
import hu.android.qtyadoki.ui.screens.drawerscreens.profilescreen.ProfileDetails
import hu.android.qtyadoki.ui.viewmodels.AppointmentsViewModel
import hu.android.qtyadoki.ui.viewmodels.DrawerViewModel
import hu.android.qtyadoki.ui.viewmodels.PetsViewModel

fun NavGraphBuilder.mainGraph(
    mainNavController: NavHostController,
    drawerViewModel: DrawerViewModel,
    petsViewModel: PetsViewModel,
    appointmentsViewModel: AppointmentsViewModel
) {
    composable(route = ScreenRoutes.PetsScreen.route) {
        PetsScreen(viewModel = petsViewModel, onItemClicked = {
            mainNavController.navigate(
                ScreenRoutes.PetsDetailScreen.route.replace(
                    oldValue = "{pet_id}", newValue = it.toString()
                )
            ) {
                popUpTo(ScreenRoutes.PetsDetailScreen.route) {
                    inclusive = true
                }
            }
        })
    }
    composable(ScreenRoutes.AppointmentsScreen.route) {
        AppointmentsScreen(viewModel = appointmentsViewModel, onItemClicked = {
            mainNavController.navigate(
                ScreenRoutes.AppointmentsDetails.route.replace(
                    oldValue = "{appointment_id}", newValue = it.toString()
                )
            )
        })
    }
    composable(ScreenRoutes.ProfileScreen.route) {
        drawerViewModel.refreshUserData()
        ProfileDetails(viewModel = drawerViewModel, onEditClicked = {
            mainNavController.navigate(ScreenRoutes.ProfileEditScreen.route) {
                popUpTo(ScreenRoutes.ProfileEditScreen.route) {
                    inclusive = true
                }
            }
        })
    }
    composable(ScreenRoutes.Logout.route) {
        mainNavController.navigate(ScreenRoutes.Authentication.route)
    }
}