package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: String = "default_user",
    val displayName: String = "",
    val level: String = "HSC", // SSC, HSC, ADMISSION
    val admissionCategory: String = "Engineering", // Medical, Engineering, General, Other
    val board: String = "Dhaka",
    val groupName: String = "Science", // Science, Commerce, Humanities
    val targetExamName: String = "HSC 2026",
    val targetExamDate: Long = System.currentTimeMillis() + (238L * 24 * 60 * 60 * 1000), // ~238 days left
    val dailyGoalMinutes: Int = 180, // Default 3 hours
    val preferredFocusMinutes: Int = 25,
    val preferredBreakMinutes: Int = 5,
    val mainGoal: String = "Exam Preparation", // Exam Preparation, Syllabus Completion, Topic Revision, Consistency
    val preferredReminderTime: String = "08:00 AM",
    val studyRemindersEnabled: Boolean = true,
    val dailyGoalRemindersEnabled: Boolean = true,
    val examCountdownRemindersEnabled: Boolean = true,
    val avatarId: Int = 1, // 1 to 4 avatar styles
    val themeMode: String = "SYSTEM", // SYSTEM, LIGHT, DARK
    val isOnboardingCompleted: Boolean = false,
    val streakDays: Int = 0,
    val lastActiveDate: Long = System.currentTimeMillis(),
    val totalStudyMinutes: Int = 0
)
