package com.example.leavewise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.leavewise.ui.AttendanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualAttendanceScreen(
    viewModel: AttendanceViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mark Actual Attendance") },
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
                    "Select a subject to mark your attendance for today's classes.",
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
                                "${subject.actualPercentage.toInt()}%",
                                fontWeight = FontWeight.Bold,
                                color = if (subject.actualPercentage < viewModel.minAttendance.value) Color.Red else Color(0xFF4CAF50)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Total: ${subject.presentClasses}/${subject.totalClasses}", fontSize = 12.sp, color = Color.Gray)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { viewModel.markAttendance(subject.id, true) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Present")
                            }
                            
                            Button(
                                onClick = { viewModel.markAttendance(subject.id, false) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Absent")
                            }
                        }
                    }
                }
            }
        }
    }
}