package com.datahiveorg.donetik.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

/**
 * Composable function for displaying a bottom sheet with options.
 *
 * @param modifier The modifier to be applied to the bottom sheet.
 * @param options The list of options to be displayed in the bottom sheet.
 * @param onDismiss The callback to be invoked when the bottom sheet is dismissed.
 * @param onOptionsClicked The callback to be invoked when an option is clicked.
 * @param sheetState The state of the bottom sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsBottomSheet(
    modifier: Modifier = Modifier,
    options: List<BottomSheetOptions>,
    onDismiss: () -> Unit,
    onOptionsClicked: (BottomSheetOptions) -> Unit,
    sheetState: SheetState,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            for (option in options) {
                BottomSheetItem(
                    iconId = option.icon,
                    label = option.label,
                    onClick = {
                        onOptionsClicked(option)
                    }
                )
            }
        }
    }
}

/**
 * A composable function that displays a single item in a bottom sheet.
 *
 * @param iconId The resource ID of the icon to display.
 * @param label The text to display for the item.
 * @param onClick A callback function that is invoked when the item is clicked.
 */
@Composable
fun BottomSheetItem(
    iconId: Int,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                role = Role.Button,
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = label
        )
        Text(
            text = label,
            style = typography.bodyLarge,
        )
    }
}