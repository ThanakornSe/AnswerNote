package com.thans.answernote.presenter.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thans.answernote.presenter.model.Answer
import com.thans.answernote.presenter.ui.components.QuestionCountDialog
import com.thans.answernote.presenter.ui.components.QuestionItem
import com.thans.answernote.presenter.viewmodel.AnswerSheetViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerSheetScreen(
    viewModel: AnswerSheetViewModel = koinViewModel(),
    onNavigateToSummary: (() -> Unit) = {},
    onNavigateBack: (() -> Unit) = {}
) {
    val answers by viewModel.answers.collectAsState()
    val numberOfQuestions by viewModel.numberOfQuestions.collectAsState()
    val answeredCount = remember(answers) {
        answers.count { it.selectedAnswer != Answer.NONE }
    }
    var showClearDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val isTestFinished = answeredCount == numberOfQuestions && numberOfQuestions > 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Answer Sheet")
                        Text(
                            text = "Progress: $answeredCount/$numberOfQuestions",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showSettingsDialog = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear All")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
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

            // Show button when test is finished
            if (isTestFinished) {
                Button(
                    onClick = onNavigateToSummary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("View Summary")
                }
            }
        }
    }

    if (showSettingsDialog) {
        QuestionCountDialog(
            currentCount = numberOfQuestions,
            onDismiss = { showSettingsDialog = false },
            onConfirm = { count ->
                viewModel.setNumberOfQuestions(count)
                showSettingsDialog = false
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

