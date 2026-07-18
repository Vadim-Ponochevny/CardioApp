package com.vpnch.cardioapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vpnch.cardioapp.MainActivity
import com.vpnch.cardioapp.R
import com.vpnch.cardioapp.core.data.sync.AppConfigSyncer
import com.vpnch.cardioapp.core.domain.repository.SurveyRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val CHANNEL_ID = "survey_updates"
private const val NOTIFICATION_ID = 1002
const val EXTRA_NOTIFICATION_TYPE = "notification_type"
const val NOTIFICATION_TYPE_SURVEY = "survey"
const val NOTIFICATION_TYPE_DAILY = "daily"

@HiltWorker
class SurveyCheckWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val surveyRepository: SurveyRepository,
    private val appConfigSyncer: AppConfigSyncer,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val previousId = surveyRepository.getCurrentSurveyId()
        appConfigSyncer.sync()
        val newId = surveyRepository.getCurrentSurveyId()

        if (newId != null && newId != previousId) {
            showNotification()
        }

        return Result.success()
    }

    private fun showNotification() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Опросы",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_NOTIFICATION_TYPE, NOTIFICATION_TYPE_SURVEY)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Новый опрос")
            .setContentText("Доступен новый опрос. Нажмите, чтобы пройти")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
