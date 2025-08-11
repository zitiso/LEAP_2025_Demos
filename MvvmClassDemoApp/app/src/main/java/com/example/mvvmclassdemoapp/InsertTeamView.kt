package com.example.mvvmclassdemoapp

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun InsertTeamView(vm: TeamViewModel = viewModel()) {
    var teamName by remember { mutableStateOf("") }
    var teamCity by remember { mutableStateOf("") }
    Text("Add a Team")
    OutlinedTextField(value = teamName,
        onValueChange = { teamName = it },
        label = { Text("Team name") })
    OutlinedTextField(value = teamCity,
        onValueChange = { teamCity = it },
        label = { Text("Team city") })
    Button(enabled = teamCity != "" && teamName!= "", onClick = { vm.addTeam(teamName, teamCity); teamName = ""; teamCity = "" }) {
        Text("Submit")
    }
}
