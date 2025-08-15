package com.example.testingdemoapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.assertTextEquals
import org.junit.Rule
import org.junit.Test

class AdditionInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun clickingAdd_showsSumInOutputField() {
        // Enter numbers
        composeRule.onNodeWithTag("text-a").performTextInput("7")
        composeRule.onNodeWithTag("text-b").performTextInput("8")
        // Click ADD
        composeRule.onNodeWithTag("button-add").performClick()
        // Verify result
        composeRule.onNodeWithTag("text-c").assertTextEquals("15")
    }
}
