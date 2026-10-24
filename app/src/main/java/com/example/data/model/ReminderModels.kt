package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ReminderType(val label: String) {
    ALL("All"),
    STUDY("Study"),
    REVISION("Revision"),
    EXAM("Exam")
}

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val subtitle: String,
    val scheduledTimeFormatted: String,
    val scheduledTimestamp: Long,
    val reminderType: ReminderType,
    val isEnabled: Boolean = true,
    val relatedEntityId: String? = null
)
