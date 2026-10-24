package com.example

import com.example.data.model.StudySessionEntity
import com.example.data.model.StudyTaskEntity
import com.example.data.model.TopicEntity
import com.example.data.model.UserProfile
import com.example.ui.viewmodel.StudyViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class ExampleUnitTest {

    @Test
    fun testStartOfDayBoundary() {
        val cal = Calendar.getInstance().apply {
            set(2026, Calendar.OCTOBER, 24, 15, 45, 30)
            set(Calendar.MILLISECOND, 500)
        }
        val startOfDay = StudyViewModel.getStartOfDay(cal.timeInMillis)

        val verifyCal = Calendar.getInstance().apply {
            timeInMillis = startOfDay
        }
        assertEquals(0, verifyCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, verifyCal.get(Calendar.MINUTE))
        assertEquals(0, verifyCal.get(Calendar.SECOND))
        assertEquals(0, verifyCal.get(Calendar.MILLISECOND))
        assertEquals(24, verifyCal.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun testSyllabusProgressMath() {
        // Zero items
        val emptyTopics = emptyList<TopicEntity>()
        val zeroProgress = if (emptyTopics.isNotEmpty()) (emptyTopics.count { it.isCompleted } * 100) / emptyTopics.size else 0
        assertEquals(0, zeroProgress)

        // Partial items: 2 of 4 completed
        val topics = listOf(
            TopicEntity("t1", "ch1", "Topic 1", isCompleted = true),
            TopicEntity("t2", "ch1", "Topic 2", isCompleted = true),
            TopicEntity("t3", "ch1", "Topic 3", isCompleted = false),
            TopicEntity("t4", "ch1", "Topic 4", isCompleted = false)
        )
        val partialProgress = (topics.count { it.isCompleted } * 100) / topics.size
        assertEquals(50, partialProgress)

        // Full items
        val allCompleted = topics.map { it.copy(isCompleted = true) }
        val fullProgress = (allCompleted.count { it.isCompleted } * 100) / allCompleted.size
        assertEquals(100, fullProgress)
    }

    @Test
    fun testTaskFilteringByDateAndStatus() {
        val today = StudyViewModel.getStartOfDay(System.currentTimeMillis())
        val tomorrow = today + 86400000L

        val tasks = listOf(
            StudyTaskEntity("1", "Physics Ch 4", "Physics", dueDate = today, isCompleted = false),
            StudyTaskEntity("2", "Chemistry Ch 2", "Chemistry", dueDate = today, isCompleted = true),
            StudyTaskEntity("3", "Math Vectors", "Mathematics", dueDate = tomorrow, isCompleted = false)
        )

        val todayTasks = tasks.filter { it.dueDate in today until tomorrow }
        assertEquals(2, todayTasks.size)

        val todayPending = todayTasks.filter { !it.isCompleted }
        assertEquals(1, todayPending.size)
        assertEquals("Physics Ch 4", todayPending[0].title)

        val todayCompleted = todayTasks.filter { it.isCompleted }
        assertEquals(1, todayCompleted.size)
        assertEquals("Chemistry Ch 2", todayCompleted[0].title)
    }

    @Test
    fun testFocusTimerElapsedCalculation() {
        val totalSeconds = 25 * 60L // 1500
        val remainingSeconds = 15 * 60L // 900
        val elapsedSeconds = totalSeconds - remainingSeconds
        assertEquals(600L, elapsedSeconds)
        assertEquals(10L, elapsedSeconds / 60)
    }

    @Test
    fun testRealStreakCalculationFromSessions() {
        // Zero sessions => 0 streak
        val emptyStreak = StudyViewModel.calculateRealStreak(emptyList())
        assertEquals(0, emptyStreak)

        val todayStart = StudyViewModel.getStartOfDay(System.currentTimeMillis())
        val yesterdayStart = todayStart - 86400000L
        val twoDaysAgoStart = yesterdayStart - 86400000L

        // Today session only => 1 day streak
        val streak1 = StudyViewModel.calculateRealStreak(
            listOf(
                StudySessionEntity(id = "s1", subjectName = "Physics", startedAt = todayStart + 1000, endedAt = todayStart + 2500000, focusSeconds = 1500)
            )
        )
        assertEquals(1, streak1)

        // Today + Yesterday + Two Days Ago => 3 days streak
        val streak3 = StudyViewModel.calculateRealStreak(
            listOf(
                StudySessionEntity(id = "s1", subjectName = "Physics", startedAt = todayStart + 1000, endedAt = todayStart + 2500000, focusSeconds = 1500),
                StudySessionEntity(id = "s2", subjectName = "Chemistry", startedAt = yesterdayStart + 2000, endedAt = yesterdayStart + 2500000, focusSeconds = 1500),
                StudySessionEntity(id = "s3", subjectName = "Math", startedAt = twoDaysAgoStart + 3000, endedAt = twoDaysAgoStart + 2500000, focusSeconds = 1500)
            )
        )
        assertEquals(3, streak3)
    }

    @Test
    fun testInitialProfileDefaults() {
        val profile = UserProfile()
        assertFalse("New profile must not have onboarding completed by default", profile.isOnboardingCompleted)
        assertEquals("New profile must have 0 streak days", 0, profile.streakDays)
        assertEquals("New profile must have 0 total study minutes", 0, profile.totalStudyMinutes)
        assertEquals("Default theme mode must be SYSTEM", "SYSTEM", profile.themeMode)
    }
}
