package com.example.data.repository

import com.example.data.local.StudyHubDao
import com.example.data.model.ChapterEntity
import com.example.data.model.ChapterStatus
import com.example.data.model.FlashcardEntity
import com.example.data.model.FlashcardStatus
import com.example.data.model.NoteEntity
import com.example.data.model.ReminderEntity
import com.example.data.model.StudySessionEntity
import com.example.data.model.StudyTaskEntity
import com.example.data.model.SubjectEntity
import com.example.data.model.TopicEntity
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

class StudyRepository(private val dao: StudyHubDao) {

    val userProfile: Flow<UserProfile?> = dao.getUserProfileFlow()

    suspend fun getProfileSync(): UserProfile? = dao.getUserProfile()

    suspend fun saveProfile(profile: UserProfile) {
        dao.insertOrUpdateProfile(profile)
    }

    fun getSubjectsForLevel(level: String): Flow<List<SubjectEntity>> = dao.getSubjectsForLevelFlow(level)

    fun getAllSubjects(): Flow<List<SubjectEntity>> = dao.getAllSubjectsFlow()

    fun getChaptersForSubject(subjectId: String): Flow<List<ChapterEntity>> = dao.getChaptersForSubjectFlow(subjectId)

    fun getAllChapters(): Flow<List<ChapterEntity>> = dao.getAllChaptersFlow()

    suspend fun updateChapterStatus(chapterId: String, status: ChapterStatus) {
        dao.updateChapterStatus(chapterId, status)
    }

    suspend fun addCustomSubject(subject: SubjectEntity) {
        dao.insertSubject(subject)
    }

    fun getTopicsForChapter(chapterId: String): Flow<List<TopicEntity>> = dao.getTopicsForChapterFlow(chapterId)

    fun getAllTopics(): Flow<List<TopicEntity>> = dao.getAllTopicsFlow()

    suspend fun updateTopicCompletion(topicId: String, isCompleted: Boolean) {
        dao.updateTopicCompletion(topicId, isCompleted)
    }

    suspend fun updateTopicWeakStatus(topicId: String, isWeak: Boolean) {
        dao.updateTopicWeakStatus(topicId, isWeak)
    }

    suspend fun markAllTopicsInChapter(chapterId: String, isCompleted: Boolean) {
        dao.markAllTopicsInChapter(chapterId, isCompleted)
        dao.updateChapterStatus(
            chapterId,
            if (isCompleted) ChapterStatus.COMPLETED else ChapterStatus.NOT_STARTED
        )
    }

    // Tasks
    val allTasks: Flow<List<StudyTaskEntity>> = dao.getAllTasksFlow()

    fun getTasksForDay(startOfDay: Long, endOfDay: Long): Flow<List<StudyTaskEntity>> =
        dao.getTasksForDayFlow(startOfDay, endOfDay)

    suspend fun saveTask(task: StudyTaskEntity) {
        dao.insertTask(task)
    }

    suspend fun updateTask(task: StudyTaskEntity) {
        dao.updateTask(task)
    }

    suspend fun toggleTaskComplete(taskId: String, isCompleted: Boolean) {
        val completedAt = if (isCompleted) System.currentTimeMillis() else null
        dao.setTaskCompleted(taskId, isCompleted, completedAt)
    }

    suspend fun deleteTask(taskId: String) {
        dao.deleteTask(taskId)
    }

    // Sessions
    val allSessions: Flow<List<StudySessionEntity>> = dao.getAllSessionsFlow()

    suspend fun saveSession(session: StudySessionEntity) {
        dao.insertSession(session)
    }

    suspend fun deleteSession(sessionId: String) {
        dao.deleteSession(sessionId)
    }

    // Notes
    val allNotes: Flow<List<NoteEntity>> = dao.getAllNotesFlow()

    suspend fun saveNote(note: NoteEntity) {
        dao.insertNote(note)
    }

    suspend fun deleteNote(noteId: String) {
        dao.deleteNote(noteId)
    }

    // Flashcards
    val allFlashcards: Flow<List<FlashcardEntity>> = dao.getAllFlashcardsFlow()

    fun getFlashcardsForSubject(subjectName: String): Flow<List<FlashcardEntity>> =
        dao.getFlashcardsForSubjectFlow(subjectName)

    suspend fun updateFlashcardStatus(cardId: String, status: FlashcardStatus) {
        dao.updateFlashcardStatus(cardId, status, System.currentTimeMillis())
    }

    // Reminders
    val allReminders: Flow<List<ReminderEntity>> = dao.getAllRemindersFlow()

    suspend fun saveReminder(reminder: ReminderEntity) {
        dao.insertReminder(reminder)
    }

    suspend fun toggleReminder(reminderId: String, isEnabled: Boolean) {
        dao.setReminderEnabled(reminderId, isEnabled)
    }

    suspend fun deleteReminder(reminderId: String) {
        dao.deleteReminder(reminderId)
    }
}
