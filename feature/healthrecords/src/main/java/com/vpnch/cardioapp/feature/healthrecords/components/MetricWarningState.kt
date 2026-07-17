package com.vpnch.cardioapp.feature.healthrecords.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.vpnch.cardioapp.core.model.health.metrics.MetricStatus

// Shared warning-visibility state for metric input sections.
// forceShowWarning — set true when a loaded record already has an out-of-norm value.
// inputs — the input strings whose change resets the warning visibility.
// Returns (isVisible, triggerWarning) where triggerWarning() shows the warning if warning != null.
@Composable
internal fun rememberMetricWarningState(
    forceShowWarning: Boolean,
    warning: MetricStatus?,
    vararg inputs: String,
): Pair<Boolean, () -> Unit> {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(forceShowWarning) {
        if (forceShowWarning) visible = true
    }
    @Suppress("SpreadOperator")
    LaunchedEffect(*inputs) {
        if (!forceShowWarning) visible = false
    }

    return visible to { visible = warning != null }
}
