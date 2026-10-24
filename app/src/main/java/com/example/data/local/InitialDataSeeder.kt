package com.example.data.local

import com.example.data.model.ChapterEntity
import com.example.data.model.ChapterStatus
import com.example.data.model.FlashcardEntity
import com.example.data.model.FlashcardStatus
import com.example.data.model.NoteEntity
import com.example.data.model.ReminderEntity
import com.example.data.model.ReminderType
import com.example.data.model.StudySessionEntity
import com.example.data.model.StudyTaskEntity
import com.example.data.model.SubjectEntity
import com.example.data.model.TopicEntity
import com.example.data.model.UserProfile
import java.util.Calendar

object InitialDataSeeder {

    suspend fun seedIfNeeded(dao: StudyHubDao) {
        val existing = dao.getUserProfile()
        if (existing == null) {
            // Seed Profile
            val targetExamCalendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 238)
            }
            dao.insertOrUpdateProfile(
                UserProfile(
                    id = "default_user",
                    displayName = "Sayem Arafat",
                    level = "HSC",
                    board = "Dhaka",
                    groupName = "Science",
                    targetExamName = "HSC 2026",
                    targetExamDate = targetExamCalendar.timeInMillis,
                    dailyGoalMinutes = 300,
                    preferredReminderTime = "08:00 AM",
                    isOnboardingCompleted = true, // ready for direct exploration
                    streakDays = 7,
                    lastActiveDate = System.currentTimeMillis(),
                    totalStudyMinutes = 2550
                )
            )

            // Seed Subjects
            val subjects = listOf(
                // HSC Science
                SubjectEntity("hsc_physics", "Physics", "HSC", "Science", "#087F8C", 1),
                SubjectEntity("hsc_chem", "Chemistry", "HSC", "Science", "#E05638", 2),
                SubjectEntity("hsc_math", "Mathematics", "HSC", "Science", "#2563EB", 3),
                SubjectEntity("hsc_bio", "Biology", "HSC", "Science", "#10B981", 4),
                SubjectEntity("hsc_eng", "English", "HSC", "Science", "#8B5CF6", 5),
                SubjectEntity("hsc_ict", "ICT", "HSC", "Science", "#0284C7", 6),

                // SSC
                SubjectEntity("ssc_physics", "Physics", "SSC", "Science", "#087F8C", 1),
                SubjectEntity("ssc_chem", "Chemistry", "SSC", "Science", "#E05638", 2),
                SubjectEntity("ssc_math", "General Math", "SSC", "Science", "#2563EB", 3),
                SubjectEntity("ssc_bio", "Biology", "SSC", "Science", "#10B981", 4),

                // Admission
                SubjectEntity("adm_physics", "Physics (Adv)", "Admission", "Science", "#087F8C", 1),
                SubjectEntity("adm_chem", "Chemistry (Adv)", "Admission", "Science", "#E05638", 2),
                SubjectEntity("adm_math", "Higher Math (Adv)", "Admission", "Science", "#2563EB", 3),
                SubjectEntity("adm_gk", "General Knowledge", "Admission", "All", "#D97706", 4)
            )
            dao.insertSubjects(subjects)

            // Seed HSC Physics Chapters (matching mockup)
            val physicsChapters = listOf(
                ChapterEntity("phys_ch1", "hsc_physics", "Chapter 1 - Units and Measurement", 1, ChapterStatus.COMPLETED, sortOrder = 1),
                ChapterEntity("phys_ch2", "hsc_physics", "Chapter 2 - Motion in a Straight Line", 2, ChapterStatus.COMPLETED, sortOrder = 2),
                ChapterEntity("phys_ch3", "hsc_physics", "Chapter 3 - Motion in a Plane", 3, ChapterStatus.IN_PROGRESS, sortOrder = 3),
                ChapterEntity("phys_ch4", "hsc_physics", "Chapter 4 - Laws of Motion", 4, ChapterStatus.NOT_STARTED, sortOrder = 4),
                ChapterEntity("phys_ch5", "hsc_physics", "Chapter 5 - Work, Energy and Power", 5, ChapterStatus.NOT_STARTED, sortOrder = 5),
                ChapterEntity("phys_ch6", "hsc_physics", "Chapter 6 - Gravitation", 6, ChapterStatus.NOT_STARTED, sortOrder = 6)
            )
            dao.insertChapters(physicsChapters)

            // Chemistry Chapters
            val chemChapters = listOf(
                ChapterEntity("chem_ch1", "hsc_chem", "Chapter 1 - Safe Laboratory Practice", 1, ChapterStatus.COMPLETED, sortOrder = 1),
                ChapterEntity("chem_ch2", "hsc_chem", "Chapter 2 - Qualitative Chemistry", 2, ChapterStatus.IN_PROGRESS, sortOrder = 2),
                ChapterEntity("chem_ch3", "hsc_chem", "Chapter 3 - Periodic Properties", 3, ChapterStatus.NOT_STARTED, sortOrder = 3),
                ChapterEntity("chem_ch4", "hsc_chem", "Chapter 4 - Chemical Changes", 4, ChapterStatus.NOT_STARTED, sortOrder = 4)
            )
            dao.insertChapters(chemChapters)

            // Math Chapters
            val mathChapters = listOf(
                ChapterEntity("math_ch1", "hsc_math", "Chapter 1 - Matrices and Determinants", 1, ChapterStatus.COMPLETED, sortOrder = 1),
                ChapterEntity("math_ch2", "hsc_math", "Chapter 2 - Vectors", 2, ChapterStatus.IN_PROGRESS, sortOrder = 2),
                ChapterEntity("math_ch3", "hsc_math", "Chapter 3 - Straight Lines", 3, ChapterStatus.NOT_STARTED, sortOrder = 3),
                ChapterEntity("math_ch4", "hsc_math", "Chapter 4 - Circles", 4, ChapterStatus.NOT_STARTED, sortOrder = 4)
            )
            dao.insertChapters(mathChapters)

            // Biology Chapters
            val bioChapters = listOf(
                ChapterEntity("bio_ch1", "hsc_bio", "Chapter 1 - Cell and its Structure", 1, ChapterStatus.COMPLETED, sortOrder = 1),
                ChapterEntity("bio_ch2", "hsc_bio", "Chapter 2 - Cell Division", 2, ChapterStatus.NOT_STARTED, sortOrder = 2),
                ChapterEntity("bio_ch3", "hsc_bio", "Chapter 3 - Cell Chemistry", 3, ChapterStatus.NOT_STARTED, sortOrder = 3)
            )
            dao.insertChapters(bioChapters)

            // English Chapters
            val engChapters = listOf(
                ChapterEntity("eng_ch1", "hsc_eng", "Chapter 1 - Reading Comprehension", 1, ChapterStatus.COMPLETED, sortOrder = 1),
                ChapterEntity("eng_ch2", "hsc_eng", "Chapter 2 - Modifiers & Connectors", 2, ChapterStatus.IN_PROGRESS, sortOrder = 2)
            )
            dao.insertChapters(engChapters)

            // Seed Topics for Chapter 3 - Motion in a Plane (exact topics from mockup!)
            val physCh3Topics = listOf(
                TopicEntity("topic_3_1", "phys_ch3", "3.1 Projectile Motion", isCompleted = true, isWeak = false, sortOrder = 1),
                TopicEntity("topic_3_2", "phys_ch3", "3.2 Relative Velocity", isCompleted = true, isWeak = false, sortOrder = 2),
                TopicEntity("topic_3_3", "phys_ch3", "3.3 Numerical Practice", isCompleted = false, isWeak = true, sortOrder = 3),
                TopicEntity("topic_3_4", "phys_ch3", "3.4 Revision & Previous Board Questions", isCompleted = false, isWeak = false, sortOrder = 4)
            )
            dao.insertTopics(physCh3Topics)

            // Today's Date
            val todayCal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val todayDate = todayCal.timeInMillis

            // Seed Tasks (matching mockup!)
            val tasks = listOf(
                StudyTaskEntity(
                    id = "task_1",
                    title = "Physics - Chapter 4 (Laws of Motion)",
                    subjectName = "Physics",
                    chapterName = "Chapter 4 - Laws of Motion",
                    dueDate = todayDate,
                    dueTimeString = "10:00 AM",
                    estimatedMinutes = 120,
                    priority = com.example.data.model.TaskPriority.HIGH,
                    isCompleted = true,
                    completedAt = System.currentTimeMillis() - 7200000,
                    notes = "Solve Newton's laws problems from previous board papers"
                ),
                StudyTaskEntity(
                    id = "task_2",
                    title = "Chemistry - Organic reaction mechanisms",
                    subjectName = "Chemistry",
                    chapterName = "Chapter 2 - Qualitative Chemistry",
                    dueDate = todayDate,
                    dueTimeString = "02:00 PM",
                    estimatedMinutes = 90,
                    priority = com.example.data.model.TaskPriority.MEDIUM,
                    isCompleted = false,
                    notes = "Revise electrophilic substitution reaction"
                ),
                StudyTaskEntity(
                    id = "task_3",
                    title = "Math - Chapter 6 Trigonometry calculus",
                    subjectName = "Mathematics",
                    chapterName = "Chapter 2 - Vectors",
                    dueDate = todayDate,
                    dueTimeString = "05:00 PM",
                    estimatedMinutes = 60,
                    priority = com.example.data.model.TaskPriority.MEDIUM,
                    isCompleted = false,
                    notes = "Practice differentiation identities"
                ),
                StudyTaskEntity(
                    id = "task_4",
                    title = "English - Essay writing practice",
                    subjectName = "English",
                    chapterName = "Chapter 1 - Reading Comprehension",
                    dueDate = todayDate,
                    dueTimeString = "08:00 PM",
                    estimatedMinutes = 45,
                    priority = com.example.data.model.TaskPriority.LOW,
                    isCompleted = false,
                    notes = "Write sample essay on Digital Bangladesh and Smart Citizen"
                )
            )
            for (t in tasks) {
                dao.insertTask(t)
            }

            // Seed Study Sessions (matching today's study time: 3h 15m = 195 minutes = 11700 seconds)
            val sessions = listOf(
                StudySessionEntity(
                    id = "sess_1",
                    subjectName = "Physics",
                    taskTitle = "Physics - Chapter 4 (Laws of Motion)",
                    startedAt = todayDate + (9 * 3600 * 1000),
                    endedAt = todayDate + (11 * 3600 * 1000),
                    focusSeconds = 7200, // 2h 00m
                    notes = "Deep focus session on circular motion formulas"
                ),
                StudySessionEntity(
                    id = "sess_2",
                    subjectName = "Chemistry",
                    taskTitle = "Reaction mechanism notes",
                    startedAt = todayDate + (14 * 3600 * 1000),
                    endedAt = todayDate + (15 * 3600 * 1000),
                    focusSeconds = 3600, // 1h 00m
                    notes = "Qualitative salt analysis memorization"
                ),
                StudySessionEntity(
                    id = "sess_3",
                    subjectName = "Mathematics",
                    taskTitle = "Vector dot & cross products",
                    startedAt = todayDate + (16 * 3600 * 1000),
                    endedAt = todayDate + (16 * 3600 * 1000 + 45 * 60 * 1000),
                    focusSeconds = 2700, // 45m
                    notes = "Unit vector derivations"
                ),
                StudySessionEntity(
                    id = "sess_4",
                    subjectName = "English",
                    taskTitle = "Vocabulary revision",
                    startedAt = todayDate + (18 * 3600 * 1000),
                    endedAt = todayDate + (18 * 3600 * 1000 + 30 * 60 * 1000),
                    focusSeconds = 1800, // 30m
                    notes = "Idioms and phrases"
                )
            )
            for (s in sessions) {
                dao.insertSession(s)
            }

            // Seed Notes
            val notes = listOf(
                NoteEntity(
                    id = "note_1",
                    title = "Motion in a Plane - Formulas",
                    content = "1. Maximum Height: H = (u² sin² θ) / (2g)\n2. Time of Flight: T = (2u sin θ) / g\n3. Horizontal Range: R = (u² sin 2θ) / g\n4. Maximum Range at θ = 45°: R_max = u² / g\n5. Equation of Path: y = x tan θ - (g x²) / (2 u² cos² θ)",
                    subjectName = "Physics",
                    chapterName = "Chapter 3 - Motion in a Plane"
                ),
                NoteEntity(
                    id = "note_2",
                    title = "Organic Reaction Summary - Aldehydes & Ketones",
                    content = "Aldol Condensation requires alpha hydrogen and dilute base. Cannizzaro Reaction occurs with aldehydes lacking alpha hydrogen (e.g., Formaldehyde, Benzaldehyde) in presence of 50% conc. NaOH.",
                    subjectName = "Chemistry",
                    chapterName = "Chapter 2 - Qualitative Chemistry"
                ),
                NoteEntity(
                    id = "note_3",
                    title = "Integration by Parts Formula",
                    content = "∫ u v dx = u ∫ v dx - ∫ [ (du/dx) * ∫ v dx ] dx\nPriority rule: ILATE (Inverse, Logarithmic, Algebraic, Trigonometric, Exponential).",
                    subjectName = "Mathematics",
                    chapterName = "Chapter 2 - Vectors"
                )
            )
            for (n in notes) {
                dao.insertNote(n)
            }

            // Seed Flashcards (matching mockup with flip-card!)
            val flashcards = listOf(
                FlashcardEntity(
                    id = "fc_1",
                    subjectName = "Physics",
                    chapterName = "Chapter 3 - Motion in a Plane",
                    questionFront = "What is the formula for projectile maximum height?",
                    answerBack = "H = (u² sin² θ) / (2g)\n\nWhere u is initial velocity, θ is launch angle, and g is gravitational acceleration (9.8 m/s²).",
                    status = FlashcardStatus.NEW
                ),
                FlashcardEntity(
                    id = "fc_2",
                    subjectName = "Physics",
                    chapterName = "Chapter 3 - Motion in a Plane",
                    questionFront = "What is the condition for maximum horizontal range of a projectile?",
                    answerBack = "Angle θ = 45°.\nMaximum Range R_max = u² / g.",
                    status = FlashcardStatus.KNOW_IT
                ),
                FlashcardEntity(
                    id = "fc_3",
                    subjectName = "Physics",
                    chapterName = "Chapter 3 - Motion in a Plane",
                    questionFront = "What is the formula for the time of flight of a projectile?",
                    answerBack = "T = (2u sin θ) / g\n\nIt is twice the time taken to reach the maximum height.",
                    status = FlashcardStatus.REVIEW_AGAIN
                ),
                FlashcardEntity(
                    id = "fc_4",
                    subjectName = "Chemistry",
                    chapterName = "Chapter 2 - Qualitative Chemistry",
                    questionFront = "Which reagent is used to detect ammonium (NH4+) ion?",
                    answerBack = "Nessler's Reagent (K₂[HgI₄] in alkaline solution).\nProduces a reddish-brown precipitate of iodide of Millon's base.",
                    status = FlashcardStatus.NEW
                ),
                FlashcardEntity(
                    id = "fc_5",
                    subjectName = "Mathematics",
                    chapterName = "Chapter 2 - Vectors",
                    questionFront = "What is the dot product of two perpendicular vectors?",
                    answerBack = "A · B = 0 (since cos 90° = 0).",
                    status = FlashcardStatus.KNOW_IT
                )
            )
            dao.insertFlashcards(flashcards)

            // Seed Reminders (matching mockup!)
            val reminders = listOf(
                ReminderEntity(
                    id = "rem_1",
                    title = "Study Task",
                    subtitle = "Physics - Chapter 4",
                    scheduledTimeFormatted = "Today, 10:00 AM",
                    scheduledTimestamp = todayDate + (10 * 3600 * 1000),
                    reminderType = ReminderType.STUDY,
                    isEnabled = true
                ),
                ReminderEntity(
                    id = "rem_2",
                    title = "Revision",
                    subtitle = "Math - Chapter 3",
                    scheduledTimeFormatted = "Tomorrow, 09:00 AM",
                    scheduledTimestamp = todayDate + (33 * 3600 * 1000),
                    reminderType = ReminderType.REVISION,
                    isEnabled = true
                ),
                ReminderEntity(
                    id = "rem_3",
                    title = "Exam",
                    subtitle = "HSC 2026 Examination",
                    scheduledTimeFormatted = "10 Nov 2026",
                    scheduledTimestamp = targetExamCalendar.timeInMillis,
                    reminderType = ReminderType.EXAM,
                    isEnabled = true
                )
            )
            for (r in reminders) {
                dao.insertReminder(r)
            }
        }
    }
}
