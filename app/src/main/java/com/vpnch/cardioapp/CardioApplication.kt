package com.vpnch.cardioapp

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration as WorkConfiguration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.vpnch.cardioapp.core.data.database.DatabaseSeeder
import com.vpnch.cardioapp.core.domain.analytics.Analytics
import com.vpnch.cardioapp.core.domain.analytics.AnalyticsEvent
import com.vpnch.cardioapp.core.data.sync.AppConfigSyncer
import com.vpnch.cardioapp.core.domain.notification.NotificationScheduler
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.worker.SurveyCheckWorker
import com.vpnch.cardioapp.R
import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private const val PREFS_NAME = "cardio_prefs"
private const val KEY_FIRST_LAUNCH = "first_launch"

@HiltAndroidApp
class CardioApplication : Application(), WorkConfiguration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var databaseSeeder: DatabaseSeeder

    @Inject
    lateinit var analytics: Analytics

    @Inject
    lateinit var appConfigSyncer: AppConfigSyncer

    @Inject
    lateinit var patientRepository: PatientRepository

    @Inject
    lateinit var notificationScheduler: NotificationScheduler

    override val workManagerConfiguration: WorkConfiguration
        get() = WorkConfiguration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun attachBaseContext(newBase: Context) {
        val config = Configuration(newBase.resources.configuration)
        config.uiMode = config.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv() or Configuration.UI_MODE_NIGHT_NO
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate() {
        super.onCreate()

        val metricaConfig = AppMetricaConfig.newConfigBuilder(getString(R.string.appmetrica_api_key)).build()
        AppMetrica.activate(this, metricaConfig)

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            databaseSeeder.seedIfNeeded()
            appConfigSyncer.sync()
            if (patientRepository.getNotificationsEnabled()) {
                notificationScheduler.schedule()
            }
        }

        val surveyCheckRequest = PeriodicWorkRequestBuilder<SurveyCheckWorker>(6, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "survey_check",
            ExistingPeriodicWorkPolicy.KEEP,
            surveyCheckRequest,
        )

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        if (isFirstLaunch) {
            analytics.report(AnalyticsEvent.FirstOpen)
            prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
        }
        analytics.report(AnalyticsEvent.AppOpened)
    }
}
