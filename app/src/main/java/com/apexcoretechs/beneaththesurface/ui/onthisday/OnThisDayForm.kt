package com.apexcoretechs.beneaththesurface.ui.onthisday

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnThisDayForm(
    onSubmit: (day: Int, month: Int) -> Unit
) {
    var selectedMonth by remember { mutableStateOf<String?>(null) }
    var selectedDay by remember { mutableStateOf<String?>(null) }
    var expandedMonth by remember { mutableStateOf(false) }
    var expandedDay by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val months = (1..12).map { it.toString().padStart(2, '0') }

    // Dynamically compute number of days in selected month
    val days = remember(selectedMonth) {
        ->
        val monthInt = selectedMonth?.toIntOrNull()
        val year = LocalDate.now().year
        val daysInMonth = if (monthInt != null) {
            YearMonth.of(year, monthInt).lengthOfMonth()
        } else 31
        (1..daysInMonth).map { it.toString().padStart(2, '0') }
    }

    // Reset day if it exceeds new max
    LaunchedEffect(selectedMonth) {
        val maxDay = days.lastOrNull()?.toIntOrNull()
        val dayInt = selectedDay?.toIntOrNull()
        if (dayInt != null && maxDay != null && dayInt > maxDay) {
            selectedDay = null
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {

        // Month Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedMonth,
            onExpandedChange = { expandedMonth = !expandedMonth }
        ) {
            TextField(
                value = selectedMonth ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Month") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMonth) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedMonth,
                onDismissRequest = { expandedMonth = false }
            ) {
                months.forEach { month ->
                    DropdownMenuItem(
                        text = { Text(month) },
                        onClick = {
                            selectedMonth = month
                            expandedMonth = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Day Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedDay,
            onExpandedChange = { expandedDay = !expandedDay }
        ) {
            TextField(
                value = selectedDay ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Day") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDay) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedDay,
                onDismissRequest = { expandedDay = false }
            ) {
                days.forEach { day ->
                    DropdownMenuItem(
                        text = { Text(day) },
                        onClick = {
                            selectedDay = day
                            expandedDay = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error Text
        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Today Button
            Button(
                onClick = {
                    val today = LocalDate.now()
                    val month = today.monthValue.toString().padStart(2, '0')
                    val day = today.dayOfMonth.toString().padStart(2, '0')
                    selectedMonth = month
                    selectedDay = day
                }
            ) {
                Text("Today", color = Color.White)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Submit Button
            Button(
                onClick = {
                    val monthInt = selectedMonth?.toIntOrNull()
                    val dayInt = selectedDay?.toIntOrNull()

                    errorMessage = when {
                        selectedMonth.isNullOrBlank() || selectedDay.isNullOrBlank() ->
                            "Both fields are required."
                        dayInt !in 1..31 ->
                            "Day must be between 1 and 31."
                        monthInt !in 1..12 ->
                            "Month must be between 1 and 12."
                        else -> null
                    }

                    if (errorMessage == null && monthInt != null && dayInt != null) {
                        onSubmit(dayInt, monthInt)
                    }
                }
            ) {
                Text("Get History", color = Color.White)
            }
        }
    }
}
