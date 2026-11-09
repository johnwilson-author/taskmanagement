package com.yourdomain.taskmanagementapp.data

import java.util.UUID

/**
 * Represents a single task.
 * @property id Unique identifier (UUID).
 * @property title The content of the task.
 * @property isCompleted Mutable state for checkbox status.
 */
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val isCompleted: Boolean = false
)