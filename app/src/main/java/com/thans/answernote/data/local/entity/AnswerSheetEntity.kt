package com.thans.answernote.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.thans.answernote.presenter.model.Answer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

@Serializable
data class QuestionAnswerData(
    val questionNumber: Int,
    val selectedAnswer: Answer = Answer.NONE,
    val isCorrect: Boolean? = null
)

@Entity(tableName = "answer_sheets")
@TypeConverters(AnswerListConverter::class)
data class AnswerSheetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val numberOfQuestions: Int,
    val answers: List<QuestionAnswerData>,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

class AnswerListConverter {
    @TypeConverter
    fun fromAnswerList(value: List<QuestionAnswerData>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toAnswerList(value: String): List<QuestionAnswerData> {
        return Json.decodeFromString(value)
    }
}




