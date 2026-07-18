package com.vpnch.cardioapp.feature.profile.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val DROPDOWN_HEIGHT = 60.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AgeGroupDropdown(
    selected: AgeGroup,
    useCustomLimits: Boolean,
    onSelect: (AgeGroup) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val displayLabel = if (useCustomLimits) "Индивид." else selected.label
    val selectableGroups = AgeGroup.entries.filter { it != AgeGroup.Custom }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
                .height(DROPDOWN_HEIGHT)
                .border(3.dp, CardioTheme.colors.textDisabled, ProfileCardShape),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = displayLabel,
                style = CardioTheme.typography.bodySmall.copy(color = CardioTheme.colors.textMain),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 52.dp),
                maxLines = 1,
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
                tint = CardioTheme.colors.textMain,
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = CardioTheme.colors.onPrimary,
        ) {
            selectableGroups.forEach { group ->
                DropdownMenuItem(
                    text = { Text(group.label, style = CardioTheme.typography.bodySmall) },
                    onClick = { onSelect(group); expanded = false },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
