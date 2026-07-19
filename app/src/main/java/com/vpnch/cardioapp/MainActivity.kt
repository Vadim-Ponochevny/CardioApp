package com.vpnch.cardioapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.vpnch.cardioapp.core.domain.analytics.Analytics
import com.vpnch.cardioapp.core.domain.analytics.AnalyticsEvent
import com.vpnch.cardioapp.navigation.CardioDestinations
import com.vpnch.cardioapp.ui.theme.CardioAppTheme
import com.vpnch.cardioapp.worker.EXTRA_NOTIFICATION_TYPE
import com.vpnch.cardioapp.worker.NOTIFICATION_TYPE_DAILY
import com.vpnch.cardioapp.worker.NOTIFICATION_TYPE_SURVEY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var analytics: Analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val initialRoute = when (intent.getStringExtra(EXTRA_NOTIFICATION_TYPE)) {
            NOTIFICATION_TYPE_SURVEY -> {
                analytics.report(AnalyticsEvent.OpenedFromSurveyNotification)
                CardioDestinations.HELP
            }
            NOTIFICATION_TYPE_DAILY -> {
                analytics.report(AnalyticsEvent.OpenedFromDailyNotification)
                null
            }
            else -> null
        }

        setContent {
            CardioAppTheme {
                SideEffect {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.light(
                            android.graphics.Color.TRANSPARENT,
                            android.graphics.Color.TRANSPARENT,
                        ),
                        navigationBarStyle = SystemBarStyle.light(
                            android.graphics.Color.TRANSPARENT,
                            android.graphics.Color.TRANSPARENT,
                        ),
                    )
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    CardioApp(
                        initialRoute = initialRoute,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
