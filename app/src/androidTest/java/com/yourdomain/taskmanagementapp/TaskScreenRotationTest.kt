package com.yourdomain.taskmanagementapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskScreenRotationTest {

    /**
     * We MUST use this rule to test rotation, as it controls the full Activity.
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun taskScreen_persistsTask_afterRotation() {
        // Arrange: Add a task
        composeTestRule.onNodeWithContentDescription("Enter new task field")
            .performTextInput("Buy groceries")
        composeTestRule.onNodeWithContentDescription("Add task button")
            .performClick()

        // Assert: Verify it's displayed before rotation
        composeTestRule.onNodeWithText("Buy groceries").assertIsDisplayed()

        // Act: Simulate a screen rotation
        // This destroys and recreates the MainActivity
        composeTestRule.activity.requestedOrientation =
            android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // We must wait for the UI to settle after rotation
        composeTestRule.waitForIdle()

        // Assert: Verify the task is STILL displayed
        // This works because we used rememberSaveable!
        composeTestRule.onNodeWithText("Buy groceries").assertIsDisplayed()
    }
}