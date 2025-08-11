package com.example.mvvmclassdemoapp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
//Add dependency androidx.lifecycle lifecycle.viewmodel.compose@2.8.4
//Import viewModel function
fun TeamView(vm: TeamViewModel) {
    Column()
    {
        Text("Teams:") // styling should be applied
        for (team in vm.teamListState.value) {
            Text("""${team.name}: ${team.city}""")
        }
    }

}