package com.thans.answernote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.thans.answernote.ui.screen.AnswerSheetScreen
import com.thans.answernote.ui.theme.AnswerNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnswerNoteTheme {
                AnswerSheetScreen()
            }
        }
    }
}
