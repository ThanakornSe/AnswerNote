package com.thans.answernote.model

enum class Answer {
    A, B, C, D, NONE
}

data class QuestionAnswer(
    val questionNumber: Int,
    val selectedAnswer: Answer = Answer.NONE
)


