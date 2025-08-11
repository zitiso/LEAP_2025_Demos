package com.example.restclassdemoapp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun TeamView(teamViewModel: TeamViewModel) {
    LaunchedEffect(Unit, block = { teamViewModel.getTeams() })
    Column() {
        Text("Teams")
        for( team in teamViewModel.teamList ) {
            Text("""${team.name}: ${team.city}""")
        }
    }
}