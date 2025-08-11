package com.example.roomdbclassdemo

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeamRepository(private val teamDao: TeamDao) {
    val allTeams: LiveData<List<Team>> = teamDao.getAllTeams()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    fun insertTeam(team: Team) {
        coroutineScope.launch(Dispatchers.IO) {
            teamDao.insertTeam(team)
        }
    }
}