package com.thans.answernote.presenter.ui.screen

import android.content.Intent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.thans.answernote.presenter.model.Answer
import com.thans.answernote.presenter.viewmodel.AnswerSheetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    viewModel: AnswerSheetViewModel,
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

    // Track scroll state for header animation
    val listState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 50
        }
    }

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
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Score Card - Sticky Header with dynamic sizing
            stickyHeader(
                key = "score_header"
            ) {
                ScoreCardHeader(
                    correctCount = correctCount,
                    gradedCount = gradedCount,
                    numberOfQuestions = numberOfQuestions,
                    isCompact = isScrolled
                )
            }

            // Instructions
            item {
                Text(
                    text = "Mark each answer as correct or wrong:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp)
                )
            }

            // Question Items
            items(answers.filter { it.selectedAnswer != Answer.NONE }) { question ->
                SummaryQuestionItem(
                    question = question,
                    onMarkCorrect = { viewModel.markAnswerCorrectness(question.questionNumber, true) },
                    onMarkWrong = { viewModel.markAnswerCorrectness(question.questionNumber, false) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )
            }

            // Show message if no answers
            if (answers.none { it.selectedAnswer != Answer.NONE }) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
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
fun ScoreCardHeader(
    correctCount: Int,
    gradedCount: Int,
    numberOfQuestions: Int,
    isCompact: Boolean = false
) {
    // Animate padding, elevation, and text sizes
    val cardPadding by animateDpAsState(
        targetValue = if (isCompact) 12.dp else 20.dp,
        animationSpec = tween(durationMillis = 300),
        label = "cardPadding"
    )

    val cardElevation by animateDpAsState(
        targetValue = if (isCompact) 2.dp else 4.dp,
        animationSpec = tween(durationMillis = 300),
        label = "cardElevation"
    )

    val titleTextScale by animateFloatAsState(
        targetValue = if (isCompact) 0.8f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "titleScale"
    )

    val valueTextScale by animateFloatAsState(
        targetValue = if (isCompact) 0.7f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "valueScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isCompact) Modifier
                else Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        shape = if (isCompact) RoundedCornerShape(0.dp) else RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(cardPadding)
        ) {
            if (!isCompact) {
                Text(
                    text = "Score",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize * titleTextScale
                    ),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (isCompact) Arrangement.SpaceAround else Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(
                    label = "Correct",
                    value = correctCount.toString(),
                    color = Color(0xFF4CAF50),
                    isCompact = isCompact,
                    textScale = valueTextScale
                )

                if (!isCompact) {
                    androidx.compose.material3.HorizontalDivider(
                        modifier = Modifier
                            .height(50.dp)
                            .width(1.dp)
                    )
                }

                StatItem(
                    label = "Graded",
                    value = "$gradedCount",
                    color = MaterialTheme.colorScheme.primary,
                    isCompact = isCompact,
                    textScale = valueTextScale
                )

                if (!isCompact) {
                    androidx.compose.material3.HorizontalDivider(
                        modifier = Modifier
                            .height(50.dp)
                            .width(1.dp)
                    )
                }

                StatItem(
                    label = "Total",
                    value = "$numberOfQuestions",
                    color = MaterialTheme.colorScheme.secondary,
                    isCompact = isCompact,
                    textScale = valueTextScale
                )
            }

            if (gradedCount > 0) {
                Spacer(modifier = Modifier.height(if (isCompact) 4.dp else 12.dp))
                Text(
                    text = if (isCompact) {
                        "${String.format(java.util.Locale.getDefault(), "%.1f", (correctCount.toFloat() / gradedCount * 100))}%"
                    } else {
                        "Percentage: ${String.format(java.util.Locale.getDefault(), "%.1f", (correctCount.toFloat() / gradedCount * 100))}%"
                    },
                    style = if (isCompact) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = if (isCompact) FontWeight.Bold else FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    color: Color,
    isCompact: Boolean = false,
    textScale: Float = 1f
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = if (isCompact) {
                MaterialTheme.typography.headlineSmall.copy(
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize * textScale
                )
            } else {
                MaterialTheme.typography.headlineLarge.copy(
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize * textScale
                )
            },
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = if (isCompact) {
                MaterialTheme.typography.labelSmall
            } else {
                MaterialTheme.typography.bodyMedium
            },
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

