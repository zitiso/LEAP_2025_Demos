package com.example.restclassdemoapp

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*

@Composable
fun InsertTeamView(vm: TeamViewModel) {
    var teamName by remember { mutableStateOf("") }
    var teamCity by remember { mutableStateOf("") }

    Text("Add a Team")
    OutlinedTextField(value = teamName,
        onValueChange = { teamName = it },
        label = { Text("Team name") })
    OutlinedTextField(value = teamCity,
        onValueChange = { teamCity = it },
        label = { Text("Team city") })
    Button(
        enabled = teamCity != "" && teamName!= "",
        onClick = { vm.addTeam(Team(teamName, teamCity))
            teamName = ""
            teamCity = "" })
    {
        Text("Submit")
    }
}