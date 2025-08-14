package com.example.mvvmclassdemoapp

import androidx.activity.ComponentActivity
//import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
// OPTIONAL: only keep this import if you actually have this class in that package.
//import com.example.mvvmclassdemoapp.TeamViewModel
//import com.example.mvvmclassdemoapp.InsertTeamView

/**
 * UI tests for InsertTeamView using the real TeamViewModel.
 * No app code changes required.
 *
 * Relies on:
 *  - The form labels: "Team name", "Team city"
 *  - The submit button text: "Submit"
 *  - InsertTeamView clears both fields after calling vm.addTeam(...)
 *  - TeamViewModel appends to teamListState
 */
class InsertTeamViewTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun submit_disabled_until_both_fields_have_text_then_enabled() {

        val vm = TeamViewModel()

        composeRule.setContent {
            InsertTeamView(vm = vm)
        }

        // Labels exist
        composeRule.onNodeWithText("Team name").assertExists()
        composeRule.onNodeWithText("Team city").assertExists()

        // Submit disabled initially
        composeRule.onNodeWithText("Submit")
            .assertHasClickAction()
            .assertIsNotEnabled()

        // Enter only the first field -> still disabled
        val textFields = composeRule.onAllNodes(hasSetTextAction())
        textFields[0].performTextInput("Lakers")

        composeRule.onNodeWithText("Submit").assertIsNotEnabled()

        // Enter second field -> enabled
        textFields[1].performTextInput("Los Angeles")
        composeRule.onNodeWithText("Submit").assertIsEnabled()
    }

    @Test
    fun clicking_submit_adds_team_to_viewmodel_and_clears_fields() {
        val vm = TeamViewModel()

        composeRule.setContent {
            InsertTeamView(vm = vm)
        }

        val textFields = composeRule.onAllNodes(hasSetTextAction())
        textFields[0].performTextInput("Warriors")
        textFields[1].performTextInput("San Francisco")

        // Enabled now
        val submit = composeRule.onNodeWithText("Submit")
        submit.assertIsEnabled().performClick()

        // Verify VM state updated (one team appended)
        val list = vm.teamListState.value
        assertEquals(1, list.size)

        // OPTIONAL: Only enable this if the Team class & package exist
        // assertEquals(Team("Warriors", "San Francisco"), list.first())

        // After submit, fields cleared -> button disabled again
        submit.assertIsNotEnabled()
    }
}
