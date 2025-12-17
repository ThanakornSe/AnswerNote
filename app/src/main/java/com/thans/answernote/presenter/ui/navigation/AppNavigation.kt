package com.thans.answernote.presenter.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object AnswerSheetListScreen : NavKey

@Serializable
data class AnswerSheetMainScreen(val answerSheetId: Long) : NavKey

@Serializable
data class SummaryMainScreen(val answerSheetId: Long) : NavKey
