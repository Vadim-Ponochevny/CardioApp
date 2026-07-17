package com.vpnch.cardioapp.feature.vitamins

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.CardioTopBar
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.vitamins.components.VitaminListItem

private val SCREEN_BG_COLOR = Color(0xFFF6F6F6)

private val CONTENT_HORIZONTAL_PADDING = 16.dp
private val CONTENT_TOP_PADDING = 8.dp
private val CONTENT_BOTTOM_PADDING = 88.dp
private val LIST_ITEM_SPACING = 8.dp

private val FAB_SIZE = 72.dp
private val FAB_ICON_SIZE = 48.dp
private val FAB_END_PADDING = 16.dp
private val FAB_BOTTOM_PADDING = 16.dp

private const val TITLE = "Витаминки"
private const val MSG_EMPTY = "Витаминки не добавлены"

@Composable
fun VitaminsScreen(
    uiState: VitaminsUiState,
    onBack: () -> Unit,
    onAddClick: () -> Unit,
    onDeleteVitamin: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SCREEN_BG_COLOR),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CardioTopBar(title = TITLE, onBack = onBack)

            if (uiState.vitamins.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = MSG_EMPTY,
                        style = CardioTheme.typography.bodyMedium,
                        color = CardioTheme.colors.textSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = CONTENT_HORIZONTAL_PADDING,
                        end = CONTENT_HORIZONTAL_PADDING,
                        top = CONTENT_TOP_PADDING,
                        bottom = CONTENT_BOTTOM_PADDING,
                    ),
                    verticalArrangement = Arrangement.spacedBy(LIST_ITEM_SPACING),
                ) {
                    items(uiState.vitamins, key = { it.id }) { vitamin ->
                        VitaminListItem(
                            vitamin = vitamin,
                            onDelete = { onDeleteVitamin(vitamin.id) },
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddClick,
            containerColor = CardioTheme.colors.primary,
            contentColor = CardioTheme.colors.onPrimary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = FAB_END_PADDING, bottom = FAB_BOTTOM_PADDING)
                .size(FAB_SIZE),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(FAB_ICON_SIZE),
            )
        }
    }
}
