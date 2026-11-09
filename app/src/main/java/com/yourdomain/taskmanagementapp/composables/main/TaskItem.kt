package com.yourdomain.taskmanagementapp.composables.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourdomain.taskmanagementapp.ui.theme.TaskManagementTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon

@Composable
fun TaskItem(
    taskName: String,
    modifier: Modifier = Modifier, // Best Practice: modifier is the first optional param
    isCompleted: Boolean = false,   // New State Parameter
    onCheckedChange: (Boolean) -> Unit = {}, // New Event Parameter
    onDeleteClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // The "Fix": Add the Checkbox
        Checkbox(
            checked = isCompleted,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .semantics {
                    // This matches the assertion in our test
                    contentDescription = "Mark task as complete"
                }
                // Use testTag for reliable node identification in tests, as recommended
                // in official Jetpack Compose testing documentation.
                .testTag("task_checkbox")
        )

        Text(
            text = taskName,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                // Update semantics to be more descriptive for accessibility
                .semantics { contentDescription = "Task name: $taskName" },
            style = MaterialTheme.typography.bodyLarge
        )

        IconButton(onClick = onDeleteClicked) {
            Icon(
                imageVector = Icons.Filled.Delete, // Use Filled (Standard)
                contentDescription = "Delete task",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    TaskManagementTheme {
        TaskItem(
            taskName = "Write the first draft",
            isCompleted = true
        )
    }
}