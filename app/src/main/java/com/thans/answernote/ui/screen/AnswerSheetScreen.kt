package com.thans.answernote.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thans.answernote.ui.components.QuestionItem
import com.thans.answernote.ui.components.SummaryDialog
import com.thans.answernote.viewmodel.AnswerSheetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerSheetScreen(
    viewModel: AnswerSheetViewModel = viewModel()
) {
    val answers by viewModel.answers.collectAsState()
    val answeredCount = viewModel.getAnsweredCount()
    var showSummaryDialog by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("TOEIC Answer Sheet")
                        Text(
                            text = "Progress: $answeredCount/200",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear All")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showSummaryDialog = true },
                icon = { Icon(Icons.Default.Info, contentDescription = "Summary") },
                text = { Text("Summary") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(answers) { question ->
                QuestionItem(
                    questionNumber = question.questionNumber,
                    selectedAnswer = question.selectedAnswer,
                    onAnswerSelected = { answer ->
                        viewModel.selectAnswer(question.questionNumber, answer)
                    }
                )
            }
        }
    }

    if (showSummaryDialog) {
        SummaryDialog(
            answers = answers,
            answeredCount = answeredCount,
            onDismiss = { showSummaryDialog = false },
            onShare = {
                val shareText = viewModel.exportAnswers()
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Share Answer Sheet")
                context.startActivity(shareIntent)
            }
        )
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear All Answers") },
            text = { Text("Are you sure you want to clear all selected answers? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAll()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Clear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
