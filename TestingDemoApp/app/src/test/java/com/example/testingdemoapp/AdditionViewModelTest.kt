package com.example.testingdemoapp

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AdditionViewModelTest {

    lateinit var additionViewModel : AdditionViewModel

    @Before
    fun setup() {
        additionViewModel = AdditionViewModel()
    }

    @Test
    fun additionIsCorrect() {
        Assert.assertEquals(4, additionViewModel.add(2, 2))
        Assert.assertEquals(3, additionViewModel.add(10, -7))
    }
}