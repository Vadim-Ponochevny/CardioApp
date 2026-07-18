package com.vpnch.cardioapp.navigation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vpnch.cardioapp.core.ui.CardioDimens
import com.vpnch.cardioapp.feature.healthrecords.HealthRecordsRoute
import com.vpnch.cardioapp.feature.healthrecords.create.HealthRecordCreateRoute
import com.vpnch.cardioapp.feature.healthrecords.detail.HealthRecordDetailRoute
import com.vpnch.cardioapp.feature.healthrecords.edit.HealthMetricEditRoute
import com.vpnch.cardioapp.feature.help.HelpRoute
import com.vpnch.cardioapp.feature.history.presentation.HistoryRoute
import com.vpnch.cardioapp.feature.profile.ProfileRoute
import com.vpnch.cardioapp.feature.today.TodayRoute
import com.vpnch.cardioapp.feature.vitamins.VitaminsRoute
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.navigation.CardioDestinations.HEALTH_RECORD_DETAIL_ARG

@Composable
fun CardioNavHost(
    modifier: Modifier = Modifier,
    initialRoute: String? = null,
    navController: NavHostController = rememberNavController(),
) {
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* user responded — no action needed, system handles the grant */ }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (initialRoute != null) {
            navController.navigate(initialRoute) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination.showsBottomBar()
    val selectedTopLevel = currentDestination.topLevelDestination()

    Scaffold(
        modifier = modifier,
        containerColor = CardioTheme.colors.background,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = CardioTheme.colors.onPrimary,
                    contentColor = CardioTheme.colors.textSecondary,
                    tonalElevation = 0.dp,
                ) {
                    TopLevelDestination.entries.forEach { destination ->
                        NavigationBarItem(
                            selected = selectedTopLevel == destination,
                            onClick = { navController.navigateTopLevel(destination) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = destination.iconRes),
                                    contentDescription = destination.label,
                                    modifier = Modifier.size(CardioDimens.NavigationBar.navigationBarItemSize),
                                )
                            },
                            label = { Text(destination.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CardioTheme.colors.primary,
                                unselectedIconColor = CardioTheme.colors.textSecondary,
                                selectedTextColor = CardioTheme.colors.primary,
                                unselectedTextColor = CardioTheme.colors.textSecondary,
                                indicatorColor = CardioTheme.colors.navSelectedContainer,
                            )
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CardioDestinations.TODAY_GRAPH,
            modifier = Modifier.padding(innerPadding),
        ) {

            navigation(
                startDestination = CardioDestinations.TODAY_HOME,
                route = CardioDestinations.TODAY_GRAPH,
            ) {
                composable(route = CardioDestinations.TODAY_HOME) {
                    TodayRoute(
                        onOpenLatestRecord = { recordId ->
                            navController.navigate(CardioDestinations.healthRecordDetail(recordId))
                        },
                        onAddHealthRecord = {
                            navController.navigate(CardioDestinations.healthRecordCreate())
                        },
                        onOpenProfile = {
                            navController.navigate(CardioDestinations.PROFILE)
                        },
                        onOpenVitamins = {
                            navController.navigate(CardioDestinations.VITAMINS)
                        },
                    )
                }
            }

            composable(route = CardioDestinations.PROFILE) {
                ProfileRoute(
                    onBack = { navController.popBackStack() },
                )
            }

            composable(CardioDestinations.HISTORY) {
                HistoryRoute(
                    onOpenRecordsForDate = { dateKey ->
                        navController.navigate(CardioDestinations.healthRecordsList(dateKey))
                    },
                )
            }

            composable(
                route = CardioDestinations.HEALTH_RECORDS_LIST,
                arguments = listOf(
                    navArgument(CardioDestinations.HEALTH_RECORDS_DATE_ARG) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                ),
            ) {
                HealthRecordsRoute(
                    onOpenRecord = { recordId ->
                        navController.navigate(CardioDestinations.healthRecordDetail(recordId))
                    },
                    onBack = { navController.popBackStack() },
                )
            }

            composable(
                route = CardioDestinations.HEALTH_RECORD_DETAIL,
                arguments = listOf(
                    navArgument(CardioDestinations.HEALTH_RECORD_DETAIL_ARG) {
                        type = NavType.StringType
                    },
                ),
            ) { backStackEntry ->
                val recordId = backStackEntry.arguments?.getString(HEALTH_RECORD_DETAIL_ARG).orEmpty()
                HealthRecordDetailRoute(
                    onBack = { navController.popBackStack() },
                    onEditMetric = { metricType ->
                        navController.safeNavigate(
                            CardioDestinations.healthMetricEdit(recordId, metricType),
                        )
                    },
                )
            }

            composable(
                route = CardioDestinations.HEALTH_METRIC_EDIT,
                arguments = listOf(
                    navArgument(CardioDestinations.HEALTH_METRIC_EDIT_RECORD_ARG) {
                        type = NavType.StringType
                    },
                    navArgument(CardioDestinations.HEALTH_METRIC_EDIT_TYPE_ARG) {
                        type = NavType.StringType
                    },
                ),
            ) {
                HealthMetricEditRoute(
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() },
                )
            }

            composable(
                route = CardioDestinations.HEALTH_RECORD_CREATE,
                arguments = listOf(
                    navArgument(CardioDestinations.HEALTH_RECORD_CREATE_ARG) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                ),
                ) {
                    HealthRecordCreateRoute(
                        onExit = { navController.popBackStack() },
                        onFinished = { navController.popBackStack() },
                        onOpenHelp = {
                            navController.navigate(CardioDestinations.HELP) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }

            composable(CardioDestinations.VITAMINS) {
                VitaminsRoute(
                    onBack = { navController.popBackStack() },
                )
            }

            composable(CardioDestinations.HELP) {
                HelpRoute()
            }
        }
    }
}

private fun NavDestination?.showsBottomBar(): Boolean {
    val route = this?.route ?: return false
    return route == CardioDestinations.TODAY_HOME ||
        route == CardioDestinations.HISTORY ||
        route == CardioDestinations.HELP ||
        route == CardioDestinations.VITAMINS
}

fun NavController.safeNavigate(route: String) {
    if (currentBackStackEntry?.destination?.route != route) {
        navigate(route) { launchSingleTop = true }
    }
}

private fun NavDestination?.topLevelDestination(): TopLevelDestination? {
    when (this?.route) {
        CardioDestinations.HISTORY -> return TopLevelDestination.HISTORY
        CardioDestinations.HELP -> return TopLevelDestination.HELP
    }
    if (this?.hierarchy?.any { it.route == CardioDestinations.TODAY_GRAPH } == true) {
        return TopLevelDestination.TODAY
    }
    return null
}

private fun NavHostController.navigateTopLevel(destination: TopLevelDestination) {
    navigate(destination.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
