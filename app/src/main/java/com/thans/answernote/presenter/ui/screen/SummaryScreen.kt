package com.thans.answernote.presenter.ui.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.thans.answernote.model.Answer
import com.thans.answernote.model.QuestionAnswer
import com.thans.answernote.viewmodel.AnswerSheetViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    viewModel: com.thans.answernote.presenter.viewmodel.AnswerSheetViewModel,
    onNavigateBack: () -> Unit
) {
    val answers by viewModel.answers.collectAsState()
    val numberOfQuestions by viewModel.numberOfQuestions.collectAsState()
    val context = LocalContext.current
    
    // Make score reactive - recalculate when answers change
    val score by remember {
        derivedStateOf {
            val graded = answers.filter { it.isCorrect != null }
            val correct = graded.count { it.isCorrect == true }
            val total = graded.size
            Pair(correct, total)
        }
    }
    val correctCount = score.first
    val gradedCount = score.second

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Answer Summary") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val shareText = viewModel.exportAnswers()
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share Answer Sheet")
                            context.startActivity(shareIntent)
                        }
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Score Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Score",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem(
                                label = "Correct",
                                value = correctCount.toString(),
                                color = Color(0xFF4CAF50)
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(1.dp)
                            )
                            StatItem(
                                label = "Graded",
                                value = "$gradedCount",
                                color = MaterialTheme.colorScheme.primary
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(1.dp)
                            )
                            StatItem(
                                label = "Total",
                                value = "$numberOfQuestions",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        if (gradedCount > 0) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Percentage: ${String.format(Locale.getDefault(), "%.1f", (correctCount.toFloat() / gradedCount * 100))}%",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Instructions
            item {
                Text(
                    text = "Mark each answer as correct or wrong:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Question Items
            items(answers.filter { it.selectedAnswer != _root_ide_package_.com.thans.answernote.presenter.model.Answer.NONE }) { question ->
                SummaryQuestionItem(
                    question = question,
                    onMarkCorrect = { viewModel.markAnswerCorrectness(question.questionNumber, true) },
                    onMarkWrong = { viewModel.markAnswerCorrectness(question.questionNumber, false) },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Show message if no answers
            if (answers.none { it.selectedAnswer != _root_ide_package_.com.thans.answernote.presenter.model.Answer.NONE }) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp)
                    ) {
                        Text(
                            text = "No answers to grade yet.\nGo back and answer some questions!",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SummaryQuestionItem(
    question: com.thans.answernote.presenter.model.QuestionAnswer,
    onMarkCorrect: () -> Unit,
    onMarkWrong: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (question.isCorrect) {
                true -> Color(0xFFE8F5E9) // Light green
                false -> Color(0xFFFFEBEE) // Light red
                null -> MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Question info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${question.questionNumber}.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.Black
                    ),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(50.dp)
                )

                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = question.selectedAnswer.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Marking buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Correct button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (question.isCorrect == true) Color(0xFF4CAF50) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = if (question.isCorrect == true) Color(0xFF4CAF50) else Color(0xFF81C784),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable(onClick = onMarkCorrect),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Mark as Correct",
                        tint = if (question.isCorrect == true) Color.White else Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Wrong button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (question.isCorrect == false) Color(0xFFF44336) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = if (question.isCorrect == false) Color(0xFFF44336) else Color(0xFFE57373),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable(onClick = onMarkWrong),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Mark as Wrong",
                        tint = if (question.isCorrect == false) Color.White else Color(0xFFF44336),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

