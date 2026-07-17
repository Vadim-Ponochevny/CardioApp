package com.vpnch.cardioapp.analytics

import com.vpnch.cardioapp.core.domain.analytics.Analytics
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    @Singleton
    abstract fun bindAnalytics(impl: YandexAnalytics): Analytics
}
