package com.thans.answernote

import android.app.Application
import com.thans.answernote.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AnswerNoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
            androidContext(this@AnswerNoteApplication)
            androidLogger()
        }
    }
}