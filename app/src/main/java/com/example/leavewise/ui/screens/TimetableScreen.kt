package com.example.leavewise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
fun TimetableScreen(
    viewModel: AttendanceViewModel,
    onBack: () -> Unit
) {
    var showAddSubjectDialog by remember { mutableStateOf(false) }
    var showAddSlotDialog by remember { mutableStateOf(false) }
    var showEditSubjectDialog by remember { mutableStateOf<com.example.leavewise.ui.Subject?>(null) }
    
    var newSubjectName by remember { mutableStateOf("") }
    var selectedDaySlot by remember { mutableStateOf("Monday") }
    var selectedSubjectIdForSlot by remember { mutableStateOf("") }

    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    // Dialog for Adding a New Subject
    if (showAddSubjectDialog) {
        AlertDialog(
            onDismissRequest = { showAddSubjectDialog = false },
            title = { Text("Add New Subject") },
            text = {
                OutlinedTextField(
                    value = newSubjectName,
                    onValueChange = { newSubjectName = it },
                    label = { Text("Subject Name") },
                    placeholder = { Text("e.g. Data Structures") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (newSubjectName.isNotBlank()) {
                        viewModel.addSubject(newSubjectName)
                        newSubjectName = ""
                        showAddSubjectDialog = false
                    }
                }) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showAddSubjectDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Dialog for Editing/Deleting a Subject
    showEditSubjectDialog?.let { subject ->
        var editName by remember { mutableStateOf(subject.name) }
        AlertDialog(
            onDismissRequest = { showEditSubjectDialog = null },
            title = { Text("Edit Subject") },
            text = {
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text("Subject Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Row {
                    TextButton(onClick = {
                        viewModel.deleteSubject(subject.id)
                        showEditSubjectDialog = null
                    }) { Text("Delete", color = Color.Red) }
                    
                    Button(onClick = {
                        viewModel.renameSubject(subject.id, editName)
                        showEditSubjectDialog = null
                    }) { Text("Save") }
                }
            }
        )
    }

    // Dialog for Adding a Timetable Slot
    if (showAddSlotDialog) {
        AlertDialog(
            onDismissRequest = { showAddSlotDialog = false },
            title = { Text("Add Timetable Slot") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Select Day", fontSize = 12.sp)
                    ScrollableTabRow(
                        selectedTabIndex = days.indexOf(selectedDaySlot),
                        edgePadding = 0.dp,
                        containerColor = Color.Transparent
                    ) {
                        days.forEach { day ->
                            Tab(selected = selectedDaySlot == day, onClick = { selectedDaySlot = day }) {
                                Text(day, modifier = Modifier.padding(8.dp), fontSize = 12.sp)
                            }
                        }
                    }
                    
                    Text("Select Subject", fontSize = 12.sp)
                    if (viewModel.subjects.isEmpty()) {
                        Text("Add subjects first!", color = Color.Red, fontSize = 12.sp)
                    } else {
                        var expanded by remember { mutableStateOf(false) }
                        val currentSubject = viewModel.subjects.find { it.id == selectedSubjectIdForSlot }
                        
                        Box {
                            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                                Text(currentSubject?.name ?: "Choose Subject")
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                viewModel.subjects.forEach { sub ->
                                    DropdownMenuItem(
                                        text = { Text(sub.name) },
                                        onClick = {
                                            selectedSubjectIdForSlot = sub.id
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedSubjectIdForSlot.isNotEmpty()) {
                            viewModel.addTimetableEntry(selectedDaySlot, selectedSubjectIdForSlot)
                            showAddSlotDialog = false
                        }
                    },
                    enabled = selectedSubjectIdForSlot.isNotEmpty()
                ) { Text("Add Slot") }
            },
            dismissButton = {
                TextButton(onClick = { showAddSlotDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Timetable & Subjects") },
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
            // Manage Subjects Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("My Subjects", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Button(
                        onClick = { showAddSubjectDialog = true },
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                        Text(" Add Subject", fontSize = 12.sp)
                    }
                }
            }
            
            if (viewModel.subjects.isEmpty()) {
                item {
                    Text("No subjects added yet. Start by adding some!", fontSize = 12.sp, color = Color.Gray)
                }
            } else {
                items(viewModel.subjects) { subject ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(subject.name, fontWeight = FontWeight.Medium)
                            IconButton(onClick = { showEditSubjectDialog = subject }) {
                                Icon(Icons.Default.Edit, null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            // Timetable Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Weekly Timetable", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Button(
                        onClick = { showAddSlotDialog = true },
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                        Text(" Add Slot", fontSize = 12.sp)
                    }
                }
            }

            days.forEach { day ->
                val dayEntries = viewModel.timetable.filter { it.day == day }
                if (dayEntries.isNotEmpty()) {
                    item {
                        Text(day, fontWeight = FontWeight.SemiBold, color = Color(0xFF4A6CF7), modifier = Modifier.padding(top = 8.dp))
                    }
                    items(dayEntries) { entry ->
                        val sub = viewModel.subjects.find { it.id == entry.subjectId }
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(sub?.name ?: "Unknown Subject", fontSize = 14.sp)
                                IconButton(onClick = { viewModel.removeTimetableEntry(entry.id) }) {
                                    Icon(Icons.Default.Delete, null, tint = Color.Red, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}