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

@Composable
fun rememberCalendarInstance(): Calendar {
    return Calendar.getInstance()
}