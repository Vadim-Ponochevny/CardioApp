package com.vpnch.cardioapp.feature.healthrecords.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.common.DateUtils.formatTime
import com.vpnch.cardioapp.core.model.health.formatAsHealthMetric
import com.vpnch.cardioapp.core.model.health.formatAsInr
import com.vpnch.cardioapp.core.model.health.formatBloodPressure
import com.vpnch.cardioapp.core.ui.CardioDialog
import com.vpnch.cardioapp.core.ui.CardioDialogButton
import com.vpnch.cardioapp.core.ui.HealthMetricSummaryRow
import com.vpnch.cardioapp.core.ui.MetricAlertBadge
import com.vpnch.cardioapp.core.ui.MetricDisplayItem
import com.vpnch.cardioapp.core.ui.MetricNormBadge
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.healthrecords.HealthRecordListItem
import com.vpnch.cardioapp.feature.healthrecords.model.HealthMetricKind
import com.vpnch.cardioapp.feature.healthrecords.ui.extensions.displayTitle
import com.vpnch.cardioapp.feature.healthrecords.ui.extensions.iconRes
import kotlinx.coroutines.launch

private val CARD_CORNER_RADIUS = 36.dp
private val CARD_CONTENT_PADDING = 16.dp
private val ARROW_ICON_SIZE = 20.dp
private val DELETE_ICON_SIZE = 24.dp
private val DELETE_CORNER_RADIUS = 36.dp
private val ColorDeleteRed = Color(0xFFE53935)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthRecordListItemCard(
    item: HealthRecordListItem,
    onOpenRecord: () -> Unit,
    onDeleteRecord: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                showDeleteDialog = true
                false // не dismiss сразу — ждём подтверждения
            } else false
        },
    )

    if (showDeleteDialog) {
        CardioDialog(
            title = "Удалить запись?",
            message = "Это действие нельзя отменить.",
            primaryButton = CardioDialogButton(
                label = "Удалить",
                containerColor = CardioTheme.colors.cardSurvey,
                contentColor = CardioTheme.colors.onPrimary,
                onClick = {
                    showDeleteDialog = false
                    onDeleteRecord()
                },
            ),
            secondaryButton = CardioDialogButton(
                label = "Отмена",
                containerColor = CardioTheme.colors.background,
                contentColor = CardioTheme.colors.textMain,
                onClick = {
                    showDeleteDialog = false
                    scope.launch { dismissState.reset() }
                },
            ),
            onDismiss = {
                showDeleteDialog = false
                scope.launch { dismissState.reset() }
            },
        )
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color by animateColorAsState(
                targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart)
                    ColorDeleteRed else Color.Transparent,
                label = "swipe_bg",
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, RoundedCornerShape(DELETE_CORNER_RADIUS))
                    .padding(end = CARD_CONTENT_PADDING),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(DELETE_ICON_SIZE),
                )
            }
        },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpenRecord),
            shape = RoundedCornerShape(CARD_CORNER_RADIUS),
            colors = CardDefaults.cardColors(containerColor = CardioTheme.colors.onPrimary),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(modifier = Modifier.padding(CARD_CONTENT_PADDING)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (item.hasOutOfNorm) {
                        MetricAlertBadge(isCritical = item.hasCritical)
                    } else {
                        MetricNormBadge()
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = formatTime(item.record.createdAt),
                        style = CardioTheme.typography.bodySmall,
                        color = CardioTheme.colors.textSecondary,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = CardioTheme.colors.textSecondary,
                        modifier = Modifier.size(ARROW_ICON_SIZE),
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0xFFE0E0E0),
                )

                HealthMetricSummaryRow(
                    metrics = item.record.toMetricDisplayItems(),
                )
            }
        }
    }
}

private fun com.vpnch.cardioapp.core.model.health.HealthRecord.toMetricDisplayItems(): List<MetricDisplayItem> {
    val compact = inr != null
    return buildList {
        add(MetricDisplayItem(
            iconRes = HealthMetricKind.BloodPressure.iconRes,
            value = if (compact)
                "${systolicPressure.formatAsHealthMetric()}\n${diastolicPressure.formatAsHealthMetric()}"
            else
                formatBloodPressure(systolicPressure, diastolicPressure),
            label = if (compact) "Давл." else HealthMetricKind.BloodPressure.displayTitle,
        ))
        add(MetricDisplayItem(
            iconRes = HealthMetricKind.RespiratoryRate.iconRes,
            value = respiratoryRate.formatAsHealthMetric(),
            label = if (compact) "Дых." else HealthMetricKind.RespiratoryRate.displayTitle,
        ))
        add(MetricDisplayItem(
            iconRes = HealthMetricKind.HeartRate.iconRes,
            value = heartRate.formatAsHealthMetric(),
            label = HealthMetricKind.HeartRate.displayTitle,
        ))
        add(MetricDisplayItem(
            iconRes = HealthMetricKind.OxygenSaturation.iconRes,
            value = oxygenSaturation.formatAsHealthMetric(),
            label = if (compact) "Кисл." else HealthMetricKind.OxygenSaturation.displayTitle,
        ))
        inr?.let {
            add(MetricDisplayItem(
                iconRes = HealthMetricKind.INR.iconRes,
                value = it.formatAsInr(),
                label = HealthMetricKind.INR.displayTitle,
            ))
        }
    }
}
