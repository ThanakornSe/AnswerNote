package com.thans.answernote.data.local.dao

import androidx.room.*
import com.thans.answernote.data.local.entity.AnswerSheetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnswerSheetDao {
    @Query("SELECT * FROM answer_sheets ORDER BY updatedAt DESC")
    fun getAllAnswerSheets(): Flow<List<AnswerSheetEntity>>

    @Query("SELECT * FROM answer_sheets WHERE id = :id")
    suspend fun getAnswerSheetById(id: Long): AnswerSheetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswerSheet(answerSheet: AnswerSheetEntity): Long

    @Update
    suspend fun updateAnswerSheet(answerSheet: AnswerSheetEntity)

    @Delete
    suspend fun deleteAnswerSheet(answerSheet: AnswerSheetEntity)

    @Query("DELETE FROM answer_sheets WHERE id = :id")
    suspend fun deleteAnswerSheetById(id: Long)
}

