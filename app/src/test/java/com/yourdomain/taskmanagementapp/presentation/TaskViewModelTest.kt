package com.yourdomain.taskmanagementapp.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

// Reusable Rule to set the Main dispatcher for ViewModel tests
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}

class TaskViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun toggleTaskCompletion_updatesTaskState() = runTest {
        // Arrange
        val viewModel = TaskViewModel()
        viewModel.addTask("Test Task")

        // Advance time so the delay(1000) in addTask completes
        advanceUntilIdle()

        val task = viewModel.tasks.first()

        // Act
        viewModel.toggleTaskCompletion(task.id)

        // Assert
        assertTrue(viewModel.tasks.first().isCompleted)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteTask_removesTaskFromList() = runTest {
        // Arrange
        val viewModel = TaskViewModel()
        viewModel.addTask("Task to Delete")
        advanceUntilIdle() // Wait for async add

        val task = viewModel.tasks.first()

        // Act
        viewModel.deleteTask(task.id)

        // Assert
        assertTrue(viewModel.tasks.isEmpty())
    }
}