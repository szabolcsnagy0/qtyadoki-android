package hu.android.qtyadoki.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.android.qtyadoki.api.TokenManager
import hu.android.qtyadoki.ui.screens.drawerscreens.appointmentsscreen.AppointmentDetails
import hu.android.qtyadoki.ui.screens.drawerscreens.drawermenu.DrawerView
import hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen.PetEdit
import hu.android.qtyadoki.ui.screens.drawerscreens.petsscreen.petdetails.PetDetails
import hu.android.qtyadoki.ui.screens.drawerscreens.profilescreen.ProfileEdit
import hu.android.qtyadoki.ui.screens.welcomescreen.LoginScreen
import hu.android.qtyadoki.ui.screens.welcomescreen.RegistrationScreen
import hu.android.qtyadoki.ui.viewmodels.AuthenticationViewModel
import hu.android.qtyadoki.ui.viewmodels.DrawerViewModel
import hu.android.qtyadoki.ui.viewmodels.PetDetailsViewModel

/**
 * Navigation between screens.
 * @param tokenManager: TokenManager
 */
@Composable
fun Navigation(tokenManager: TokenManager) {

    val navController = rememberNavController()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)

    val authenticationViewModel = remember { AuthenticationViewModel(tokenManager) }
    val drawerViewModel = remember { DrawerViewModel() }

    val startScreenRoute = remember {
        mutableStateOf(ScreenRoutes.ProfileScreen.route)
    }

    val petDetailsViewModel = remember { PetDetailsViewModel() }

    authenticationViewModel.testToken()

    NavHost(navController = navController, startDestination = ScreenRoutes.Authentication.route) {
        navigation(
            startDestination = ScreenRoutes.LoginScreen.route,
            route = ScreenRoutes.Authentication.route
        ) {
            composable(route = ScreenRoutes.LoginScreen.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                }) {
                LoginScreen(
                    navController = navController,
                    viewModel = authenticationViewModel
                )
            }
            composable(route = ScreenRoutes.RegistrationScreen.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                }) {
                RegistrationScreen(navController = navController)
            }
        }

        navigation(
            startDestination = ScreenRoutes.DrawerView.route,
            route = ScreenRoutes.Menu.route
        ) {
            composable(
                route = ScreenRoutes.DrawerView.route,
                enterTransition = {
                    if (this.initialState.destination.route == ScreenRoutes.LoginScreen.route) {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                            animationSpec = tween(700)
                        )
                    } else {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                            animationSpec = tween(700)
                        )
                    }
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                }) {
                DrawerView(
                    mainNavController = navController,
                    viewModel = drawerViewModel,
                    drawerState = drawerState,
                    startScreen = startScreenRoute,
                    logoutRequested = {
                        authenticationViewModel.logOut()
                    }
                )
            }

            composable(route = ScreenRoutes.PetsDetailScreen.route, arguments = listOf(
                navArgument("pet_id") {
                    type = NavType.IntType
                }
            ),
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    if (this.targetState.destination.route == ScreenRoutes.PetsEditScreen.route) {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                            animationSpec = tween(700)
                        )
                    } else slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                }) { entry ->
                val id = entry.arguments?.getInt("pet_id") ?: 0
                petDetailsViewModel.selectPet(id)
                PetDetails(navController = navController, viewModel = petDetailsViewModel)
            }

            composable(route = ScreenRoutes.PetsEditScreen.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                }) {
                PetEdit(navController = navController, viewModel = petDetailsViewModel)
            }

            composable(route = ScreenRoutes.ProfileEditScreen.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                }) {
                ProfileEdit(navController = navController)
            }

            composable(route = ScreenRoutes.AppointmentsDetails.route, arguments = listOf(
                navArgument("appointment_id") {
                    type = NavType.IntType
                }
            ),
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                }) { entry ->
                val id = entry.arguments?.getInt("appointment_id") ?: 0
                AppointmentDetails(
                    navController = navController,
                    appId = id
                )
            }
        }
    }
}
