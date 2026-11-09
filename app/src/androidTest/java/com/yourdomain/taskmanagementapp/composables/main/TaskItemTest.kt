package com.yourdomain.taskmanagementapp.composables.main

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yourdomain.taskmanagementapp.ui.theme.TaskManagementTheme
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun taskItem_displaysCheckbox_and_toggles() {
        // 1. Arrange: Render the TaskItem
        // We haven't updated the Composable yet, so we just pass the title.
        composeTestRule.setContent {
            TaskManagementTheme {
                TaskItem(taskName = "Test Task")
            }
        }

        // 2. Act & Assert (The "Red" Phase)

        // Verify the text is visible (This should pass)
        composeTestRule.onNodeWithText("Test Task").assertIsDisplayed()

        // Verify a Checkbox exists (This will FAIL)
        // This combined matcher ensures we find a specific checkbox by description
        // and role, improving test specificity as per Jetpack Compose testing guidelines.
        composeTestRule.onNode(
            hasContentDescription("Mark task as complete")
                .and(hasRole(Role.Checkbox))
        )
            .assertIsDisplayed()
            .assertIsOff() // Check initial state
            .performClick() // Simulate user interaction
            .assertIsOn() // Verify state changed (requires implementation)
    }

    @Test
    fun taskItem_checkboxClick_triggersCallback() {
        var wasChecked = false
        composeTestRule.setContent {
            TaskManagementTheme {
                TaskItem(
                    taskName = "Test",
                    isCompleted = false,
                    onCheckedChange = { wasChecked = true } // Capture the event
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Mark task as complete")
            .performClick()

        assertTrue("Checkbox click should trigger callback", wasChecked)
    }

    @Test
    fun taskItem_deleteButton_triggersCallback() {
        var deleteRequested = false
        composeTestRule.setContent {
            TaskManagementTheme {
                TaskItem(
                    taskName = "Test",
                    onDeleteClicked = { deleteRequested = true }
                )
            }
        }

        // We expect a button with this content description
        composeTestRule.onNodeWithContentDescription("Delete task").performClick()

        assertTrue(deleteRequested)
    }


}

// Helper function to match nodes by Role (Semantics Property)
fun hasRole(role: Role) = SemanticsMatcher.expectValue(SemanticsProperties.Role, role)