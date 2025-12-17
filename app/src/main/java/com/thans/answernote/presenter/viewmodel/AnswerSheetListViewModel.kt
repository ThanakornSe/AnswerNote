package com.thans.answernote.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thans.answernote.data.local.entity.AnswerSheetEntity
import com.thans.answernote.data.local.entity.QuestionAnswerData
import com.thans.answernote.data.repository.AnswerSheetRepository
import com.thans.answernote.presenter.model.Answer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnswerSheetListViewModel(
    private val repository: AnswerSheetRepository
) : ViewModel() {

    private val _answerSheets = MutableStateFlow<List<AnswerSheetEntity>>(emptyList())
    val answerSheets: StateFlow<List<AnswerSheetEntity>> = _answerSheets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadAnswerSheets()
    }

    private fun loadAnswerSheets() {
        viewModelScope.launch {
            repository.getAllAnswerSheets().collect { sheets ->
                _answerSheets.value = sheets
            }
        }
    }

    fun createAnswerSheet(name: String, numberOfQuestions: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val answers = (1..numberOfQuestions).map { questionNum ->
                    QuestionAnswerData(
                        questionNumber = questionNum,
                        selectedAnswer = Answer.NONE,
                        isCorrect = null
                    )
                }

                val answerSheet = AnswerSheetEntity(
                    name = name,
                    numberOfQuestions = numberOfQuestions,
                    answers = answers,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                repository.insertAnswerSheet(answerSheet)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAnswerSheet(answerSheet: AnswerSheetEntity) {
        viewModelScope.launch {
            repository.deleteAnswerSheet(answerSheet)
        }
    }
}


