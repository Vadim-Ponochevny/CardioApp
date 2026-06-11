package com.vpnch.cardioapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
import com.vpnch.cardioapp.feature.healthrecords.HealthRecordsRoute
import com.vpnch.cardioapp.feature.healthrecords.create.HealthRecordCreateRoute
import com.vpnch.cardioapp.feature.healthrecords.detail.HealthRecordDetailRoute
import com.vpnch.cardioapp.feature.healthrecords.edit.HealthMetricEditRoute
import com.vpnch.cardioapp.feature.help.HelpRoute
import com.vpnch.cardioapp.feature.history.HistoryRoute
import com.vpnch.cardioapp.feature.today.TodayRoute
import com.vpnch.cardioapp.feature.vitamins.VitaminsRoute
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

@Composable
fun CardioNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination.showsBottomBar()
    val selectedTopLevel = currentDestination.topLevelDestination()

    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFFF6F6F6),
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
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = { Text(destination.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CardioTheme.colors.primary,      // цвет иконки когда выбрана
                                unselectedIconColor = CardioTheme.colors.textSecondary, // цвет иконки когда не выбрана
                                selectedTextColor = CardioTheme.colors.primary,      // цвет текста когда выбран
                                unselectedTextColor = CardioTheme.colors.textSecondary, // цвет текста когда не выбран
                                indicatorColor = CardioTheme.colors.navSelectedContainer // цвет индикатора выбора
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
                route = CardioDestinations.TODAY_GRAPH,
                startDestination = CardioDestinations.TODAY_HOME,
            ) {
                composable(route = CardioDestinations.TODAY_HOME) {
                    TodayRoute(
                        onOpenLatestRecord = { recordId ->
                            navController.navigate(CardioDestinations.healthRecordDetail(recordId))
                        },
                        onAddHealthRecord = {
                            navController.navigate(CardioDestinations.healthRecordCreate())
                        },
                        onOpenSurvey = {},
                    )
                }
                composable(route = CardioDestinations.VITAMINS) {
                    VitaminsRoute()
                }
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
            ) {
                HealthRecordDetailRoute(
                    onBack = { navController.popBackStack() },
                    onEditMetric = { metricType ->
                        val recordId = navController.currentBackStackEntry
                            ?.arguments
                            ?.getString(CardioDestinations.HEALTH_RECORD_DETAIL_ARG)
                            .orEmpty()
                        navController.navigate(
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
        route == CardioDestinations.HELP
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
