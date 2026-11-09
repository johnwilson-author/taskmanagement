package com.yourdomain.taskmanagementapp.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourdomain.taskmanagementapp.data.Task
import com.yourdomain.taskmanagementapp.logic.InputValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Sealed class for UI State (Loading/Success/Error)
sealed class TaskUiState {
    data object Idle : TaskUiState()
    data object Loading : TaskUiState()
    data class Error(val message: String) : TaskUiState()
}

class TaskViewModel : ViewModel() {

    // Mutable collection for internal logic
    private val _tasks = mutableStateListOf<Task>()
    // Immutable view for the UI (Concept: Immutable vs Mutable Collections)
    val tasks: List<Task> get() = _tasks

    // StateFlow for UI status (Loading/Error)
    private val _uiState = MutableStateFlow<TaskUiState>(TaskUiState.Idle)
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    fun addTask(title: String) {
        // 1. Input Validation (Result Type)
        val validationResult = InputValidator.validateTaskTitle(title)

        validationResult.onFailure { exception ->
            // Handle error state
            _uiState.value = TaskUiState.Error(exception.message ?: "Invalid Input")
        }

        validationResult.onSuccess { validTitle ->
            // 2. Coroutines: Launch a background job
            viewModelScope.launch {
                try {
                    _uiState.value = TaskUiState.Loading

                    // Simulate network/database delay (suspend function)
                    delay(1000)

                    // Add to collection
                    _tasks.add(Task(title = validTitle))

                    // Reset UI state
                    _uiState.value = TaskUiState.Idle
                } catch (_: Exception) {
                    // 3. Error Handling
                    _uiState.value = TaskUiState.Error("Failed to save task.")
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = TaskUiState.Idle
    }

    fun toggleTaskCompletion(taskId: String) {
        // Find index to update the specific item in the mutable list
        val index = _tasks.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val task = _tasks[index]
            // Create a copy with the toggled state
            _tasks[index] = task.copy(isCompleted = !task.isCompleted)
        }
    }

    fun deleteTask(taskId: String) {
        _tasks.removeAll { it.id == taskId }
    }

}