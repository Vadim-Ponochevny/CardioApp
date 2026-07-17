package com.vpnch.cardioapp.core.data.di

import com.vpnch.cardioapp.core.data.notification.NotificationSchedulerImpl
import com.vpnch.cardioapp.core.domain.notification.NotificationScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NotificationsModule {
    @Binds
    @Singleton
    abstract fun bindNotificationScheduler(impl: NotificationSchedulerImpl): NotificationScheduler
}
