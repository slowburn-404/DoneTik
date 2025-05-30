package com.datahiveorg.donetik.ui.components

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * A composable function that displays a DatePickerDialog when `showDateTimePicker` is true.
 *
 * @param modifier Modifier to be applied to the DatePickerDialog.
 * @param onSelectDate Callback function invoked when a date is selected.
 * @param showDateTimePicker Boolean flag to control the visibility of the DatePickerDialog.
 * @param onDismiss Callback function invoked when the DatePickerDialog is dismissed.
 * @param dataPickerState State object that can be used to control the DatePicker.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoneTikDatePicker(
    modifier: Modifier = Modifier,
    onSelectDate: (DatePickerState) -> Unit,
    showDateTimePicker: Boolean = false,
    onDismiss: () -> Unit,
    dataPickerState: DatePickerState = rememberDatePickerState()
) {
    if (showDateTimePicker) {
        DatePickerDialog(
            modifier = modifier,
            onDismissRequest = onDismiss,
            confirmButton = {
                DoneTikTextButton(
                    text = "Select Date",
                    onClick = {
                        onSelectDate(dataPickerState)
                        onDismiss()
                    }
                )
            },
            dismissButton = {
                DoneTikTextButton(
                    text = "Cancel",
                    onClick = onDismiss,
                    color = colorScheme.secondary
                )
            },
        ) {
            DatePicker(state = dataPickerState)
        }
    }
}

/**
 * A composable function that displays a time picker dialog.
 *
 * @param modifier Modifier to be applied to the dialog.
 * @param onSelectTime Callback function invoked when a time is selected.
 * @param showTimePicker Boolean to control the visibility of the time picker dialog.
 * @param onDismiss Callback function invoked when the dialog is dismissed.
 * @param calendar The calendar instance to initialize the time picker with. Defaults to the current system calendar.
 * @param timePickerState The state of the time picker. Defaults to a state initialized with the current time.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoneTikTimePicker(
    modifier: Modifier = Modifier,
    onSelectTime: (TimePickerState) -> Unit,
    showTimePicker: Boolean = false,
    onDismiss: () -> Unit,
    calendar: Calendar = rememberCalendarInstance(),
    timePickerState: TimePickerState = rememberTimePickerState(
        initialMinute = calendar.get(Calendar.MINUTE),
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        is24Hour = true
    ),
) {
    if (showTimePicker) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Card(
                shape = shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TimePicker(
                        state = timePickerState
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        DoneTikTextButton(
                            text = "Cancel",
                            onClick = onDismiss,
                            color = colorScheme.secondary
                        )

                        DoneTikTextButton(
                            text = "Select Time",
                            onClick = {
                                onSelectTime(timePickerState)
                                onDismiss()
                            },
                            color = colorScheme.primary
                        )

                    }
                }
            }
        }

    }
}

/**
 * Remembers a [Calendar] instance.
 *
 * This composable function provides a [Calendar] instance that can be used
 * for date and time operations. It is useful when you need to access the current
 * date or time within a composable function.
 *
 * @return A [Calendar] instance representing the current date and time.
 */
@Composable
fun rememberCalendarInstance(): Calendar {
    return Calendar.getInstance()
}