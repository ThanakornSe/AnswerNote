package com.thans.answernote.presenter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thans.answernote.model.Answer

@Composable
fun QuestionItem(
    questionNumber: Int,
    selectedAnswer: com.thans.answernote.presenter.model.Answer,
    onAnswerSelected: (com.thans.answernote.presenter.model.Answer) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Question $questionNumber",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(_root_ide_package_.com.thans.answernote.presenter.model.Answer.A, _root_ide_package_.com.thans.answernote.presenter.model.Answer.B, _root_ide_package_.com.thans.answernote.presenter.model.Answer.C, _root_ide_package_.com.thans.answernote.presenter.model.Answer.D).forEach { answer ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        RadioButton(
                            selected = selectedAnswer == answer,
                            onClick = { onAnswerSelected(answer) }
                        )
                        Text(
                            text = answer.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

