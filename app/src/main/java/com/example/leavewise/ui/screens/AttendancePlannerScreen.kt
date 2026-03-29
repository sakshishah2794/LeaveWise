package com.example.leavewise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.leavewise.ui.AttendanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendancePlannerScreen(
    viewModel: AttendanceViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance Planner") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF2F3F7))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Simulate your future leaves and see the impact on your attendance.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            items(viewModel.subjects) { subject ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(subject.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(
                                "Planned: ${subject.plannedPercentage.toInt()}%",
                                fontWeight = FontWeight.Bold,
                                color = if (subject.plannedPercentage < viewModel.minAttendance.value) Color.Red else Color(0xFF4A6CF7)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Current: ${subject.presentClasses}/${subject.totalClasses}", fontSize = 12.sp, color = Color.Gray)
                        Text("Planned Leaves: ${subject.plannedLeaves}", fontSize = 12.sp, color = Color.Gray)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { viewModel.planLeave(subject.id) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6CF7)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Add Planned Leave")
                        }
                        
                        if (subject.plannedPercentage < viewModel.minAttendance.value) {
                            val required = calculateRequiredClasses(subject.presentClasses, subject.totalClasses + subject.plannedLeaves, viewModel.minAttendance.value)
                            Text(
                                "Need $required more classes to reach ${viewModel.minAttendance.value.toInt()}%",
                                color = Color.Red,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun calculateRequiredClasses(present: Int, total: Int, target: Float): Int {
    if (total == 0) return 0
    var p = present
    var t = total
    var needed = 0
    while ((p.toFloat() / t.toFloat()) * 100f < target) {
        p++
        t++
        needed++
    }
    return needed
}