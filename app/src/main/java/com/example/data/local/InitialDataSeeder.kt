package com.example.data.local

import com.example.data.model.ChapterEntity
import com.example.data.model.ChapterStatus
import com.example.data.model.FlashcardEntity
import com.example.data.model.FlashcardStatus
import com.example.data.model.SubjectEntity
import com.example.data.model.TopicEntity
import com.example.data.model.UserProfile
import java.util.Calendar

object InitialDataSeeder {

    suspend fun seedIfNeeded(dao: StudyHubDao) {
        val existing = dao.getUserProfile()
        if (existing == null) {
            val examCal = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 240)
            }
            // Fresh, unconfigured profile with 0 fake stats and onboarding required
            dao.insertOrUpdateProfile(
                UserProfile(
                    id = "default_user",
                    displayName = "",
                    level = "HSC",
                    admissionCategory = "Engineering",
                    board = "Dhaka",
                    groupName = "Science",
                    targetExamName = "HSC 2026",
                    targetExamDate = examCal.timeInMillis,
                    dailyGoalMinutes = 180, // 3 hours
                    preferredFocusMinutes = 25,
                    preferredBreakMinutes = 5,
                    mainGoal = "Exam Preparation",
                    preferredReminderTime = "08:00 AM",
                    studyRemindersEnabled = true,
                    dailyGoalRemindersEnabled = true,
                    examCountdownRemindersEnabled = true,
                    avatarId = 1,
                    themeMode = "SYSTEM",
                    isOnboardingCompleted = false, // Must onboard first!
                    streakDays = 0,
                    lastActiveDate = System.currentTimeMillis(),
                    totalStudyMinutes = 0
                )
            )

            // Seed legitimate academic curriculum subjects (No fake user records)
            val subjects = listOf(
                // HSC Science
                SubjectEntity("hsc_physics", "Physics", "HSC", "Science", "#087F8C", 1),
                SubjectEntity("hsc_chem", "Chemistry", "HSC", "Science", "#EA580C", 2),
                SubjectEntity("hsc_math", "Higher Mathematics", "HSC", "Science", "#2563EB", 3),
                SubjectEntity("hsc_bio", "Biology", "HSC", "Science", "#10B981", 4),
                SubjectEntity("hsc_eng", "English", "HSC", "Science", "#8B5CF6", 5),
                SubjectEntity("hsc_ict", "ICT", "HSC", "Science", "#0284C7", 6),

                // HSC Business Studies (Commerce)
                SubjectEntity("hsc_acc", "Accounting", "HSC", "Commerce", "#087F8C", 1),
                SubjectEntity("hsc_fin", "Finance & Banking", "HSC", "Commerce", "#2563EB", 2),
                SubjectEntity("hsc_mgmt", "Business Organization", "HSC", "Commerce", "#10B981", 3),

                // HSC Humanities (Arts)
                SubjectEntity("hsc_civics", "Civics & Governance", "HSC", "Humanities", "#EA580C", 1),
                SubjectEntity("hsc_econ", "Economics", "HSC", "Humanities", "#2563EB", 2),
                SubjectEntity("hsc_socio", "Sociology", "HSC", "Humanities", "#8B5CF6", 3),

                // SSC Core
                SubjectEntity("ssc_physics", "Physics", "SSC", "Science", "#087F8C", 1),
                SubjectEntity("ssc_chem", "Chemistry", "SSC", "Science", "#EA580C", 2),
                SubjectEntity("ssc_math", "General Math", "SSC", "Science", "#2563EB", 3),
                SubjectEntity("ssc_bio", "Biology", "SSC", "Science", "#10B981", 4),
                SubjectEntity("ssc_bgs", "Bangladesh & Global Studies", "SSC", "Science", "#D97706", 5),

                // Admission
                SubjectEntity("adm_physics", "Physics (Adv)", "Admission", "Engineering", "#087F8C", 1),
                SubjectEntity("adm_math", "Higher Math (Adv)", "Admission", "Engineering", "#2563EB", 2),
                SubjectEntity("adm_chem", "Chemistry (Adv)", "Admission", "Medical", "#EA580C", 3),
                SubjectEntity("adm_bio", "Biology (Adv)", "Admission", "Medical", "#10B981", 4),
                SubjectEntity("adm_gk", "General Knowledge & English", "Admission", "General", "#8B5CF6", 5)
            )
            dao.insertSubjects(subjects)

            // Seed standard chapters for Physics
            val physicsChapters = listOf(
                ChapterEntity("phys_ch1", "hsc_physics", "Chapter 1 - Physical World & Measurement", 1, ChapterStatus.NOT_STARTED, sortOrder = 1),
                ChapterEntity("phys_ch2", "hsc_physics", "Chapter 2 - Vectors", 2, ChapterStatus.NOT_STARTED, sortOrder = 2),
                ChapterEntity("phys_ch3", "hsc_physics", "Chapter 3 - Dynamics & Motion in a Plane", 3, ChapterStatus.NOT_STARTED, sortOrder = 3),
                ChapterEntity("phys_ch4", "hsc_physics", "Chapter 4 - Newtonian Mechanics", 4, ChapterStatus.NOT_STARTED, sortOrder = 4),
                ChapterEntity("phys_ch5", "hsc_physics", "Chapter 5 - Work, Energy & Power", 5, ChapterStatus.NOT_STARTED, sortOrder = 5),
                ChapterEntity("phys_ch6", "hsc_physics", "Chapter 6 - Gravitation", 6, ChapterStatus.NOT_STARTED, sortOrder = 6)
            )
            dao.insertChapters(physicsChapters)

            // Seed Chemistry chapters
            val chemChapters = listOf(
                ChapterEntity("chem_ch1", "hsc_chem", "Chapter 1 - Safe Laboratory Practice", 1, ChapterStatus.NOT_STARTED, sortOrder = 1),
                ChapterEntity("chem_ch2", "hsc_chem", "Chapter 2 - Qualitative Chemistry", 2, ChapterStatus.NOT_STARTED, sortOrder = 2),
                ChapterEntity("chem_ch3", "hsc_chem", "Chapter 3 - Periodic Properties of Elements", 3, ChapterStatus.NOT_STARTED, sortOrder = 3),
                ChapterEntity("chem_ch4", "hsc_chem", "Chapter 4 - Chemical Changes", 4, ChapterStatus.NOT_STARTED, sortOrder = 4)
            )
            dao.insertChapters(chemChapters)

            // Seed Math chapters
            val mathChapters = listOf(
                ChapterEntity("math_ch1", "hsc_math", "Chapter 1 - Matrices & Determinants", 1, ChapterStatus.NOT_STARTED, sortOrder = 1),
                ChapterEntity("math_ch2", "hsc_math", "Chapter 2 - Vectors", 2, ChapterStatus.NOT_STARTED, sortOrder = 2),
                ChapterEntity("math_ch3", "hsc_math", "Chapter 3 - Straight Lines", 3, ChapterStatus.NOT_STARTED, sortOrder = 3),
                ChapterEntity("math_ch4", "hsc_math", "Chapter 4 - Circles", 4, ChapterStatus.NOT_STARTED, sortOrder = 4)
            )
            dao.insertChapters(mathChapters)

            // Seed Biology chapters
            val bioChapters = listOf(
                ChapterEntity("bio_ch1", "hsc_bio", "Chapter 1 - Cell and its Structure", 1, ChapterStatus.NOT_STARTED, sortOrder = 1),
                ChapterEntity("bio_ch2", "hsc_bio", "Chapter 2 - Cell Division", 2, ChapterStatus.NOT_STARTED, sortOrder = 2),
                ChapterEntity("bio_ch3", "hsc_bio", "Chapter 3 - Cell Chemistry", 3, ChapterStatus.NOT_STARTED, sortOrder = 3)
            )
            dao.insertChapters(bioChapters)

            // Seed topics for Chapter 3
            val physCh3Topics = listOf(
                TopicEntity("topic_3_1", "phys_ch3", "3.1 Projectile Motion & Trajectory", isCompleted = false, isWeak = false, sortOrder = 1),
                TopicEntity("topic_3_2", "phys_ch3", "3.2 Relative Velocity in 2D", isCompleted = false, isWeak = false, sortOrder = 2),
                TopicEntity("topic_3_3", "phys_ch3", "3.3 Circular Motion & Centripetal Acceleration", isCompleted = false, isWeak = false, sortOrder = 3),
                TopicEntity("topic_3_4", "phys_ch3", "3.4 Previous Board Exam Numerical Problems", isCompleted = false, isWeak = false, sortOrder = 4)
            )
            dao.insertTopics(physCh3Topics)

            // Starter flashcard templates for formula memorization
            val starterCards = listOf(
                FlashcardEntity(
                    id = "fc_1",
                    subjectName = "Physics",
                    chapterName = "Chapter 3 - Dynamics",
                    questionFront = "What is the formula for projectile maximum height?",
                    answerBack = "H = (u² sin² θ) / (2g)\n\nWhere u is initial velocity, θ is launch angle, and g = 9.8 m/s².",
                    status = FlashcardStatus.NEW
                ),
                FlashcardEntity(
                    id = "fc_2",
                    subjectName = "Physics",
                    chapterName = "Chapter 3 - Dynamics",
                    questionFront = "What is the maximum horizontal range condition?",
                    answerBack = "Launch angle θ = 45°.\nMaximum Range R_max = u² / g.",
                    status = FlashcardStatus.NEW
                ),
                FlashcardEntity(
                    id = "fc_3",
                    subjectName = "Chemistry",
                    chapterName = "Chapter 2 - Qualitative Chemistry",
                    questionFront = "What is Nessler's Reagent used to detect?",
                    answerBack = "Used to detect ammonium (NH₄⁺) ion.\nProduces a reddish-brown precipitate.",
                    status = FlashcardStatus.NEW
                )
            )
            dao.insertFlashcards(starterCards)

            // NO FAKE TASKS, NO FAKE SESSIONS, NO FAKE NOTES!
            // All student data will be authored genuinely by the user.
        }
    }
}
