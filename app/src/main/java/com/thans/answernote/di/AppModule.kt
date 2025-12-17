package com.thans.answernote.di

import androidx.room.Room
import com.thans.answernote.data.local.database.AnswerNoteDatabase
import com.thans.answernote.data.repository.AnswerSheetRepository
import com.thans.answernote.presenter.viewmodel.AnswerSheetListViewModel
import com.thans.answernote.presenter.viewmodel.AnswerSheetViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AnswerNoteDatabase::class.java,
            AnswerNoteDatabase.DATABASE_NAME
        ).build()
    }

    // DAO
    single { get<AnswerNoteDatabase>().answerSheetDao() }

    // Repository
    single { AnswerSheetRepository(get()) }

    // ViewModels
    viewModel { AnswerSheetListViewModel(get()) }
    viewModel { AnswerSheetViewModel(get()) }
}