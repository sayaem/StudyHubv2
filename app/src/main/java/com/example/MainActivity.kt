package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.StudyTaskEntity
import com.example.ui.components.AppHeader
import com.example.ui.components.NavTab
import com.example.ui.components.StudyHubBottomNav
import com.example.ui.components.StudyHubDrawerContent
import com.example.ui.screens.AddTaskScreen
import com.example.ui.screens.ChapterDetailScreen
import com.example.ui.screens.CreateNoteScreen
import com.example.ui.screens.FocusTimerScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LevelSelectionScreen
import com.example.ui.screens.NotesAndFlashcardsScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PerformanceAnalyticsScreen
import com.example.ui.screens.PlanScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProfileSetupScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.SessionLogScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.SyllabusScreen
import com.example.ui.screens.ToolsHubScreen
import com.example.ui.theme.StudyBackground
import com.example.ui.theme.StudyHubTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.StudyViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: StudyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StudyHubTheme {
                StudyHubApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun StudyHubApp(viewModel: StudyViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val allTasks by viewModel.allTasks.collectAsStateWithLifecycle()
    val allSessions by viewModel.allSessions.collectAsStateWithLifecycle()
    val allSubjects by viewModel.allSubjects.collectAsStateWithLifecycle()
    val allChapters by viewModel.allChapters.collectAsStateWithLifecycle()
    val allTopics by viewModel.allTopics.collectAsStateWithLifecycle()
    val allNotes by viewModel.allNotes.collectAsStateWithLifecycle()
    val allFlashcards by viewModel.allFlashcards.collectAsStateWithLifecycle()
    val allReminders by viewModel.allReminders.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    // Timer states
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val remainingSeconds by viewModel.remainingSeconds.collectAsStateWithLifecycle()
    val totalSeconds by viewModel.totalTimerSeconds.collectAsStateWithLifecycle()
    val timerMode by viewModel.timerMode.collectAsStateWithLifecycle()
    val timerDuration by viewModel.timerDurationMinutes.collectAsStateWithLifecycle()
    val breakDuration by viewModel.breakDurationMinutes.collectAsStateWithLifecycle()
    val selectedSubject by viewModel.selectedTimerSubject.collectAsStateWithLifecycle()
    val showSaveDialog by viewModel.showSessionSaveDialog.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Handle toast messages
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            viewModel.clearToast()
        }
    }

    // Determine current tab if in main app
    val currentTab = (currentScreen as? Screen.Main)?.tab ?: NavTab.HOME

    // Should show bottom nav
    val showBottomNav = currentScreen is Screen.Main

    // Back handler
    BackHandler(enabled = true) {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else if (!viewModel.navigateBack()) {
            if (currentScreen is Screen.Main && (currentScreen as Screen.Main).tab != NavTab.HOME) {
                viewModel.switchTab(NavTab.HOME)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showBottomNav,
        drawerContent = {
            StudyHubDrawerContent(
                profile = userProfile,
                onNavigateTab = { tab -> viewModel.switchTab(tab) },
                onNavigateAnalytics = { viewModel.navigateTo(Screen.PerformanceAnalytics) },
                onNavigateFocusTimer = { viewModel.navigateTo(Screen.FocusTimer) },
                onNavigateNotes = { viewModel.navigateTo(Screen.NotesFlashcards(0)) },
                onNavigateReminders = { viewModel.navigateTo(Screen.Reminders) },
                onNavigateSettings = { viewModel.navigateTo(Screen.Settings) },
                onCloseDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(StudyBackground),
            bottomBar = {
                if (showBottomNav) {
                    StudyHubBottomNav(
                        currentTab = currentTab,
                        onTabSelected = { tab -> viewModel.switchTab(tab) }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                when (val screen = currentScreen) {
                    is Screen.Splash -> {
                        SplashScreen(
                            onGetStarted = {
                                if (userProfile?.isOnboardingCompleted == true) {
                                    viewModel.replaceTop(Screen.Main(NavTab.HOME))
                                } else {
                                    viewModel.navigateTo(Screen.Onboarding)
                                }
                            }
                        )
                    }

                    is Screen.Onboarding -> {
                        OnboardingScreen(
                            onSkip = { viewModel.navigateTo(Screen.LevelSelect) },
                            onContinue = { viewModel.navigateTo(Screen.LevelSelect) }
                        )
                    }

                    is Screen.LevelSelect -> {
                        var tempLevel by remember { mutableStateOf("HSC") }
                        LevelSelectionScreen(
                            selectedLevel = tempLevel,
                            onSelectLevel = { tempLevel = it },
                            onBack = { viewModel.navigateBack() },
                            onContinue = { viewModel.navigateTo(Screen.ProfileSetup) }
                        )
                    }

                    is Screen.ProfileSetup -> {
                        ProfileSetupScreen(
                            initialLevel = userProfile?.level ?: "HSC",
                            onBack = { viewModel.navigateBack() },
                            onComplete = { name, level, board, group, examName, examDate ->
                                viewModel.completeOnboardingAndSetup(
                                    displayName = name,
                                    level = level,
                                    board = board,
                                    groupName = group,
                                    targetExamName = examName,
                                    targetExamDate = examDate
                                )
                            }
                        )
                    }

                    is Screen.Main -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            AppHeader(
                                onMenuClick = {
                                    coroutineScope.launch { drawerState.open() }
                                },
                                onNotificationClick = {
                                    viewModel.navigateTo(Screen.Reminders)
                                },
                                notificationCount = allReminders.count { it.isEnabled }
                            )

                            when (screen.tab) {
                                NavTab.HOME -> {
                                    HomeScreen(
                                        profile = userProfile,
                                        tasks = allTasks,
                                        sessions = allSessions,
                                        topics = allTopics,
                                        onToggleTaskComplete = { id, comp -> viewModel.toggleTaskComplete(id, comp) },
                                        onNavigateTab = { tab -> viewModel.switchTab(tab) },
                                        onAddTask = { viewModel.navigateTo(Screen.AddTask(selectedDate)) },
                                        onStartFocus = { viewModel.navigateTo(Screen.FocusTimer) },
                                        onOpenTask = { task -> viewModel.navigateTo(Screen.EditTask(task.id)) }
                                    )
                                }

                                NavTab.PLAN -> {
                                    PlanScreen(
                                        selectedDate = selectedDate,
                                        tasks = allTasks,
                                        onSelectDate = { date -> viewModel.selectDate(date) },
                                        onChangeDay = { offset -> viewModel.changeDay(offset) },
                                        onToggleTaskComplete = { id, comp -> viewModel.toggleTaskComplete(id, comp) },
                                        onAddTask = { viewModel.navigateTo(Screen.AddTask(selectedDate)) },
                                        onEditTask = { task -> viewModel.navigateTo(Screen.EditTask(task.id)) },
                                        onDeleteTask = { id -> viewModel.deleteTask(id) }
                                    )
                                }

                                NavTab.TRACK -> {
                                    SyllabusScreen(
                                        currentLevel = userProfile?.level ?: "HSC",
                                        subjects = allSubjects,
                                        chapters = allChapters,
                                        onOpenChapter = { chId, subId, subName ->
                                            viewModel.navigateTo(Screen.ChapterDetail(chId, subId, subName))
                                        },
                                        onAddCustomSubject = { name, level, color ->
                                            viewModel.addCustomSubject(name, level, color)
                                        }
                                    )
                                }

                                NavTab.TOOLS -> {
                                    ToolsHubScreen(
                                        onOpenFocusTimer = { viewModel.navigateTo(Screen.FocusTimer) },
                                        onOpenSessionLog = { viewModel.navigateTo(Screen.SessionLog) },
                                        onOpenNotes = { viewModel.navigateTo(Screen.NotesFlashcards(0)) },
                                        onOpenFlashcards = { viewModel.navigateTo(Screen.NotesFlashcards(1)) },
                                        onOpenReminders = { viewModel.navigateTo(Screen.Reminders) },
                                        onOpenAnalytics = { viewModel.navigateTo(Screen.PerformanceAnalytics) }
                                    )
                                }

                                NavTab.PROFILE -> {
                                    ProfileScreen(
                                        profile = userProfile,
                                        onEditProfile = { viewModel.navigateTo(Screen.EditProfile) },
                                        onOpenSettings = { viewModel.navigateTo(Screen.Settings) },
                                        onOpenAnalytics = { viewModel.navigateTo(Screen.PerformanceAnalytics) }
                                    )
                                }
                            }
                        }
                    }

                    is Screen.ChapterDetail -> {
                        val ch = allChapters.firstOrNull { it.id == screen.chapterId }
                        val chTopics = allTopics.filter { it.chapterId == screen.chapterId }
                        ChapterDetailScreen(
                            chapter = ch,
                            subjectName = screen.subjectName,
                            topics = chTopics,
                            onToggleTopic = { id, comp ->
                                viewModel.toggleTopicComplete(id, comp, screen.chapterId)
                            },
                            onToggleWeak = { id, weak ->
                                viewModel.toggleTopicWeak(id, weak)
                            },
                            onMarkChapterComplete = {
                                viewModel.markChapterComplete(screen.chapterId)
                            },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    is Screen.AddTask -> {
                        AddTaskScreen(
                            initialDate = screen.initialDate,
                            subjects = allSubjects,
                            chapters = allChapters,
                            onBack = { viewModel.navigateBack() },
                            onSave = { title, sub, ch, due, time, est, priority, notes ->
                                viewModel.addTask(title, sub, ch, due, time, est, priority, notes)
                            }
                        )
                    }

                    is Screen.EditTask -> {
                        val taskToEdit = allTasks.firstOrNull { it.id == screen.taskId }
                        AddTaskScreen(
                            initialDate = taskToEdit?.dueDate ?: selectedDate,
                            subjects = allSubjects,
                            chapters = allChapters,
                            existingTask = taskToEdit,
                            onBack = { viewModel.navigateBack() },
                            onSave = { title, sub, ch, due, time, est, priority, notes ->
                                if (taskToEdit != null) {
                                    viewModel.updateTask(
                                        taskToEdit.copy(
                                            title = title,
                                            subjectName = sub,
                                            chapterName = ch,
                                            dueDate = due,
                                            dueTimeString = time,
                                            estimatedMinutes = est,
                                            priority = priority,
                                            notes = notes
                                        )
                                    )
                                }
                            }
                        )
                    }

                    is Screen.FocusTimer -> {
                        FocusTimerScreen(
                            timerState = timerState,
                            remainingSeconds = remainingSeconds,
                            totalSeconds = totalSeconds,
                            mode = timerMode,
                            durationMinutes = timerDuration,
                            breakMinutes = breakDuration,
                            selectedSubject = selectedSubject,
                            availableSubjects = allSubjects.map { it.name }.distinct().ifEmpty { listOf("Physics", "Chemistry", "Mathematics", "Biology") },
                            showSaveDialog = showSaveDialog,
                            onSetMode = { m -> viewModel.setTimerMode(m) },
                            onSetDuration = { mins -> viewModel.setTimerDuration(mins) },
                            onSetBreak = { mins -> viewModel.setBreakDuration(mins) },
                            onSetSubject = { sub -> viewModel.setTimerSubject(sub) },
                            onStartTimer = { viewModel.startTimer() },
                            onPauseTimer = { viewModel.pauseTimer() },
                            onResumeTimer = { viewModel.resumeTimer() },
                            onResetTimer = { viewModel.resetTimer() },
                            onEndTimer = { viewModel.endTimerPrematurely() },
                            onSaveSession = { notes -> viewModel.saveCompletedSession(notes) },
                            onDiscardSession = { viewModel.discardCompletedSession() },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    is Screen.SessionLog -> {
                        SessionLogScreen(
                            sessions = allSessions,
                            onDeleteSession = { id -> viewModel.deleteSession(id) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    is Screen.PerformanceAnalytics -> {
                        PerformanceAnalyticsScreen(
                            tasks = allTasks,
                            sessions = allSessions,
                            subjects = allSubjects,
                            chapters = allChapters,
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    is Screen.NotesFlashcards -> {
                        NotesAndFlashcardsScreen(
                            initialTab = screen.initialTab,
                            notes = allNotes,
                            flashcards = allFlashcards,
                            onAddNote = { viewModel.navigateTo(Screen.CreateNote) },
                            onDeleteNote = { id -> viewModel.deleteNote(id) },
                            onUpdateFlashcard = { id, stat -> viewModel.updateFlashcard(id, stat) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    is Screen.CreateNote -> {
                        CreateNoteScreen(
                            onSaveNote = { title, content, sub, ch ->
                                viewModel.addNote(title, content, sub, ch)
                            },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    is Screen.Reminders -> {
                        RemindersScreen(
                            reminders = allReminders,
                            onToggleReminder = { id, en -> viewModel.toggleReminder(id, en) },
                            onAddReminder = { title, sub, time, type ->
                                viewModel.addReminder(title, sub, time, type)
                            },
                            onDeleteReminder = { id -> viewModel.deleteReminder(id) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    is Screen.Settings -> {
                        SettingsScreen(
                            profile = userProfile,
                            onBack = { viewModel.navigateBack() },
                            onResetData = {
                                userProfile?.let {
                                    viewModel.updateProfile(
                                        it.copy(
                                            displayName = "Sayem Arafat",
                                            level = "HSC",
                                            board = "Dhaka",
                                            groupName = "Science"
                                        )
                                    )
                                }
                            }
                        )
                    }

                    is Screen.EditProfile -> {
                        ProfileSetupScreen(
                            initialLevel = userProfile?.level ?: "HSC",
                            onBack = { viewModel.navigateBack() },
                            onComplete = { name, level, board, group, examName, examDate ->
                                userProfile?.let {
                                    viewModel.updateProfile(
                                        it.copy(
                                            displayName = name,
                                            level = level,
                                            board = board,
                                            groupName = group,
                                            targetExamName = examName,
                                            targetExamDate = examDate
                                        )
                                    )
                                }
                                viewModel.navigateBack()
                            }
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}
