package com.vpnch.cardioapp.core.domain.notification

interface NotificationScheduler {
    fun schedule()
    fun cancel()
}
