package com.thans.answernote.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thans.answernote.data.local.dao.AnswerSheetDao
import com.thans.answernote.data.local.entity.AnswerListConverter
import com.thans.answernote.data.local.entity.AnswerSheetEntity

@Database(
    entities = [AnswerSheetEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(AnswerListConverter::class)
abstract class AnswerNoteDatabase : RoomDatabase() {
    abstract fun answerSheetDao(): AnswerSheetDao

    companion object {
        const val DATABASE_NAME = "answer_note_database"
    }
}

