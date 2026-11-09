package com.yourdomain.taskmanagementapp.logic

object InputValidator {

    /**
     * Validates the task title using Kotlin's String manipulation.
     * Returns a Result type (Success or Failure).
     */
    fun validateTaskTitle(input: String?): Result<String> {
        // 1. Null Safety & String Manipulation
        if (input.isNullOrBlank()) {
            return Result.failure(IllegalArgumentException("Task cannot be empty."))
        }

        val trimmed = input.trim()

        if (trimmed.length < 3) {
            return Result.failure(IllegalArgumentException("Task is too short (min 3 chars)."))
        }

        // 2. Simulated Security/Sanitization
        // In a real app, we might check for malicious script tags here before storage.
        return Result.success(trimmed)
    }
}