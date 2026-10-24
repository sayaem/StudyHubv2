package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.InitialDataSeeder
import com.example.data.local.StudyHubDatabase
import com.example.data.model.AcademicLevel
import com.example.data.model.ChapterEntity
import com.example.data.model.ChapterStatus
import com.example.data.model.FlashcardEntity
import com.example.data.model.FlashcardStatus
import com.example.data.model.NoteEntity
import com.example.data.model.ReminderEntity
import com.example.data.model.ReminderType
import com.example.data.model.SessionMode
import com.example.data.model.StudySessionEntity
import com.example.data.model.StudyTaskEntity
import com.example.data.model.SubjectEntity
import com.example.data.model.TaskPriority
import com.example.data.model.TopicEntity
import com.example.data.model.UserProfile
import com.example.data.repository.StudyRepository
import com.example.ui.components.NavTab
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

sealed class Screen {
    object Splash : Screen()
    object Onboarding : Screen()
    object LevelSelect : Screen()
    object ProfileSetup : Screen()
    data class Main(val tab: NavTab = NavTab.HOME) : Screen()
    data class ChapterDetail(val chapterId: String, val subjectId: String, val subjectName: String) : Screen()
    data class AddTask(val initialDate: Long) : Screen()
    data class EditTask(val taskId: String) : Screen()
    object FocusTimer : Screen()
    object SessionLog : Screen()
    object PerformanceAnalytics : Screen()
    data class NotesFlashcards(val initialTab: Int = 0) : Screen()
    object CreateNote : Screen()
    data class FlashcardsStudy(val subjectName: String) : Screen()
    object Reminders : Screen()
    object Settings : Screen()
    object EditProfile : Screen()
}

enum class TimerState {
    IDLE,
    RUNNING,
    PAUSED,
    COMPLETED
}

class StudyViewModel(application: Application) : AndroidViewModel(application) {

    private val db = StudyHubDatabase.getDatabase(application)
    private val repository = StudyRepository(db.studyHubDao())

    // Navigation back stack
    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Splash))
    val currentScreen: StateFlow<Screen> = MutableStateFlow<Screen>(Screen.Splash).apply {
        viewModelScope.launch {
            _screenStack.collect { stack ->
                value = stack.lastOrNull() ?: Screen.Main(NavTab.HOME)
            }
        }
    }

    // Profile state
    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Planner: Selected Date (defaults to today 00:00:00)
    private val _selectedDate = MutableStateFlow(getStartOfDay(System.currentTimeMillis()))
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    // Tasks
    val allTasks: StateFlow<List<StudyTaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Subjects
    val allSubjects: StateFlow<List<SubjectEntity>> = repository.getAllSubjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Chapters
    val allChapters: StateFlow<List<ChapterEntity>> = repository.getAllChapters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Topics
    val allTopics: StateFlow<List<TopicEntity>> = repository.getAllTopics()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Sessions
    val allSessions: StateFlow<List<StudySessionEntity>> = repository.allSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Notes & Flashcards
    val allNotes: StateFlow<List<NoteEntity>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFlashcards: StateFlow<List<FlashcardEntity>> = repository.allFlashcards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReminders: StateFlow<List<ReminderEntity>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Focus Timer state
    private val _timerMode = MutableStateFlow(SessionMode.POMODORO)
    val timerMode: StateFlow<SessionMode> = _timerMode.asStateFlow()

    private val _timerDurationMinutes = MutableStateFlow(25)
    val timerDurationMinutes: StateFlow<Int> = _timerDurationMinutes.asStateFlow()

    private val _breakDurationMinutes = MutableStateFlow(5)
    val breakDurationMinutes: StateFlow<Int> = _breakDurationMinutes.asStateFlow()

    private val _selectedTimerSubject = MutableStateFlow("Physics")
    val selectedTimerSubject: StateFlow<String> = _selectedTimerSubject.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(25 * 60L)
    val remainingSeconds: StateFlow<Long> = _remainingSeconds.asStateFlow()

    private val _totalTimerSeconds = MutableStateFlow(25 * 60L)
    val totalTimerSeconds: StateFlow<Long> = _totalTimerSeconds.asStateFlow()

    private val _showSessionSaveDialog = MutableStateFlow(false)
    val showSessionSaveDialog: StateFlow<Boolean> = _showSessionSaveDialog.asStateFlow()

    private var timerJob: Job? = null
    private var sessionStartTime: Long = 0L
    private var targetEndTime: Long = 0L

    // Notification message / Toast
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        viewModelScope.launch {
            InitialDataSeeder.seedIfNeeded(db.studyHubDao())
        }
    }

    // Navigation methods
    fun navigateTo(screen: Screen) {
        _screenStack.value = _screenStack.value + screen
    }

    fun replaceTop(screen: Screen) {
        val current = _screenStack.value
        _screenStack.value = if (current.isNotEmpty()) current.dropLast(1) + screen else listOf(screen)
    }

    fun switchTab(tab: NavTab) {
        _screenStack.value = listOf(Screen.Main(tab))
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value
        if (current.size > 1) {
            _screenStack.value = current.dropLast(1)
            return true
        }
        return false
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // Date navigation
    fun selectDate(timestamp: Long) {
        _selectedDate.value = getStartOfDay(timestamp)
    }

    fun changeDay(offsetDays: Int) {
        val cal = Calendar.getInstance().apply {
            timeInMillis = _selectedDate.value
            add(Calendar.DAY_OF_YEAR, offsetDays)
        }
        _selectedDate.value = getStartOfDay(cal.timeInMillis)
    }

    // Profile Setup
    fun completeOnboardingAndSetup(
        displayName: String,
        level: String,
        board: String,
        groupName: String,
        targetExamName: String,
        targetExamDate: Long
    ) {
        viewModelScope.launch {
            val updated = UserProfile(
                id = "default_user",
                displayName = displayName.ifBlank { "Sayem Arafat" },
                level = level,
                board = board,
                groupName = groupName,
                targetExamName = targetExamName.ifBlank { "$level 2026" },
                targetExamDate = targetExamDate,
                dailyGoalMinutes = 300,
                preferredReminderTime = "08:00 AM",
                isOnboardingCompleted = true,
                streakDays = 7,
                lastActiveDate = System.currentTimeMillis(),
                totalStudyMinutes = 2550
            )
            repository.saveProfile(updated)
            _screenStack.value = listOf(Screen.Main(NavTab.HOME))
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveProfile(profile)
            showToast("Profile updated successfully")
        }
    }

    // Task Actions
    fun addTask(
        title: String,
        subjectName: String,
        chapterName: String,
        dueDate: Long,
        dueTimeString: String,
        estimatedMinutes: Int,
        priority: TaskPriority,
        notes: String
    ) {
        viewModelScope.launch {
            val newTask = StudyTaskEntity(
                id = "task_${UUID.randomUUID()}",
                title = title.trim(),
                subjectName = subjectName,
                chapterName = chapterName,
                dueDate = getStartOfDay(dueDate),
                dueTimeString = dueTimeString,
                estimatedMinutes = estimatedMinutes,
                priority = priority,
                isCompleted = false,
                notes = notes.trim()
            )
            repository.saveTask(newTask)
            showToast("Task added to your plan")
            navigateBack()
        }
    }

    fun updateTask(task: StudyTaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
            showToast("Task updated")
            navigateBack()
        }
    }

    fun toggleTaskComplete(taskId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskComplete(taskId, isCompleted)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
            showToast("Task removed")
        }
    }

    // Chapter & Topic progress
    fun toggleTopicComplete(topicId: String, isCompleted: Boolean, chapterId: String) {
        viewModelScope.launch {
            repository.updateTopicCompletion(topicId, isCompleted)
            // Re-evaluate chapter status
            val topics = allTopics.value.filter { it.chapterId == chapterId }
            val completedCount = topics.count { if (it.id == topicId) isCompleted else it.isCompleted }
            val newStatus = when {
                completedCount == 0 -> ChapterStatus.NOT_STARTED
                completedCount == topics.size -> ChapterStatus.COMPLETED
                else -> ChapterStatus.IN_PROGRESS
            }
            repository.updateChapterStatus(chapterId, newStatus)
        }
    }

    fun toggleTopicWeak(topicId: String, isWeak: Boolean) {
        viewModelScope.launch {
            repository.updateTopicWeakStatus(topicId, isWeak)
        }
    }

    fun markChapterComplete(chapterId: String) {
        viewModelScope.launch {
            repository.markAllTopicsInChapter(chapterId, true)
            showToast("Chapter marked as completed!")
        }
    }

    fun addCustomSubject(name: String, level: String, colorHex: String) {
        viewModelScope.launch {
            val subject = SubjectEntity(
                id = "sub_${UUID.randomUUID()}",
                name = name.trim(),
                level = level,
                groupName = "Custom",
                colorHex = colorHex,
                sortOrder = 99,
                isCustom = true
            )
            repository.addCustomSubject(subject)
            showToast("Subject '$name' added!")
        }
    }

    // Focus Timer Operations
    fun setTimerMode(mode: SessionMode) {
        if (_timerState.value != TimerState.RUNNING) {
            _timerMode.value = mode
            val minutes = if (mode == SessionMode.POMODORO) 25 else _timerDurationMinutes.value
            _remainingSeconds.value = minutes * 60L
            _totalTimerSeconds.value = minutes * 60L
            _timerState.value = TimerState.IDLE
        }
    }

    fun setTimerDuration(minutes: Int) {
        if (_timerState.value != TimerState.RUNNING) {
            _timerDurationMinutes.value = minutes
            _remainingSeconds.value = minutes * 60L
            _totalTimerSeconds.value = minutes * 60L
        }
    }

    fun setBreakDuration(minutes: Int) {
        _breakDurationMinutes.value = minutes
    }

    fun setTimerSubject(subject: String) {
        _selectedTimerSubject.value = subject
    }

    fun startTimer() {
        if (_timerState.value == TimerState.RUNNING) return
        sessionStartTime = System.currentTimeMillis()
        targetEndTime = sessionStartTime + (_remainingSeconds.value * 1000L)
        _timerState.value = TimerState.RUNNING

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_remainingSeconds.value > 0 && _timerState.value == TimerState.RUNNING) {
                delay(500)
                val now = System.currentTimeMillis()
                val left = ((targetEndTime - now) / 1000L).coerceAtLeast(0L)
                _remainingSeconds.value = left
                if (left <= 0) {
                    _timerState.value = TimerState.COMPLETED
                    _showSessionSaveDialog.value = true
                    break
                }
            }
        }
    }

    fun pauseTimer() {
        if (_timerState.value == TimerState.RUNNING) {
            timerJob?.cancel()
            _timerState.value = TimerState.PAUSED
        }
    }

    fun resumeTimer() {
        if (_timerState.value == TimerState.PAUSED) {
            targetEndTime = System.currentTimeMillis() + (_remainingSeconds.value * 1000L)
            _timerState.value = TimerState.RUNNING
            timerJob?.cancel()
            timerJob = viewModelScope.launch {
                while (_remainingSeconds.value > 0 && _timerState.value == TimerState.RUNNING) {
                    delay(500)
                    val now = System.currentTimeMillis()
                    val left = ((targetEndTime - now) / 1000L).coerceAtLeast(0L)
                    _remainingSeconds.value = left
                    if (left <= 0) {
                        _timerState.value = TimerState.COMPLETED
                        _showSessionSaveDialog.value = true
                        break
                    }
                }
            }
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.IDLE
        val minutes = if (_timerMode.value == SessionMode.POMODORO) 25 else _timerDurationMinutes.value
        _remainingSeconds.value = minutes * 60L
        _totalTimerSeconds.value = minutes * 60L
    }

    fun endTimerPrematurely() {
        timerJob?.cancel()
        val elapsed = _totalTimerSeconds.value - _remainingSeconds.value
        if (elapsed >= 60) { // If at least 1 minute was studied, prompt to save
            _timerState.value = TimerState.COMPLETED
            _showSessionSaveDialog.value = true
        } else {
            resetTimer()
        }
    }

    fun saveCompletedSession(notes: String = "") {
        viewModelScope.launch {
            val focusSecs = (_totalTimerSeconds.value - _remainingSeconds.value).coerceAtLeast(60L)
            val session = StudySessionEntity(
                id = "session_${UUID.randomUUID()}",
                subjectName = _selectedTimerSubject.value,
                startedAt = sessionStartTime,
                endedAt = System.currentTimeMillis(),
                focusSeconds = focusSecs,
                sessionMode = _timerMode.value,
                notes = notes
            )
            repository.saveSession(session)

            // Update user profile total study minutes
            val profile = userProfile.value
            if (profile != null) {
                val addedMinutes = (focusSecs / 60).toInt()
                repository.saveProfile(
                    profile.copy(
                        totalStudyMinutes = profile.totalStudyMinutes + addedMinutes
                    )
                )
            }

            _showSessionSaveDialog.value = false
            resetTimer()
            showToast("Focus session saved! +${focusSecs / 60}m recorded.")
        }
    }

    fun discardCompletedSession() {
        _showSessionSaveDialog.value = false
        resetTimer()
    }

    fun deleteSession(sessionId: String) {
        viewModelScope.launch {
            repository.deleteSession(sessionId)
            showToast("Session removed from log")
        }
    }

    // Notes
    fun addNote(title: String, content: String, subjectName: String, chapterName: String) {
        viewModelScope.launch {
            val note = NoteEntity(
                id = "note_${UUID.randomUUID()}",
                title = title.trim(),
                content = content.trim(),
                subjectName = subjectName,
                chapterName = chapterName
            )
            repository.saveNote(note)
            showToast("Note saved")
            navigateBack()
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
            showToast("Note deleted")
        }
    }

    // Flashcards
    fun updateFlashcard(cardId: String, status: FlashcardStatus) {
        viewModelScope.launch {
            repository.updateFlashcardStatus(cardId, status)
        }
    }

    // Reminders
    fun toggleReminder(id: String, enabled: Boolean) {
        viewModelScope.launch {
            repository.toggleReminder(id, enabled)
        }
    }

    fun addReminder(
        title: String,
        subtitle: String,
        timeFormatted: String,
        type: ReminderType
    ) {
        viewModelScope.launch {
            val reminder = ReminderEntity(
                id = "rem_${UUID.randomUUID()}",
                title = title.trim(),
                subtitle = subtitle.trim(),
                scheduledTimeFormatted = timeFormatted,
                scheduledTimestamp = System.currentTimeMillis() + 86400000,
                reminderType = type,
                isEnabled = true
            )
            repository.saveReminder(reminder)
            showToast("Reminder scheduled")
        }
    }

    fun deleteReminder(id: String) {
        viewModelScope.launch {
            repository.deleteReminder(id)
            showToast("Reminder removed")
        }
    }

    companion object {
        fun getStartOfDay(timestamp: Long): Long {
            return Calendar.getInstance().apply {
                timeInMillis = timestamp
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        }
    }
}
