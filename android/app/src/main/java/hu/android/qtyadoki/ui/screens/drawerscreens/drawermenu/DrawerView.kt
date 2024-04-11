package hu.android.qtyadoki.ui.screens.drawerscreens.drawermenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.signature.MediaStoreSignature
import hu.android.qtyadoki.R
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.data.OwnerModel
import hu.android.qtyadoki.ui.ScreenRoutes
import hu.android.qtyadoki.ui.components.CustomAlertDialog
import hu.android.qtyadoki.ui.theme.LightBlue
import hu.android.qtyadoki.ui.theme.LightBlueAlpha
import hu.android.qtyadoki.ui.theme.LightGrey
import hu.android.qtyadoki.ui.theme.quickSandFamily
import hu.android.qtyadoki.ui.viewmodels.AppointmentsViewModel
import hu.android.qtyadoki.ui.viewmodels.DrawerViewModel
import hu.android.qtyadoki.ui.viewmodels.PetsViewModel
import kotlinx.coroutines.launch


/**
 * Composable function that displays the drawer.
 * @param drawerState: The state of the drawer, whether it is open or closed.
 * @param mainNavController: The navController of the main graph.
 * @param navController: The navController of the drawer graph.
 * @param viewModel: The viewModel of the drawer.
 * @param startScreen: The start screen of the drawer.
 * @param logoutRequested: The function that is called when the user wants to logout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerView(
    drawerState: DrawerState,
    mainNavController: NavHostController,
    navController: NavHostController = rememberNavController(),
    viewModel: DrawerViewModel,
    startScreen: MutableState<String>,
    logoutRequested: () -> Unit,
) {
    val user by viewModel.ownerData

    val coroutineScope = rememberCoroutineScope()
    val logoutRequest = remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onItemSelected = { route ->
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navController.navigate(route) {
                        popUpTo(ScreenRoutes.Menu.route) {
                            inclusive = true
                        }
                    }
                    startScreen.value = route
                },
                user = user,
                prevSelectedRoute = startScreen.value,
                logoutRequest = logoutRequest,
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                viewModel.refreshUserData()
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                Icons.Rounded.Menu,
                                contentDescription = stringResource(R.string.menubutton),
                                tint = LightBlue,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            val petsViewModel = remember { PetsViewModel() }
            val appointmentsViewModel = remember { AppointmentsViewModel() }
            NavHost(
                navController = navController,
                startDestination = startScreen.value,
                modifier = Modifier.padding(paddingValues)
            ) {
                mainGraph(mainNavController, viewModel, petsViewModel, appointmentsViewModel)
            }
        }
    }

    if (logoutRequest.value) {
        CustomAlertDialog(
            dialogText = stringResource(R.string.logout_confirmation),
            confirmText = stringResource(R.string.yes),
            dismissText = stringResource(
                id = R.string.cancel
            ),
            onConfirmation = {
                logoutRequested()
                navController.navigate(ScreenRoutes.Logout.route) {
                    popUpTo(ScreenRoutes.Menu.route) {
                        inclusive = true
                    }
                }
                startScreen.value = ScreenRoutes.ProfileScreen.route
            },
            onDismissRequest = {
                logoutRequest.value = false
            }
        )
    }

}

/**
 * Composable function that displays the drawer content.
 * @param modifier: The modifier of the drawer content.
 * @param items: The list of the drawer items.
 * @param user: The user data.
 * @param onItemSelected: The function that is called when an item is selected.
 * @param logoutRequest: The state of the logout request.
 * @param prevSelectedRoute: The previously selected route.
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    items: List<NavMenuItem> = listOf(
        NavMenuItem.Profile,
        NavMenuItem.Pets,
        NavMenuItem.Appointments
    ),
    user: OwnerModel,
    onItemSelected: (String) -> Unit = {},
    logoutRequest: MutableState<Boolean> = mutableStateOf(false),
    prevSelectedRoute: String,
) {
    ModalDrawerSheet {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.padding(vertical = 20.dp))
                GlideImage(
                    model = ApiService.getGlideURLProfile(),
                    loading = placeholder(R.drawable.dog_image_placeholder),
                    contentDescription = stringResource(id = R.string.profile_picture),
                    modifier = Modifier
                        .size(80.dp)
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Crop,
                ) {
                    it.signature(MediaStoreSignature("image/jpeg", System.currentTimeMillis(), 0))
                }
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Text(
                    text = user.name,
                    fontFamily = quickSandFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = LightGrey
                )
                Text(
                    text = user.email,
                    fontFamily = quickSandFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    color = LightGrey
                )
            }
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.weight(2f)
            ) {
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                for (item in items) {
                    val arrowIcon = if (item.route == prevSelectedRoute) {
                        Icons.AutoMirrored.Filled.ArrowBackIos
                    } else {
                        Icons.AutoMirrored.Filled.ArrowForwardIos
                    }
                    DrawerItem(item = item,
                        isSelected = item.route == prevSelectedRoute,
                        arrowIcon = arrowIcon,
                        modifier = Modifier
                            .width(250.dp),
                        onClick = {
                            if (prevSelectedRoute != it.route) {
                                onItemSelected(it.route)
                            }
                        })

                }
                Spacer(modifier = Modifier.padding(vertical = 30.dp))
            }
            Column(verticalArrangement = Arrangement.Bottom) {
                DrawerItem(
                    item = NavMenuItem.Logout,
                    arrowIcon = null,
                    modifier = Modifier.width(250.dp),
                    onClick = {
                        logoutRequest.value = true
                    })
            }
        }
    }
}

/**
 * Composable function that displays a drawer item.
 * @param item: The drawer item.
 * @param modifier: The modifier of the drawer item.
 * @param isSelected: The state of the drawer item.
 * @param arrowIcon: The arrow icon of the drawer item.
 * @param onClick: The function that is called when the drawer item is clicked.
 */
@Composable
fun DrawerItem(
    item: NavMenuItem,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    arrowIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowForwardIos,
    onClick: (NavMenuItem) -> Unit = {}
) {
    val customModifier = if (isSelected) {
        modifier.background(color = LightBlueAlpha)
    } else modifier

    val iconColor = if (isSelected) {
        LightBlue
    } else LightGrey
    Row(verticalAlignment = Alignment.CenterVertically, modifier = customModifier
        .fillMaxWidth()
        .height(80.dp)
        .clickable { onClick(item) }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .weight(3f)
                .padding(end = 0.dp, start = 30.dp)
        ) {
            Icon(imageVector = item.icon, contentDescription = null, tint = iconColor)
            Spacer(modifier = Modifier.padding(horizontal = 7.dp))
            Text(
                text = item.title,
                color = LightGrey,
                fontFamily = quickSandFamily,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            if (arrowIcon != null)
                Icon(
                    imageVector = arrowIcon, contentDescription = null, tint = iconColor, modifier =
                    Modifier.size(15.dp)
                )
        }
    }
}