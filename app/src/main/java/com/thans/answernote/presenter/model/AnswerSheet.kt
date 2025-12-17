package com.thans.answernote.presenter.model

enum class Answer {
    A, B, C, D, NONE
}

data class QuestionAnswer(
    val questionNumber: Int,
    val selectedAnswer: Answer = Answer.NONE,
    val correctAnswer: Answer? = null, // The correct answer for this question
    val isCorrect: Boolean? = null // null = not graded, true = correct, false = wrong (calculated from correctAnswer)
)


