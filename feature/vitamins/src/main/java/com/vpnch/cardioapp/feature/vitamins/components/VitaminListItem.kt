package com.vpnch.cardioapp.feature.vitamins.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.vitamins.Vitamin
import com.vpnch.cardioapp.core.ui.CardioDialog
import com.vpnch.cardioapp.core.ui.CardioDialogButton
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.core.ui.R as CoreUiR

private val CARD_CORNER_RADIUS = 36.dp
private val CARD_HORIZONTAL_PADDING = 20.dp
private val CARD_VERTICAL_PADDING = 16.dp
private val ICON_SIZE = 28.dp
private val ICON_SPACING = 12.dp
private val DELETE_THRESHOLD = 0.4f
private val ColorDeleteRed = Color(0xFFE53935)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VitaminListItem(
    vitamin: Vitamin,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                showDeleteDialog = true
            }
            false
        },
        positionalThreshold = { totalDistance -> totalDistance * DELETE_THRESHOLD },
    )

    LaunchedEffect(showDeleteDialog) {
        if (!showDeleteDialog) dismissState.reset()
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color by animateColorAsState(
                targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart)
                    ColorDeleteRed else Color(0xFFFFCDD2),
                label = "swipe_bg",
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, RoundedCornerShape(CARD_CORNER_RADIUS)),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(end = CARD_HORIZONTAL_PADDING),
                )
            }
        },
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(CARD_CORNER_RADIUS),
            colors = CardDefaults.cardColors(containerColor = CardioTheme.colors.onPrimary),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = CARD_HORIZONTAL_PADDING, vertical = CARD_VERTICAL_PADDING),
                horizontalArrangement = Arrangement.spacedBy(ICON_SPACING),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(CoreUiR.drawable.ic_vitamin),
                    contentDescription = null,
                    modifier = Modifier.size(ICON_SIZE),
                )
                Text(
                    text = vitamin.name,
                    style = CardioTheme.typography.bodyMedium,
                    color = CardioTheme.colors.textMain,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }

    if (showDeleteDialog) {
        CardioDialog(
            title = vitamin.name,
            message = "Удалить витаминку?",
            primaryButton = CardioDialogButton(
                label = "Удалить",
                containerColor = ColorDeleteRed,
                contentColor = Color.White,
                onClick = {
                    showDeleteDialog = false
                    onDelete()
                },
            ),
            secondaryButton = CardioDialogButton(
                label = "Отмена",
                containerColor = CardioTheme.colors.textDisabled,
                contentColor = CardioTheme.colors.textMain,
                onClick = { showDeleteDialog = false },
            ),
            onDismiss = { showDeleteDialog = false },
        )
    }
}
