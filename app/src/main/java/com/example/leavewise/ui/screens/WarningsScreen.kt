package com.example.leavewise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
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
fun WarningsScreen(
    viewModel: AttendanceViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Warnings & Notifications") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val minTarget = viewModel.minAttendance.value
            
            items(viewModel.subjects) { subject ->
                val status = when {
                    subject.actualPercentage < minTarget -> "Shortage"
                    subject.actualPercentage < minTarget + 5 -> "Risk"
                    else -> "Safe"
                }
                
                val cardColor = when(status) {
                    "Shortage" -> Color(0xFFFFEBEE)
                    "Risk" -> Color(0xFFFFF3E0)
                    else -> Color(0xFFE8F5E9)
                }
                
                val icon = when(status) {
                    "Shortage" -> Icons.Default.Error
                    "Risk" -> Icons.Default.Warning
                    else -> Icons.Default.CheckCircle
                }
                
                val tint = when(status) {
                    "Shortage" -> Color.Red
                    "Risk" -> Color(0xFFEF6C00)
                    else -> Color(0xFF2E7D32)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(icon, null, tint = tint)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(subject.name, fontWeight = FontWeight.Bold)
                            Text(
                                when(status) {
                                    "Shortage" -> "Critical: Your attendance is ${subject.actualPercentage.toInt()}%"
                                    "Risk" -> "Warning: Close to threshold at ${subject.actualPercentage.toInt()}%"
                                    else -> "Safe: Your attendance is ${subject.actualPercentage.toInt()}%"
                                },
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}