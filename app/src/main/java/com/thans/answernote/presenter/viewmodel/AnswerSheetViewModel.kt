package com.thans.answernote.presenter.viewmodel

import androidx.lifecycle.ViewModel
import com.thans.answernote.model.Answer
import com.thans.answernote.model.QuestionAnswer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnswerSheetViewModel : ViewModel() {
    private val _numberOfQuestions = MutableStateFlow(200)
    val numberOfQuestions: StateFlow<Int> = _numberOfQuestions.asStateFlow()

    private val _answers = MutableStateFlow(
        (1..200).map {
            _root_ide_package_.com.thans.answernote.presenter.model.QuestionAnswer(
                questionNumber = it
            )
        }
    )
    val answers: StateFlow<List<com.thans.answernote.presenter.model.QuestionAnswer>> = _answers.asStateFlow()

    fun selectAnswer(questionNumber: Int, answer: com.thans.answernote.presenter.model.Answer) {
        _answers.value = _answers.value.map { question ->
            if (question.questionNumber == questionNumber) {
                question.copy(selectedAnswer = answer)
            } else {
                question
            }
        }
    }

    fun setNumberOfQuestions(count: Int) {
        _numberOfQuestions.value = count
        _answers.value = (1..count).map {
            _root_ide_package_.com.thans.answernote.presenter.model.QuestionAnswer(
                questionNumber = it
            )
        }
    }

    fun clearAll() {
        _answers.value = (1.._numberOfQuestions.value).map {
            _root_ide_package_.com.thans.answernote.presenter.model.QuestionAnswer(
                questionNumber = it
            )
        }
    }

    fun getAnsweredCount(): Int {
        return _answers.value.count { it.selectedAnswer != _root_ide_package_.com.thans.answernote.presenter.model.Answer.NONE }
    }

    fun markAnswerCorrectness(questionNumber: Int, isCorrect: Boolean) {
        _answers.value = _answers.value.map { question ->
            if (question.questionNumber == questionNumber) {
                question.copy(isCorrect = isCorrect)
            } else {
                question
            }
        }
    }

    fun getScore(): Pair<Int, Int> {
        val graded = _answers.value.filter { it.isCorrect != null }
        val correct = graded.count { it.isCorrect == true }
        val total = graded.size
        return Pair(correct, total)
    }

    fun exportAnswers(): String {
        val builder = StringBuilder()
        builder.append("TOEIC Answer Sheet (${_numberOfQuestions.value} Questions)\n")
        builder.append("=".repeat(40))
        builder.append("\n\n")

        _answers.value.forEach { question ->
            val answer = if (question.selectedAnswer == _root_ide_package_.com.thans.answernote.presenter.model.Answer.NONE) {
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
}
