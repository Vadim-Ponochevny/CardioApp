package com.vpnch.cardioapp

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.vpnch.cardioapp.core.data.database.DatabaseSeeder
import com.vpnch.cardioapp.core.domain.analytics.Analytics
import com.vpnch.cardioapp.core.domain.analytics.AnalyticsEvent
import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private const val APPMETRICA_API_KEY = "f40c5411-ffdc-4930-86f0-dd8311bd9cf5"
private const val PREFS_NAME = "cardio_prefs"
private const val KEY_FIRST_LAUNCH = "first_launch"

@HiltAndroidApp
class CardioApplication : Application() {
    @Inject
    lateinit var databaseSeeder: DatabaseSeeder

    @Inject
    lateinit var analytics: Analytics

    override fun attachBaseContext(newBase: Context) {
        val config = Configuration(newBase.resources.configuration)
        config.uiMode = config.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv() or Configuration.UI_MODE_NIGHT_NO
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate() {
        super.onCreate()

        val metricaConfig = AppMetricaConfig.newConfigBuilder(APPMETRICA_API_KEY).build()
        AppMetrica.activate(this, metricaConfig)

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            databaseSeeder.seedIfNeeded()
        }

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        if (isFirstLaunch) {
            analytics.report(AnalyticsEvent.FirstOpen)
            prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
        }
        analytics.report(AnalyticsEvent.AppOpened)
    }
}
