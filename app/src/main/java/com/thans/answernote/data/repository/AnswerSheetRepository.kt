package com.thans.answernote.data.repository

import com.thans.answernote.data.local.dao.AnswerSheetDao
import com.thans.answernote.data.local.entity.AnswerSheetEntity
import kotlinx.coroutines.flow.Flow

class AnswerSheetRepository(
    private val answerSheetDao: AnswerSheetDao
) {
    fun getAllAnswerSheets(): Flow<List<AnswerSheetEntity>> {
        return answerSheetDao.getAllAnswerSheets()
    }

    suspend fun getAnswerSheetById(id: Long): AnswerSheetEntity? {
        return answerSheetDao.getAnswerSheetById(id)
    }

    suspend fun insertAnswerSheet(answerSheet: AnswerSheetEntity): Long {
        return answerSheetDao.insertAnswerSheet(answerSheet)
    }

    suspend fun updateAnswerSheet(answerSheet: AnswerSheetEntity) {
        answerSheetDao.updateAnswerSheet(answerSheet)
    }

    suspend fun deleteAnswerSheet(answerSheet: AnswerSheetEntity) {
        answerSheetDao.deleteAnswerSheet(answerSheet)
    }

    suspend fun deleteAnswerSheetById(id: Long) {
        answerSheetDao.deleteAnswerSheetById(id)
    }
}

