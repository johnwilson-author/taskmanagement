package com.yourdomain.taskmanagementapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yourdomain.taskmanagementapp.composables.main.TaskScreen
import com.yourdomain.taskmanagementapp.presentation.TaskViewModel
import com.yourdomain.taskmanagementapp.ui.theme.TaskManagementTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * All UI tests must be annotated with @RunWith(AndroidJUnit4::class)
 */
@RunWith(AndroidJUnit4::class)
class TaskScreenTest {

    /**
     * This is the simple Compose test rule.
     * It launches just the composable you specify in setContent {}.
     * It's fast but CANNOT test activity-level events like screen rotation.
     */
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * This is the Android-specific test rule.
     * It launches your entire MainActivity.
     * It's slightly slower but CAN test rotation, permissions, etc.
     */
    // @get:Rule
    // val androidComposeTestRule = createAndroidComposeRule<MainActivity>()


    @Test
    fun taskScreen_addsAndDisplaysTask() {
        // Arrange
        composeTestRule.setContent {
            TaskManagementTheme {
                TaskScreen()
            }
        }

        // Act: Add a task
        composeTestRule.onNodeWithContentDescription("Enter new task field")
            .performTextInput("Buy groceries")

        composeTestRule.onNodeWithContentDescription("Add task button")
            .performClick()

        // Assert
        // Check that the new task is now displayed in the list
        composeTestRule.onNodeWithText("Buy groceries")
            .assertIsDisplayed()
    }

    @Test
    fun taskScreen_displaysListOfTasks() {
        // Arrange: Create a ViewModel with dummy data
        val viewModel = TaskViewModel()
        viewModel.addTask("Task 1")
        viewModel.addTask("Task 2")

        composeTestRule.setContent {
            TaskManagementTheme {
                TaskScreen(viewModel = viewModel)
            }
        }

        // Wait for the async tasks to populate the list
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            viewModel.tasks.size == 2
        }

        // Assert
        composeTestRule.onNodeWithText("Task 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Task 2").assertIsDisplayed()
    }

}