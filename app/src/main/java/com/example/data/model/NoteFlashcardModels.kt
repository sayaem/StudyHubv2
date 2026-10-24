package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val subjectName: String,
    val chapterName: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class FlashcardStatus {
    NEW,
    REVIEW_AGAIN,
    KNOW_IT
}

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey
    val id: String,
    val subjectName: String,
    val chapterName: String = "",
    val questionFront: String,
    val answerBack: String,
    val status: FlashcardStatus = FlashcardStatus.NEW,
    val timesReviewed: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)
