package com.example.leavewise.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class Subject(
    val id: String = java.util.UUID.randomUUID().toString(),
    var name: String,
    var presentClasses: Int = 0,
    var totalClasses: Int = 0,
    var plannedLeaves: Int = 0,
    var eventAttendance: Int = 0
) {
    val actualPercentage: Float
        get() = if (totalClasses == 0) 0f else (presentClasses.toFloat() / totalClasses.toFloat()) * 100f

    val withEventPercentage: Float
        get() = if (totalClasses == 0) 0f else ((presentClasses + eventAttendance).toFloat() / totalClasses.toFloat()) * 100f

    val plannedPercentage: Float
        get() {
            val futureTotal = totalClasses + plannedLeaves
            return if (futureTotal == 0) 0f else (presentClasses.toFloat() / futureTotal.toFloat()) * 100f
        }
}

data class TimetableEntry(
    val id: String = java.util.UUID.randomUUID().toString(),
    val day: String,
    val subjectId: String
)

class AttendanceViewModel : ViewModel() {
    var semesterStart = mutableStateOf("")
    var semesterEnd = mutableStateOf("")
    var minAttendance = mutableStateOf(75f)
    
    val subjects = mutableStateListOf<Subject>()
    val timetable = mutableStateListOf<TimetableEntry>()

    fun addSubject(name: String) {
        if (name.isNotBlank() && !subjects.any { it.name.equals(name, ignoreCase = true) }) {
            subjects.add(Subject(name = name))
        }
    }

    fun updateSubjectAttendance(subjectId: String, total: Int, attended: Int, event: Int) {
        val index = subjects.indexOfFirst { it.id == subjectId }
        if (index != -1) {
            subjects[index] = subjects[index].copy(
                totalClasses = total,
                presentClasses = attended,
                eventAttendance = event
            )
        }
    }

    fun renameSubject(subjectId: String, newName: String) {
        val index = subjects.indexOfFirst { it.id == subjectId }
        if (index != -1 && newName.isNotBlank()) {
            subjects[index] = subjects[index].copy(name = newName)
        }
    }

    fun deleteSubject(subjectId: String) {
        subjects.removeAll { it.id == subjectId }
        timetable.removeAll { it.subjectId == subjectId }
    }

    fun addTimetableEntry(day: String, subjectId: String) {
        timetable.add(TimetableEntry(day = day, subjectId = subjectId))
    }

    fun removeTimetableEntry(entryId: String) {
        timetable.removeAll { it.id == entryId }
    }

    fun markAttendance(subjectId: String, isPresent: Boolean) {
        val index = subjects.indexOfFirst { it.id == subjectId }
        if (index != -1) {
            val subject = subjects[index]
            subjects[index] = subject.copy(
                presentClasses = if (isPresent) subject.presentClasses + 1 else subject.presentClasses,
                totalClasses = subject.totalClasses + 1
            )
        }
    }

    fun planLeave(subjectId: String) {
        val index = subjects.indexOfFirst { it.id == subjectId }
        if (index != -1) {
            val subject = subjects[index]
            subjects[index] = subject.copy(
                plannedLeaves = subject.plannedLeaves + 1
            )
        }
    }

    val overallActualPercentage: Float
        get() {
            val total = subjects.sumOf { it.totalClasses }
            val present = subjects.sumOf { it.presentClasses }
            return if (total == 0) 0f else (present.toFloat() / total.toFloat()) * 100f
        }

    val overallWithEventPercentage: Float
        get() {
            val total = subjects.sumOf { it.totalClasses }
            val presentWithEvent = subjects.sumOf { it.presentClasses + it.eventAttendance }
            return if (total == 0) 0f else (presentWithEvent.toFloat() / total.toFloat()) * 100f
        }

    val overallPlannedPercentage: Float
        get() {
            val total = subjects.sumOf { it.totalClasses + it.plannedLeaves }
            val present = subjects.sumOf { it.presentClasses }
            return if (total == 0) 0f else (present.toFloat() / total.toFloat()) * 100f
        }
        
    val totalClasses: Int get() = subjects.sumOf { it.totalClasses }
    val totalPresent: Int get() = subjects.sumOf { it.presentClasses }
    val totalEvent: Int get() = subjects.sumOf { it.eventAttendance }
}