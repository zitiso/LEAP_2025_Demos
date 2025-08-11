package com.example.roomdbclassdemo


import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun InsertTeamView(vm :TeamViewModel) {
    var teamName by remember { mutableStateOf("") }
    var teamCity by remember { mutableStateOf("") }
    Text("Add a Team")
    OutlinedTextField( value = teamName, // City TextField not shown
        onValueChange = {teamName = it}, // styling/layout not shown
        label = { Text("Team name") } )
    OutlinedTextField( value = teamCity, // City TextField not shown
        onValueChange = {teamCity = it}, // styling/layout not shown
        label = { Text("Team city") } )
    Button(
        enabled = teamName != "" && teamCity != "", // enable if name/city entered
        onClick = {
            vm.insertTeam(Team(teamName,teamCity)) // insert data in database
            teamName = ""
            teamCity = ""
        } ) { Text( "Submit" ) }
}