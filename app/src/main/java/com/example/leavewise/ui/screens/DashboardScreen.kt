package com.example.leavewise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.leavewise.ui.AttendanceViewModel
import com.example.leavewise.ui.AuthViewModel
import com.example.leavewise.ui.Subject

@Composable
fun DashboardScreen(
    viewModel: AttendanceViewModel,
    authViewModel: AuthViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val actualPercent = viewModel.overallActualPercentage
    val withEventPercent = viewModel.overallWithEventPercentage

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F3F7))
    ) {
        // Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFF4A6CF7), Color(0xFF8E2DE2))
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "LeaveWise",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row {
                            IconButton(onClick = { onNavigate("semester") }) {
                                Icon(Icons.Default.Settings, null, tint = Color.White)
                            }
                            IconButton(onClick = { 
                                authViewModel.logout {
                                    onLogout()
                                }
                            }) {
                                Icon(Icons.Default.Logout, null, tint = Color.White)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AttendanceSummaryCard(
                            title = "Actual",
                            percentage = actualPercent,
                            present = viewModel.totalPresent,
                            total = viewModel.totalClasses,
                            modifier = Modifier.weight(1f)
                        )
                        AttendanceSummaryCard(
                            title = "With Event",
                            percentage = withEventPercent,
                            present = viewModel.totalPresent + viewModel.totalEvent,
                            total = viewModel.totalClasses,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Subject-wise Planning", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Manage your class and event attendance per subject", fontSize = 12.sp, color = Color.Gray)
            }
        }

        items(viewModel.subjects) { subject ->
            DetailedSubjectCard(subject, viewModel)
        }
        
        item {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { onNavigate("timetable") },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("Manage Timetable")
                }
                Button(
                    onClick = { onNavigate("planner") },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6CF7))
                ) {
                    Text("Plan Leaves")
                }
            }
        }
        
        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
fun AttendanceSummaryCard(title: String, percentage: Float, present: Int, total: Int, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, color = Color.White, fontSize = 12.sp)
            Text("${percentage.toInt()}%", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("$present/$total classes", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { if (total == 0) 0f else percentage / 100f },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun DetailedSubjectCard(subject: Subject, viewModel: AttendanceViewModel) {
    val target = viewModel.minAttendance.value
    val actual = subject.actualPercentage
    val withEvent = subject.withEventPercentage

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(subject.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF4A6CF7))
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Actual", fontSize = 12.sp, color = Color.Gray)
                    Text("${actual.toInt()}%", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if(actual < target) Color.Red else Color.Black)
                }
                Column {
                    Text("With Event", fontSize = 12.sp, color = Color.Gray)
                    Text("${withEvent.toInt()}%", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if(withEvent < target) Color.Red else Color(0xFF4CAF50))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Total Classes", fontSize = 12.sp, color = Color.Gray)
                    Text("${subject.totalClasses}", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            LinearProgressIndicator(
                progress = { if (subject.totalClasses == 0) 0f else withEvent / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = if (withEvent < target) Color.Red else Color(0xFF4CAF50),
                trackColor = Color(0xFFEEEEEE),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Warning Section
            if (actual < target) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp)).padding(8.dp)) {
                    Icon(Icons.Default.Warning, null, tint = Color.Red, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (withEvent >= target) "You meet the attendance requirement if event attendance is approved."
                               else "Your actual attendance is below the minimum requirement.",
                        fontSize = 11.sp,
                        color = Color.Red,
                        lineHeight = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Inputs
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AttendanceInput(
                    label = "Total", 
                    value = subject.totalClasses.toString(),
                    modifier = Modifier.weight(1f)
                ) { 
                    viewModel.updateSubjectAttendance(subject.id, it.toIntOrNull() ?: subject.totalClasses, subject.presentClasses, subject.eventAttendance)
                }
                AttendanceInput(
                    label = "Attended", 
                    value = subject.presentClasses.toString(),
                    modifier = Modifier.weight(1f)
                ) { 
                    viewModel.updateSubjectAttendance(subject.id, subject.totalClasses, it.toIntOrNull() ?: subject.presentClasses, subject.eventAttendance)
                }
                AttendanceInput(
                    label = "Event", 
                    value = subject.eventAttendance.toString(),
                    modifier = Modifier.weight(1f)
                ) { 
                    viewModel.updateSubjectAttendance(subject.id, subject.totalClasses, subject.presentClasses, it.toIntOrNull() ?: subject.eventAttendance)
                }
            }
        }
    }
}

@Composable
fun AttendanceInput(label: String, value: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit) {
    Column(modifier = modifier) {
        Text(label, fontSize = 11.sp, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.bodySmall,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF8F9FA),
                focusedContainerColor = Color(0xFFF8F9FA),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            )
        )
    }
}

@Composable
fun OutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    shape: androidx.compose.ui.graphics.Shape = OutlinedTextFieldDefaults.shape,
    singleLine: Boolean = false,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        shape = shape,
        singleLine = singleLine,
        colors = colors
    )
}