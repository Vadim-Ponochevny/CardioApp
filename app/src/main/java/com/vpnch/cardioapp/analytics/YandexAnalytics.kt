package com.vpnch.cardioapp.analytics

import com.vpnch.cardioapp.core.domain.analytics.Analytics
import com.vpnch.cardioapp.core.domain.analytics.AnalyticsEvent
import io.appmetrica.analytics.AppMetrica
import javax.inject.Inject

class YandexAnalytics @Inject constructor() : Analytics {
    override fun report(event: AnalyticsEvent) {
        AppMetrica.reportEvent(event.name)
    }
}
