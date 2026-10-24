package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TaskPriority(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High")
}

@Entity(tableName = "study_tasks")
data class StudyTaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val subjectName: String,
    val chapterName: String = "",
    val dueDate: Long, // timestamp for selected day at 00:00:00
    val dueTimeString: String = "10:00 AM",
    val estimatedMinutes: Int = 60,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val notes: String = "",
    val reminderEnabled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
