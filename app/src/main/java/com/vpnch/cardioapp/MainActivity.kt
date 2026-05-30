package com.vpnch.cardioapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vpnch.cardioapp.feature.healthrecords.HealthRecordsRoute
import com.vpnch.cardioapp.feature.help.HelpRoute
import com.vpnch.cardioapp.feature.history.HistoryRoute
import com.vpnch.cardioapp.feature.today.TodayRoute
import com.vpnch.cardioapp.feature.vitamins.VitaminsRoute
import com.vpnch.cardioapp.ui.theme.CardioAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardioAppTheme {
                CardioApp(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
fun CardioApp(
    modifier: Modifier = Modifier,
) {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Today) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                BottomDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentScreen.bottomDestination == destination,
                        onClick = { currentScreen = destination.toScreen() },
                        icon = { Text(destination.iconText) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                AppScreen.Today -> TodayRoute(
                    onOpenTodayRecords = { currentScreen = AppScreen.HealthRecords },
                    onAddHealthRecord = { currentScreen = AppScreen.HealthRecords },
                    onOpenVitamins = { currentScreen = AppScreen.Vitamins },
                    onOpenSurvey = {},
                )

                AppScreen.History -> HistoryRoute()
                AppScreen.Help -> HelpRoute()
                AppScreen.HealthRecords -> HealthRecordsRoute()
                AppScreen.Vitamins -> VitaminsRoute()
            }
        }
    }
}

private enum class BottomDestination(
    val label: String,
    val iconText: String,
) {
    Today("Сегодня", "С"),
    History("История", "И"),
    Help("Помощь", "?"),
}

private fun BottomDestination.toScreen(): AppScreen = when (this) {
    BottomDestination.Today -> AppScreen.Today
    BottomDestination.History -> AppScreen.History
    BottomDestination.Help -> AppScreen.Help
}

private sealed interface AppScreen {
    val bottomDestination: BottomDestination?

    data object Today : AppScreen {
        override val bottomDestination = BottomDestination.Today
    }

    data object History : AppScreen {
        override val bottomDestination = BottomDestination.History
    }

    data object Help : AppScreen {
        override val bottomDestination = BottomDestination.Help
    }

    data object HealthRecords : AppScreen {
        override val bottomDestination = BottomDestination.Today
    }

    data object Vitamins : AppScreen {
        override val bottomDestination = BottomDestination.Today
    }
}

@Preview(showBackground = true)
@Composable
fun CardioAppPreview() {
    CardioAppTheme {
        Text("CardioApp")
    }
}