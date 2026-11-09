package com.yourdomain.taskmanagementapp.composables.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yourdomain.taskmanagementapp.presentation.TaskUiState
import com.yourdomain.taskmanagementapp.presentation.TaskViewModel

@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    // Inject ViewModel via default parameter (using standard library)
    viewModel: TaskViewModel = viewModel()
) {
    // Observe ViewModel state
    val uiState by viewModel.uiState.collectAsState()
    val tasks = viewModel.tasks // Read-only access to the list

    var taskName by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Input Row ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = taskName,
                onValueChange = {
                    taskName = it
                    if (uiState is TaskUiState.Error) viewModel.clearError()
                },
                label = { Text("Enter a new task") },
                modifier = Modifier
                    .weight(1f)
                    .semantics { contentDescription = "Enter new task field" },
                singleLine = true,
                // Disable input while loading
                enabled = uiState !is TaskUiState.Loading,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.addTask(taskName)
                    taskName = ""
                }),
                isError = uiState is TaskUiState.Error
            )

            Button(
                onClick = {
                    viewModel.addTask(taskName)
                    taskName = ""
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .semantics { contentDescription = "Add task button" },
                // Disable button while loading
                enabled = uiState !is TaskUiState.Loading
            ) {
                Text("Add")
            }
        }

        // --- Error & Loading Feedback ---
        Spacer(modifier = Modifier.height(8.dp))

        when (uiState) {
            is TaskUiState.Loading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            is TaskUiState.Error -> {
                Text(
                    text = (uiState as TaskUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            else -> Spacer(modifier = Modifier.height(4.dp)) // Layout stability
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Task List ---
        if (tasks.isEmpty() && uiState !is TaskUiState.Loading) {
            Text(
                text = "No tasks yet! Add one above.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = tasks,
                    key = { task -> task.id } // Use stable UUID key
                ) { task ->
                    // We update TaskItem to accept the Data Class title
                    TaskItem(
                        taskName = task.title,
                        isCompleted = task.isCompleted,
                        // Connect the event up to the ViewModel
                        onCheckedChange = { viewModel.toggleTaskCompletion(task.id) },
                        onDeleteClicked = { viewModel.deleteTask(task.id) }
                    )
                }
            }
        }
    }
}