package com.thans.answernote.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thans.answernote.data.local.entity.QuestionAnswerData
import com.thans.answernote.data.repository.AnswerSheetRepository
import com.thans.answernote.presenter.model.Answer
import com.thans.answernote.presenter.model.QuestionAnswer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnswerSheetViewModel(
    private val repository: AnswerSheetRepository
) : ViewModel() {
    private var currentAnswerSheetId: Long? = null
    private var currentAnswerSheetName: String = ""

    private val _numberOfQuestions = MutableStateFlow(200)
    val numberOfQuestions: StateFlow<Int> = _numberOfQuestions.asStateFlow()

    private val _answers = MutableStateFlow<List<QuestionAnswer>>(emptyList())
    val answers: StateFlow<List<QuestionAnswer>> = _answers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadAnswerSheet(answerSheetId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val answerSheet = repository.getAnswerSheetById(answerSheetId)
                answerSheet?.let {
                    currentAnswerSheetId = it.id
                    currentAnswerSheetName = it.name
                    _numberOfQuestions.value = it.numberOfQuestions
                    _answers.value = it.answers.map { data ->
                        QuestionAnswer(
                            questionNumber = data.questionNumber,
                            selectedAnswer = data.selectedAnswer,
                            isCorrect = data.isCorrect
                        )
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectAnswer(questionNumber: Int, answer: Answer) {
        _answers.value = _answers.value.map { question ->
            if (question.questionNumber == questionNumber) {
                question.copy(selectedAnswer = answer)
            } else {
                question
            }
        }
        saveAnswerSheet()
    }

    fun setNumberOfQuestions(count: Int) {
        _numberOfQuestions.value = count
        _answers.value = (1..count).map {
            QuestionAnswer(questionNumber = it)
        }
        saveAnswerSheet()
    }

    fun clearAll() {
        _answers.value = (1.._numberOfQuestions.value).map {
            QuestionAnswer(questionNumber = it)
        }
        saveAnswerSheet()
    }

    fun getAnsweredCount(): Int {
        return _answers.value.count { it.selectedAnswer != Answer.NONE }
    }

    fun markAnswerCorrectness(questionNumber: Int, isCorrect: Boolean) {
        _answers.value = _answers.value.map { question ->
            if (question.questionNumber == questionNumber) {
                question.copy(isCorrect = isCorrect)
            } else {
                question
            }
        }
        saveAnswerSheet()
    }

    fun getScore(): Pair<Int, Int> {
        val graded = _answers.value.filter { it.isCorrect != null }
        val correct = graded.count { it.isCorrect == true }
        val total = graded.size
        return Pair(correct, total)
    }

    fun exportAnswers(): String {
        val builder = StringBuilder()
        builder.append("$currentAnswerSheetName (${_numberOfQuestions.value} Questions)\n")
        builder.append("=".repeat(40))
        builder.append("\n\n")

        _answers.value.forEach { question ->
            val answer = if (question.selectedAnswer == Answer.NONE) {
                "-"
            } else {
                question.selectedAnswer.name
            }
            builder.append("${question.questionNumber}. $answer\n")
        }

        builder.append("\n")
        builder.append("=".repeat(40))
        builder.append("\nTotal Answered: ${getAnsweredCount()}/${_numberOfQuestions.value}\n")

        return builder.toString()
    }

    private fun saveAnswerSheet() {
        currentAnswerSheetId?.let { id ->
            viewModelScope.launch {
                val answerSheet = repository.getAnswerSheetById(id)
                answerSheet?.let { sheet ->
                    val updatedAnswers = _answers.value.map { answer ->
                        QuestionAnswerData(
                            questionNumber = answer.questionNumber,
                            selectedAnswer = answer.selectedAnswer,
                            isCorrect = answer.isCorrect
                        )
                    }
                    repository.updateAnswerSheet(
                        sheet.copy(
                            answers = updatedAnswers,
                            numberOfQuestions = _numberOfQuestions.value,
                            updatedAt = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }
}
