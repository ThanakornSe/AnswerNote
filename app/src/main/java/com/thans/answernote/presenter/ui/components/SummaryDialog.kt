//package com.thans.answernote.presenter.ui.components
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Share
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun SummaryDialog(
//    answers: List<com.thans.answernote.presenter.model.QuestionAnswer>,
//    answeredCount: Int,
//    onDismiss: () -> Unit,
//    onShare: () -> Unit
//) {
//    val totalQuestions = answers.size
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = {
//            Text("Answer Sheet Summary")
//        },
//        text = {
//            Column(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.primaryContainer
//                    )
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Column {
//                            Text(
//                                text = "Answered",
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                            Text(
//                                text = "$answeredCount",
//                                style = MaterialTheme.typography.headlineMedium,
//                                color = MaterialTheme.colorScheme.primary
//                            )
//                        }
//                        Column {
//                            Text(
//                                text = "Remaining",
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                            Text(
//                                text = "${totalQuestions - answeredCount}",
//                                style = MaterialTheme.typography.headlineMedium,
//                                color = MaterialTheme.colorScheme.secondary
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = "Your Answers:",
//                    style = MaterialTheme.typography.titleMedium
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(300.dp)
//                ) {
//                    items(answers) { question ->
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 4.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text(
//                                text = "${question.questionNumber}.",
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                            Text(
//                                text = if (question.selectedAnswer == _root_ide_package_.com.thans.answernote.presenter.model.Answer.NONE) "-" else question.selectedAnswer.name,
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = if (question.selectedAnswer == _root_ide_package_.com.thans.answernote.presenter.model.Answer.NONE) {
//                                    MaterialTheme.colorScheme.outline
//                                } else {
//                                    MaterialTheme.colorScheme.primary
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        },
//        confirmButton = {
//            Button(onClick = onShare) {
//                Icon(Icons.Default.Share, contentDescription = "Share")
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Share")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Close")
//            }
//        }
//    )
//}
//
