package com.example.testingdemoapp

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AdditionViewModelInstrumentedTest {

    @Test
    fun add_returnsCorrectSum() {
        val vm = AdditionViewModel()
        assertThat(vm.add(2, 3)).isEqualTo(5)
        assertThat(vm.add(0, 5)).isEqualTo(5)
        assertThat(vm.add(-4, -6)).isEqualTo(-10)
    }
}
