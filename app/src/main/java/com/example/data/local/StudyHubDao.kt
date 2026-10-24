package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

@Dao
interface StudyHubDao {

    // --- Profile ---
    @Query("SELECT * FROM user_profile WHERE id = 'default_user' LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 'default_user' LIMIT 1")
    suspend fun getUserProfile(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    // --- Subjects ---
    @Query("SELECT * FROM subjects WHERE level = :level ORDER BY sortOrder ASC")
    fun getSubjectsForLevelFlow(level: String): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM subjects ORDER BY sortOrder ASC")
    fun getAllSubjectsFlow(): Flow<List<SubjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: SubjectEntity)

    // --- Chapters ---
    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId ORDER BY sortOrder ASC, chapterNumber ASC")
    fun getChaptersForSubjectFlow(subjectId: String): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters ORDER BY sortOrder ASC")
    fun getAllChaptersFlow(): Flow<List<ChapterEntity>>

    @Query("UPDATE chapters SET status = :status WHERE id = :chapterId")
    suspend fun updateChapterStatus(chapterId: String, status: ChapterStatus)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    // --- Topics ---
    @Query("SELECT * FROM topics WHERE chapterId = :chapterId ORDER BY sortOrder ASC")
    fun getTopicsForChapterFlow(chapterId: String): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics")
    fun getAllTopicsFlow(): Flow<List<TopicEntity>>

    @Query("UPDATE topics SET isCompleted = :isCompleted WHERE id = :topicId")
    suspend fun updateTopicCompletion(topicId: String, isCompleted: Boolean)

    @Query("UPDATE topics SET isWeak = :isWeak WHERE id = :topicId")
    suspend fun updateTopicWeakStatus(topicId: String, isWeak: Boolean)

    @Query("UPDATE topics SET isCompleted = :isCompleted WHERE chapterId = :chapterId")
    suspend fun markAllTopicsInChapter(chapterId: String, isCompleted: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<TopicEntity>)

    // --- Study Tasks ---
    @Query("SELECT * FROM study_tasks ORDER BY dueDate ASC, priority DESC")
    fun getAllTasksFlow(): Flow<List<StudyTaskEntity>>

    @Query("SELECT * FROM study_tasks WHERE dueDate >= :startOfDay AND dueDate < :endOfDay ORDER BY isCompleted ASC, priority DESC")
    fun getTasksForDayFlow(startOfDay: Long, endOfDay: Long): Flow<List<StudyTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: StudyTaskEntity)

    @Update
    suspend fun updateTask(task: StudyTaskEntity)

    @Query("UPDATE study_tasks SET isCompleted = :isCompleted, completedAt = :completedAt WHERE id = :taskId")
    suspend fun setTaskCompleted(taskId: String, isCompleted: Boolean, completedAt: Long?)

    @Query("DELETE FROM study_tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: String)

    // --- Study Sessions ---
    @Query("SELECT * FROM study_sessions ORDER BY endedAt DESC")
    fun getAllSessionsFlow(): Flow<List<StudySessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity)

    @Query("DELETE FROM study_sessions WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: String)

    // --- Notes ---
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotesFlow(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: String)

    // --- Flashcards ---
    @Query("SELECT * FROM flashcards ORDER BY updatedAt DESC")
    fun getAllFlashcardsFlow(): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE subjectName = :subjectName ORDER BY updatedAt DESC")
    fun getFlashcardsForSubjectFlow(subjectName: String): Flow<List<FlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(flashcards: List<FlashcardEntity>)

    @Query("UPDATE flashcards SET status = :status, timesReviewed = timesReviewed + 1, updatedAt = :timestamp WHERE id = :cardId")
    suspend fun updateFlashcardStatus(cardId: String, status: FlashcardStatus, timestamp: Long)

    // --- Reminders ---
    @Query("SELECT * FROM reminders ORDER BY scheduledTimestamp ASC")
    fun getAllRemindersFlow(): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity)

    @Query("UPDATE reminders SET isEnabled = :isEnabled WHERE id = :reminderId")
    suspend fun setReminderEnabled(reminderId: String, isEnabled: Boolean)

    @Query("DELETE FROM reminders WHERE id = :reminderId")
    suspend fun deleteReminder(reminderId: String)
}
