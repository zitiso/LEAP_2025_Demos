package com.example.roomdbclassdemo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel


class TeamViewModel(application: Application) : ViewModel() {
    val allTeams: LiveData<List<Team>>
    private val repository: TeamRepository

    init {
        val teamDb = TeamDatabase.getInstance(application)
        val teamDao = teamDb.teamDao()
        repository = TeamRepository(teamDao)

        allTeams = repository.allTeams
    }

    fun insertTeam(team: Team) { repository.insertTeam(team) }
}