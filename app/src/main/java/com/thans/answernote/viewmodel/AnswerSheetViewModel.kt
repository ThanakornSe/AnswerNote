package com.thans.answernote.viewmodel

import androidx.lifecycle.ViewModel
import com.thans.answernote.model.Answer
import com.thans.answernote.model.QuestionAnswer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnswerSheetViewModel : ViewModel() {
    private val _answers = MutableStateFlow(
        (1..200).map { QuestionAnswer(questionNumber = it) }
    )
    val answers: StateFlow<List<QuestionAnswer>> = _answers.asStateFlow()

    fun selectAnswer(questionNumber: Int, answer: Answer) {
        _answers.value = _answers.value.map { question ->
            if (question.questionNumber == questionNumber) {
                question.copy(selectedAnswer = answer)
            } else {
                question
            }
        }
    }

    fun clearAll() {
        _answers.value = (1..200).map { QuestionAnswer(questionNumber = it) }
    }

    fun getAnsweredCount(): Int {
        return _answers.value.count { it.selectedAnswer != Answer.NONE }
    }

    fun exportAnswers(): String {
        val builder = StringBuilder()
        builder.append("TOEIC Answer Sheet (200 Questions)\n")
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
        builder.append("\nTotal Answered: ${getAnsweredCount()}/200\n")

        return builder.toString()
    }
}
