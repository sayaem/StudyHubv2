package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: String = "default_user",
    val displayName: String = "Sayem Arafat",
    val level: String = "HSC", // SSC, HSC, ADMISSION
    val board: String = "Dhaka",
    val groupName: String = "Science", // Science, Commerce, Humanities
    val targetExamName: String = "HSC 2026",
    val targetExamDate: Long = System.currentTimeMillis() + (238L * 24 * 60 * 60 * 1000), // ~238 days left
    val dailyGoalMinutes: Int = 300, // 5 hours
    val preferredReminderTime: String = "08:00 AM",
    val isOnboardingCompleted: Boolean = false,
    val streakDays: Int = 7,
    val lastActiveDate: Long = System.currentTimeMillis(),
    val totalStudyMinutes: Int = 7245 // ~120h 45m
)
