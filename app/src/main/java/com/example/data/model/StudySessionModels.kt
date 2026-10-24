package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SessionMode(val label: String) {
    POMODORO("Pomodoro 25/5"),
    CUSTOM("Custom")
}

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey
    val id: String,
    val subjectName: String,
    val taskTitle: String? = null,
    val startedAt: Long,
    val endedAt: Long,
    val focusSeconds: Long,
    val sessionMode: SessionMode = SessionMode.POMODORO,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
