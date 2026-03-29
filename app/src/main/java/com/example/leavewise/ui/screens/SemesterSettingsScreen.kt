package com.example.leavewise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.leavewise.ui.AttendanceViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterSettingsScreen(
    viewModel: AttendanceViewModel,
    onBack: () -> Unit
) {
    var startDate by remember { mutableStateOf(viewModel.semesterStart.value) }
    var endDate by remember { mutableStateOf(viewModel.semesterEnd.value) }
    var minAttendance by remember { mutableStateOf(viewModel.minAttendance.value.toString()) }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        startDate = dateFormatter.format(Date(it))
                    }
                    showStartDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        endDate = dateFormatter.format(Date(it))
                    }
                    showEndDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Semester Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Configure your semester details", color = Color.Gray)

            OutlinedTextField(
                value = startDate,
                onValueChange = { },
                label = { Text("Semester Start Date") },
                placeholder = { Text("dd-mm-yyyy") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(Icons.Default.CalendarToday, null, Modifier.clickable { showStartDatePicker = true })
                },
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = endDate,
                onValueChange = { },
                label = { Text("Semester End Date") },
                placeholder = { Text("dd-mm-yyyy") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(Icons.Default.CalendarToday, null, Modifier.clickable { showEndDatePicker = true })
                },
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = minAttendance,
                onValueChange = { minAttendance = it },
                label = { Text("Minimum Attendance %") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.semesterStart.value = startDate
                    viewModel.semesterEnd.value = endDate
                    viewModel.minAttendance.value = minAttendance.toFloatOrNull() ?: 75f
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6CF7))
            ) {
                Text("Save Settings")
            }
        }
    }
}