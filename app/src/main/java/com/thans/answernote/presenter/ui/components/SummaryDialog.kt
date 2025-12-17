package com.thans.answernote.presenter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thans.answernote.model.Answer
import com.thans.answernote.model.QuestionAnswer

@Composable
fun SummaryDialog(
    answers: List<com.thans.answernote.presenter.model.QuestionAnswer>,
    answeredCount: Int,
    onDismiss: () -> Unit,
    onShare: () -> Unit
) {
    val totalQuestions = answers.size

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Answer Sheet Summary")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Answered",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "$answeredCount",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Column {
                            Text(
                                text = "Remaining",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${totalQuestions - answeredCount}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your Answers:",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(answers) { question ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${question.questionNumber}.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = if (question.selectedAnswer == _root_ide_package_.com.thans.answernote.presenter.model.Answer.NONE) "-" else question.selectedAnswer.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (question.selectedAnswer == _root_ide_package_.com.thans.answernote.presenter.model.Answer.NONE) {
                                    MaterialTheme.colorScheme.outline
                                } else {
                                    MaterialTheme.colorScheme.primary
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onShare) {
                Icon(Icons.Default.Share, contentDescription = "Share")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

