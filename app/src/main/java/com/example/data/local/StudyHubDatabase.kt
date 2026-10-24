package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ChapterEntity
import com.example.data.model.FlashcardEntity
import com.example.data.model.NoteEntity
import com.example.data.model.ReminderEntity
import com.example.data.model.StudySessionEntity
import com.example.data.model.StudyTaskEntity
import com.example.data.model.SubjectEntity
import com.example.data.model.TopicEntity
import com.example.data.model.UserProfile

@Database(
    entities = [
        UserProfile::class,
        SubjectEntity::class,
        ChapterEntity::class,
        TopicEntity::class,
        StudyTaskEntity::class,
        StudySessionEntity::class,
        NoteEntity::class,
        FlashcardEntity::class,
        ReminderEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class StudyHubDatabase : RoomDatabase() {

    abstract fun studyHubDao(): StudyHubDao

    companion object {
        @Volatile
        private var INSTANCE: StudyHubDatabase? = null

        fun getDatabase(context: Context): StudyHubDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudyHubDatabase::class.java,
                    "studyhub_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
