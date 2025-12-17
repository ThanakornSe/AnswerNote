package com.thans.answernote.presenter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateAnswerSheetDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, numberOfQuestions: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var questionCount by remember { mutableStateOf("200") }
    var nameError by remember { mutableStateOf(false) }
    var questionError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Answer Sheet") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = it.isBlank()
                    },
                    label = { Text("Name") },
                    placeholder = { Text("Enter answer sheet name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError,
                    supportingText = if (nameError) {
                        { Text("Name cannot be empty") }
                    } else null,
                    singleLine = true
                )

                OutlinedTextField(
                    value = questionCount,
                    onValueChange = {
                        questionCount = it
                        questionError = it.toIntOrNull() == null || it.toInt() <= 0
                    },
                    label = { Text("Number of Questions") },
                    placeholder = { Text("Enter number of questions") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = questionError,
                    supportingText = if (questionError) {
                        { Text("Please enter a valid number greater than 0") }
                    } else null,
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val isValid = name.isNotBlank() &&
                                  questionCount.toIntOrNull() != null &&
                                  questionCount.toInt() > 0

                    if (isValid) {
                        onConfirm(name, questionCount.toInt())
                    } else {
                        nameError = name.isBlank()
                        questionError = questionCount.toIntOrNull() == null ||
                                       questionCount.toInt() <= 0
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

