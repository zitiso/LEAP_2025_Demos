package com.example.testingdemoapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class MainActivityUiTests {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    // --- Test 1: Only verify the presence of UI components ---
    @Test
    fun screen_has_twoInputs_addButton_and_answerBox() {
        composeRule.onNodeWithTag("text-a").assertIsDisplayed()
        composeRule.onNodeWithTag("text-b").assertIsDisplayed()
        composeRule.onNodeWithTag("button-add").assertIsDisplayed()
        composeRule.onNodeWithTag("text-c").assertIsDisplayed()
    }

    // --- Test 2: Verify button enabled/disabled behavior (will FAIL with current code) ---
    @Test
    fun addButton_isDisabledUntilBothInputsHaveValues_thenEnabled() {
        // Initially disabled
        composeRule.onNodeWithTag("button-add").assertIsNotEnabled()

        // One field filled -> still disabled
        composeRule.onNodeWithTag("text-a").performTextInput("5")
        composeRule.onNodeWithTag("button-add").assertIsNotEnabled()

        // Both fields filled -> enabled
        composeRule.onNodeWithTag("text-b").performTextInput("3")
        composeRule.onNodeWithTag("button-add").assertIsEnabled()
    }
}
