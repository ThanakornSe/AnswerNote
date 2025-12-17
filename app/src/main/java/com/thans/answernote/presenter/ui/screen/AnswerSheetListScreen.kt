package com.thans.answernote.presenter.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.thans.answernote.data.local.entity.AnswerSheetEntity
import com.thans.answernote.presenter.ui.components.CreateAnswerSheetDialog
import com.thans.answernote.presenter.viewmodel.AnswerSheetListViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerSheetListScreen(
    viewModel: AnswerSheetListViewModel = koinViewModel(),
    onNavigateToAnswerSheet: (Long) -> Unit
) {
    val answerSheets by viewModel.answerSheets.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var answerSheetToDelete by remember { mutableStateOf<AnswerSheetEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Answer Sheets") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Answer Sheet")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (answerSheets.isEmpty() && !isLoading) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No Answer Sheets",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap the + button to create your first answer sheet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(answerSheets, key = { it.id }) { answerSheet ->
                        AnswerSheetCard(
                            answerSheet = answerSheet,
                            onClick = { onNavigateToAnswerSheet(answerSheet.id) },
                            onDeleteClick = { answerSheetToDelete = answerSheet }
                        )
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    if (showCreateDialog) {
        CreateAnswerSheetDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { name, numberOfQuestions ->
                viewModel.createAnswerSheet(name, numberOfQuestions)
                showCreateDialog = false
            }
        )
    }

    answerSheetToDelete?.let { sheet ->
        AlertDialog(
            onDismissRequest = { answerSheetToDelete = null },
            title = { Text("Delete Answer Sheet") },
            text = { Text("Are you sure you want to delete \"${sheet.name}\"? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAnswerSheet(sheet)
                        answerSheetToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { answerSheetToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AnswerSheetCard(
    answerSheet: AnswerSheetEntity,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()) }
    val answeredCount = answerSheet.answers.count { it.selectedAnswer != com.thans.answernote.presenter.model.Answer.NONE }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = answerSheet.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Questions: ${answerSheet.numberOfQuestions}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Answered: $answeredCount/${answerSheet.numberOfQuestions}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Updated: ${dateFormat.format(Date(answerSheet.updatedAt))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

