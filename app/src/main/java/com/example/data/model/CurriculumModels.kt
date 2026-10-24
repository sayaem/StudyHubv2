package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class AcademicLevel(val label: String, val classLabel: String, val description: String) {
    SSC("SSC", "Class 9-10", "Secondary School Certificate preparation"),
    HSC("HSC", "Class 11-12", "Higher Secondary Certificate preparation"),
    ADMISSION("Admission", "University Preparation", "Engineering, Medical & Varsity Admission")
}

enum class EducationBoard(val displayName: String) {
    DHAKA("Dhaka"),
    RAJSHAHI("Rajshahi"),
    CHITTAGONG("Chittagong"),
    COMILLA("Comilla"),
    BARISAL("Barisal"),
    SYLHET("Sylhet"),
    DINAJPUR("Dinajpur"),
    MYMENSINGH("Mymensingh"),
    JESSORE("Jessore"),
    MADRASAH("Madrasah"),
    TECHNICAL("Technical")
}

enum class StudyStream(val displayName: String) {
    SCIENCE("Science"),
    COMMERCE("Business Studies (Commerce)"),
    HUMANITIES("Humanities (Arts)")
}

enum class ChapterStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val level: String, // SSC, HSC, ADMISSION
    val groupName: String, // Science, Commerce, Humanities, All
    val colorHex: String,
    val sortOrder: Int = 0,
    val isCustom: Boolean = false
)

@Entity(
    tableName = "chapters",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("subjectId")]
)
data class ChapterEntity(
    @PrimaryKey
    val id: String,
    val subjectId: String,
    val title: String,
    val chapterNumber: Int,
    val status: ChapterStatus = ChapterStatus.NOT_STARTED,
    val notes: String = "",
    val sortOrder: Int = 0
)

@Entity(
    tableName = "topics",
    foreignKeys = [
        ForeignKey(
            entity = ChapterEntity::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("chapterId")]
)
data class TopicEntity(
    @PrimaryKey
    val id: String,
    val chapterId: String,
    val title: String,
    val isCompleted: Boolean = false,
    val isWeak: Boolean = false,
    val sortOrder: Int = 0
)
